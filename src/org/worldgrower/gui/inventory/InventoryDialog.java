/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.gui.inventory;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.MenuFactory;

public class InventoryDialog extends AbstractDialog {

	private static final String MONEY_PLAYER_CHARACTER_TOOL_TIP = "shows amount of gold that the player character has";
	private static final String WEIGHT_PLAYER_CHARACTER_TOOL_TIP = "shows current weight of things that the player character is carrying and maximum weight";
	private static final String MONEY_TARGET_TOOL_TIP = "shows amount of gold that a character has";
	private static final String WEIGHT_TARGET_TOOL_TIP = "shows current weight of things that a character is carrying and maximum weight";
	private static final String PRICES_TOOL_TIP = "show list of items with associated prices. These prices are used instead of the default prices when an item is sold by the player character";
	
	private final ImageInfoReader imageInfoReader;
	
	private JPanel rootInventoryPanel;
	
	private JPanel inventoryPanel;
	private JList<InventoryItem> inventoryJList;
	private JButton okButton;

	private JLabel moneyValueLabel;
	private JLabel weightLabelValue;

	private JPanel targetInventoryPanel;
	private JList<InventoryItem> targetInventoryList;
	private JLabel targetMoney;
	private JLabel targetWeight;
	
	private JButton pricesButton;
	
	private JPanel containersPanel;
	private JLabel lblPlayercharacter;
	private JLabel lblTarget;
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			InventoryDialog.this.dispose();
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public InventoryDialog(InventoryDialogModel inventoryDialogModel, ImageInfoReader imageInfoReader, InventoryActionFactory inventoryActionFactory) {
		super(762, 690);
		this.imageInfoReader = imageInfoReader;
		
		initializeGUI(inventoryDialogModel, imageInfoReader, inventoryActionFactory);
	}

	private void initializeGUI(InventoryDialogModel inventoryDialogModel, ImageInfoReader imageInfoReader, InventoryActionFactory inventoryActionFactory) {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(378, 595, 354, 40);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);

		okButton = ButtonFactory.createButton("Ok");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new CloseDialogAction());

		rootInventoryPanel = JPanelFactory.createBorderlessPanel();
		rootInventoryPanel.setBounds(12, 12, 500, 450);
		CardLayout cardLayout = new CardLayout();
		rootInventoryPanel.setLayout(cardLayout);
		addComponent(rootInventoryPanel);
		
		inventoryPanel = JPanelFactory.createBorderlessPanel();
		inventoryPanel.setBounds(0, 0, 500, 450);
		inventoryPanel.setLayout(null);
		inventoryPanel.setOpaque(true);
		inventoryPanel.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		rootInventoryPanel.add(inventoryPanel, "player");
		
		inventoryJList = createInventoryList(inventoryDialogModel.getPlayerCharacterInventory(), imageInfoReader);
		
		JScrollPane inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setViewportView(inventoryJList);
		inventoryScrollPane.setBounds(12, 82, 200, 450);
		inventoryPanel.add(inventoryScrollPane);
		
		final JLabel moneyLabel = JLabelFactory.createJLabel("Money:");
		moneyLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyLabel.setBounds(312, 200, 64, 25);
		inventoryPanel.add(moneyLabel);
		
		moneyValueLabel = JLabelFactory.createJLabel(inventoryDialogModel.getPlayerCharacterMoney());
		moneyValueLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyValueLabel.setBounds(377, 200, 50, 25);
		inventoryPanel.add(moneyValueLabel);
		
		JLabel lblWeight = JLabelFactory.createJLabel("Weight:");
		lblWeight.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		lblWeight.setBounds(312, 250, 64, 25);
		inventoryPanel.add(lblWeight);
		
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue = JLabelFactory.createJLabel(weightString);
		weightLabelValue.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		weightLabelValue.setBounds(377, 250, 64, 25);
		inventoryPanel.add(weightLabelValue);
		
		pricesButton = ButtonFactory.createButton("Prices");
		pricesButton.setToolTipText(PRICES_TOOL_TIP);
		pricesButton.setBounds(224, 399, 100, 25);
		inventoryPanel.add(pricesButton);

		if (inventoryDialogModel.hasTarget()) {
			targetInventoryPanel = JPanelFactory.createBorderlessPanel();
			targetInventoryPanel.setLayout(null);
			targetInventoryPanel.setBounds(0, 0, 500, 450);
			targetInventoryPanel.setOpaque(true);
			targetInventoryPanel.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
			rootInventoryPanel.add(targetInventoryPanel, "target");
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 82, 200, 450);
			targetInventoryPanel.add(scrollPane);
			
			targetInventoryList = createInventoryList(inventoryDialogModel.getTargetInventory(), imageInfoReader);
			scrollPane.setViewportView(targetInventoryList);
			
			if (inventoryDialogModel.hasTargetMoney()) {
				JLabel targetMoneyLabel = JLabelFactory.createJLabel("Money:");
				targetMoneyLabel.setToolTipText(MONEY_TARGET_TOOL_TIP);
				targetMoneyLabel.setBounds(312, 200, 64, 25);
				targetMoneyLabel.addMouseListener(new StealMoneyMouseListener(inventoryActionFactory));
				targetInventoryPanel.add(targetMoneyLabel);
				
				targetMoney = JLabelFactory.createJLabel(inventoryDialogModel.getTargetMoney());
				targetMoney.setToolTipText(MONEY_TARGET_TOOL_TIP);
				targetMoney.setBounds(377, 200, 50, 25);
				targetMoney.addMouseListener(new StealMoneyMouseListener(inventoryActionFactory));
				targetInventoryPanel.add(targetMoney);
			}
			
			if (inventoryDialogModel.hasTargetCarryingCapacity()) {
				JLabel targetWeightLabel = JLabelFactory.createJLabel("Weight:");
				targetWeightLabel.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeightLabel.setBounds(312, 250, 64, 25);
				targetInventoryPanel.add(targetWeightLabel);
				
				String targetWeightString = getTargetWeight(inventoryDialogModel);
				targetWeight = JLabelFactory.createJLabel(targetWeightString);
				targetWeight.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeight.setBounds(377, 250, 64, 25);
				targetInventoryPanel.add(targetWeight);
			}
			
			containersPanel = JPanelFactory.createBorderlessPanel();
			containersPanel.setLayout(null);
			containersPanel.setBounds(12, 540, 400, 50);
			addComponent(containersPanel);
			
			lblPlayercharacter = JLabelFactory.createJLabel(inventoryDialogModel.getPlayerCharacterName(), inventoryDialogModel.getPlayerCharacterImage(imageInfoReader));
			lblPlayercharacter.setToolTipText(inventoryDialogModel.getPlayerCharacterName());
			lblPlayercharacter.setBounds(12, 5, 148, 48);
			lblPlayercharacter.addMouseListener(new SwitchPanelMouseAdapter(this::setPlayerCharacterPanelOnTop));
			containersPanel.add(lblPlayercharacter);
			
			lblTarget = JLabelFactory.createJLabel(inventoryDialogModel.getTargetName(), inventoryDialogModel.getTargetImage(imageInfoReader));
			lblTarget.setToolTipText(inventoryDialogModel.getTargetName());
			lblTarget.setBounds(250, 5, 148, 48);
			lblTarget.addMouseListener(new SwitchPanelMouseAdapter(this::setTargetInventoryOnTop));
			containersPanel.add(lblTarget);
			
			setPlayerCharacterPanelOnTop();
		}
		
		setInventoryActions(inventoryDialogModel.getPlayerCharacterPrices());
		addPopupMenuToInventoryList(inventoryDialogModel, inventoryActionFactory);
	}
	
	private static class SwitchPanelMouseAdapter extends MouseAdapter {

		private final Procedure switchFunction;
		
		public SwitchPanelMouseAdapter(Procedure switchFunction) {
			super();
			this.switchFunction = switchFunction;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			switchFunction.execute();
		}
	}
	
	private static class StealMoneyMouseListener extends MouseAdapter {
		private final InventoryActionFactory inventoryActionFactory;
		
		public StealMoneyMouseListener(InventoryActionFactory inventoryActionFactory) {
			super();
			this.inventoryActionFactory = inventoryActionFactory;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
			for(Action inventoryDialogAction : inventoryActionFactory.getTargetMoneyActions()) {
				JMenuItem actionMenuItem = MenuFactory.createJMenuItem(inventoryDialogAction);
				actionMenuItem.setToolTipText((String) inventoryDialogAction.getValue(Action.LONG_DESCRIPTION));
				popupMenu.add(actionMenuItem);
			}
	        popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void setPlayerCharacterPanelOnTop() {
		CardLayout cardLayout = (CardLayout) rootInventoryPanel.getLayout();
		cardLayout.show(rootInventoryPanel, "player");
		lblPlayercharacter.setFont(Fonts.BOLD_FONT);
		lblTarget.setFont(Fonts.FONT);
	}
	
	private void setTargetInventoryOnTop() {
		CardLayout cardLayout = (CardLayout) rootInventoryPanel.getLayout();
		cardLayout.show(rootInventoryPanel, "target");
		lblPlayercharacter.setFont(Fonts.FONT);
		lblTarget.setFont(Fonts.BOLD_FONT);
	}
	
	private interface Procedure {
		public void execute();
	}
	
	private void addPopupMenuToInventoryList(InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		inventoryJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
		        InventoryItem inventoryItem = inventoryJList.getSelectedValue();
		        
		        if (inventoryItem != null) {
			        JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
			        JCheckBoxMenuItem sellableMenuItem = createSellableMenuItem(inventoryItem);
					popupMenu.add(sellableMenuItem);
					addPlayerCharacterMenuActions(popupMenu, inventoryItem, inventoryDialogModel, inventoryActionFactory);
			        popupMenu.show(inventoryJList, e.getX(), e.getY());
		        }
			}

			private JCheckBoxMenuItem createSellableMenuItem(InventoryItem inventoryItem) {
				JCheckBoxMenuItem sellableMenuItem = MenuFactory.createJCheckBoxMenuItem(new SellableAction(inventoryItem));
				sellableMenuItem.setIcon(new ImageIcon(imageInfoReader.getImage(ImageIds.SILVER_COIN, null)));
				sellableMenuItem.setToolTipText("marks inventory item as sellable");
				return sellableMenuItem;
			}
		});
		
		if (targetInventoryList != null) {
			targetInventoryList.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
			        InventoryItem inventoryItem = targetInventoryList.getSelectedValue();
			        
			        if (inventoryItem != null) {
				        JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
						addTargetMenuActions(popupMenu, inventoryItem, inventoryDialogModel, inventoryActionFactory);
				        popupMenu.show(targetInventoryList, e.getX(), e.getY());
			        }
				}
			});
		}
	}
	
	private class SellableAction extends AbstractAction {

		public SellableAction(InventoryItem inventoryItem) {
			super();
			boolean sellable = inventoryItem.isSellable();
	        int price = inventoryItem.getPrice();
			String text = "mark as sellable for " + price + " gold";
		
			this.putValue(Action.NAME, text);
			this.putValue(Action.SELECTED_KEY, sellable);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem source = (JCheckBoxMenuItem)e.getSource();
			inventoryJList.getSelectedValue().setSellable(source.isSelected());
		}
	}
	


	private String getTargetWeight(InventoryDialogModel inventoryDialogModel) {
		String targetWeightString = Integer.toString(inventoryDialogModel.getTargetWeight())
				+ "/"
				+Integer.toString(inventoryDialogModel.getTargetCarryingCapacity());
		return targetWeightString;
	}

	private String getPlayerCharacterWeight(InventoryDialogModel inventoryDialogModel) {
		String weightString = Integer.toString(inventoryDialogModel.getPlayerCharacterWeight())
							+ "/"
							+Integer.toString(inventoryDialogModel.getPlayerCharacterCarryingCapacity());
		return weightString;
	}

	private void setInventoryActions(Prices pricesOnPlayer) {
		pricesButton.addActionListener(e -> new PricesDialog(pricesOnPlayer).showMe());
	}

	private void addPlayerCharacterMenuActions(JPopupMenu popupMenu, InventoryItem inventoryItem, InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		for(Action inventoryDialogAction : inventoryActionFactory.getPlayerCharacterInventoryActions(inventoryItem.getId())) {
			JMenuItem actionMenuItem = MenuFactory.createJMenuItem(inventoryDialogAction);
			actionMenuItem.setToolTipText((String) inventoryDialogAction.getValue(Action.LONG_DESCRIPTION));
			popupMenu.add(actionMenuItem);
		}
	}
	
	private void addTargetMenuActions(JPopupMenu popupMenu, InventoryItem inventoryItem, InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		for(Action inventoryDialogAction : inventoryActionFactory.getTargetInventoryActions(inventoryItem.getId())) {
			JMenuItem actionMenuItem = MenuFactory.createJMenuItem(inventoryDialogAction);
			actionMenuItem.setToolTipText((String) inventoryDialogAction.getValue(Action.LONG_DESCRIPTION));
			popupMenu.add(actionMenuItem);
		}
	}

	private JList<InventoryItem> createInventoryList(WorldObjectContainer inventory, ImageInfoReader imageInfoReader) {
		DefaultListModel<InventoryItem> listModel = getInventoryListModel(inventory);
		JList<InventoryItem> inventoryList = new InventoryJList(listModel);
		inventoryList.setCellRenderer(new InventoryListCellRenderer(imageInfoReader));
		
		return inventoryList;
	}
	
	private static class InventoryJList extends JList<InventoryItem> {
		public InventoryJList(ListModel<InventoryItem> listModel) {
			super(listModel);
		}

		@Override
        public JToolTip createToolTip() {
            JScrollableToolTip tip = new JScrollableToolTip(250, 130);
            tip.setComponent(this);
            return tip;
        }
	}

	private DefaultListModel<InventoryItem> getInventoryListModel(WorldObjectContainer inventory) {
		DefaultListModel<InventoryItem> listModel = new DefaultListModel<>();
		for(int index=0; index < inventory.size(); index++) {
			WorldObject inventoryItem = inventory.get(index);
			if (inventoryItem != null) {
				listModel.addElement(new InventoryItem(index, inventoryItem));
			}
		}
		return listModel;
	}
	
	public void showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void refresh(InventoryDialogModel inventoryDialogModel) {
		inventoryJList.setModel(getInventoryListModel(inventoryDialogModel.getPlayerCharacterInventory()));
		moneyValueLabel.setText(Integer.toString(inventoryDialogModel.getPlayerCharacterMoney()));
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue.setText(weightString);
		
		if (inventoryDialogModel.hasTarget()) {
			targetInventoryList.setModel(getInventoryListModel(inventoryDialogModel.getTargetInventory()));
			
			if (inventoryDialogModel.hasTargetMoney()) {
				targetMoney.setText(Integer.toString(inventoryDialogModel.getTargetMoney()));
			}
			if (inventoryDialogModel.hasTargetCarryingCapacity()) {
				String targetWeightString = getTargetWeight(inventoryDialogModel);
				targetWeight.setText(targetWeightString);
			}
		}
		
		setInventoryActions(inventoryDialogModel.getPlayerCharacterPrices());
	}

	public InventoryItem getPlayerCharacterSelectedValue() {
		return inventoryJList.getSelectedValue();
	}
	
	public InventoryItem getTargetSelectedValue() {
		return targetInventoryList.getSelectedValue();
	}
}