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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.ButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.MenuFactory;

public class InventoryDialog extends AbstractDialog {

	private static final String MONEY_PLAYER_CHARACTER_TOOL_TIP = "shows amount of gold that the player character has";
	private static final String WEIGHT_PLAYER_CHARACTER_TOOL_TIP = "shows current weight of things that the player character is carrying and maximum weight";
	private static final String MONEY_TARGET_TOOL_TIP = "shows amount of gold that a character has";
	private static final String WEIGHT_TARGET_TOOL_TIP = "shows current weight of things that a character is carrying and maximum weight";
	private static final String PRICES_TOOL_TIP = "show list of items with associated prices. These prices are used instead of the default prices when an item is sold by the player character";
	
	private final ImageInfoReader imageInfoReader;
	
	private JList<InventoryItem> inventoryJList;
	private JButton okButton;
	private JButton okButton2;
	private JButton cancelButton;
	private JLabel moneyValueLabel;
	private JLabel weightLabelValue;

	private JList<InventoryItem> targetInventoryList;
	private JLabel targetMoney;
	private JLabel targetWeight;
	
	private JButton pricesButton;
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			InventoryDialog.this.dispose();
		}
	}
	
	private final class ApplyAndCloseAction implements ActionListener {
		
		private final WorldObjectContainer inventory;
		public ApplyAndCloseAction(WorldObjectContainer inventory) {
			this.inventory = inventory;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			for(int index = 0; index < inventoryJList.getModel().getSize(); index++) {
				InventoryItem inventoryItem = inventoryJList.getModel().getElementAt(index);
				
				inventory.setProperty(index, Constants.SELLABLE, inventoryItem.isSellable());
			}
			InventoryDialog.this.dispose();
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public InventoryDialog(InventoryDialogModel inventoryDialogModel, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, InventoryActionFactory inventoryActionFactory) {
		super(762, 690);
		this.imageInfoReader = imageInfoReader;
		if (inventoryDialogAction == null) {
			inventoryDialogAction = new DefaultInventoryDialogAction(inventoryDialogModel.getPlayerCharacterInventory());
		}
		
		initializeGUI(inventoryDialogModel, inventoryDialogAction, imageInfoReader, inventoryActionFactory);
		
		okButton.addActionListener(inventoryDialogAction.getGuiAction());
		if (inventoryDialogAction.getGuiAction2() != null) {
			okButton2.addActionListener(inventoryDialogAction.getGuiAction2());
		}
		addActions(inventoryDialogAction);
	}

	private void initializeGUI(InventoryDialogModel inventoryDialogModel, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, InventoryActionFactory inventoryActionFactory) {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(378, 595, 354, 40);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);

		okButton = ButtonFactory.createButton(inventoryDialogAction.getDescription());
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		if (inventoryDialogAction.getGuiAction2() != null) {
			okButton2 = ButtonFactory.createButton(inventoryDialogAction.getDescription2());
			buttonPane.add(okButton2);
		}
		
		cancelButton = ButtonFactory.createButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		inventoryJList = createInventoryList(inventoryDialogModel.getPlayerCharacterInventory(), imageInfoReader);
		
		JScrollPane inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setViewportView(inventoryJList);
		inventoryScrollPane.setBounds(12, 82, 200, 450);
		addComponent(inventoryScrollPane);
		
		final JLabel moneyLabel = JLabelFactory.createJLabel("Money:");
		moneyLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyLabel.setBounds(12, 555, 64, 25);
		addComponent(moneyLabel);
		
		moneyValueLabel = JLabelFactory.createJLabel(inventoryDialogModel.getPlayerCharacterMoney());
		moneyValueLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyValueLabel.setBounds(77, 555, 50, 25);
		addComponent(moneyValueLabel);
		
		JLabel lblWeight = JLabelFactory.createJLabel("Weight:");
		lblWeight.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		lblWeight.setBounds(127, 555, 64, 25);
		addComponent(lblWeight);
		
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue = JLabelFactory.createJLabel(weightString);
		weightLabelValue.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		weightLabelValue.setBounds(203, 555, 64, 25);
		addComponent(weightLabelValue);
		
		pricesButton = ButtonFactory.createButton("Prices");
		pricesButton.setToolTipText(PRICES_TOOL_TIP);
		pricesButton.setBounds(224, 399, 100, 25);
		addComponent(pricesButton);

		JLabel lblPlayercharacter = JLabelFactory.createJLabel(inventoryDialogModel.getPlayerCharacterImage(imageInfoReader));
		lblPlayercharacter.setToolTipText(inventoryDialogModel.getPlayerCharacterName());
		lblPlayercharacter.setBounds(12, 30, 48, 48);
		addComponent(lblPlayercharacter);
		
		

		if (inventoryDialogModel.hasTarget()) {
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(532, 82, 200, 450);
			addComponent(scrollPane);
			
			targetInventoryList = createInventoryList(inventoryDialogModel.getTargetInventory(), imageInfoReader);
			scrollPane.setViewportView(targetInventoryList);
			
			JLabel lblTarget = JLabelFactory.createJLabel(inventoryDialogModel.getTargetImage(imageInfoReader));
			lblTarget.setToolTipText(inventoryDialogModel.getTargetName());
			lblTarget.setBounds(532, 30, 48, 48);
			addComponent(lblTarget);
			
			JLabel targetMoneyLabel = JLabelFactory.createJLabel("Money:");
			targetMoneyLabel.setToolTipText(MONEY_TARGET_TOOL_TIP);
			targetMoneyLabel.setBounds(477, 555, 64, 25);
			addComponent(targetMoneyLabel);
			
			if (inventoryDialogModel.hasTargetMoney()) {
				targetMoney = JLabelFactory.createJLabel(inventoryDialogModel.getTargetMoney());
				targetMoney.setToolTipText(MONEY_TARGET_TOOL_TIP);
				targetMoney.setBounds(542, 555, 50, 25);
				addComponent(targetMoney);
			}
			
			if (inventoryDialogModel.hasTargetCarryingCapacity()) {
				JLabel targetWeightLabel = JLabelFactory.createJLabel("Weight:");
				targetWeightLabel.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeightLabel.setBounds(592, 555, 64, 25);
				addComponent(targetWeightLabel);
				
				String targetWeightString = getTargetWeight(inventoryDialogModel);
				targetWeight = JLabelFactory.createJLabel(targetWeightString);
				targetWeight.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeight.setBounds(668, 555, 64, 25);
				addComponent(targetWeight);
			}
		}
		
		setInventoryActions(inventoryDialogModel.getPlayerCharacterPrices());
		addPopupMenuToInventoryList(inventoryDialogModel, inventoryActionFactory);
		
		if (inventoryJList.getModel().getSize() == 0) {
			okButton.setEnabled(false);
		}
	}
	
	private void addPopupMenuToInventoryList(InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		inventoryJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
		        if (SwingUtilities.isRightMouseButton(e)) {
		        	int row = inventoryJList.locationToIndex(e.getPoint());
		        	inventoryJList.setSelectedIndex(row);
			        InventoryItem inventoryItem = inventoryJList.getSelectedValue();
			        
			        JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
			        JCheckBoxMenuItem sellableMenuItem = createSellableMenuItem(inventoryItem);
					popupMenu.add(sellableMenuItem);
			        addMenuActions(popupMenu, inventoryItem, inventoryDialogModel, inventoryActionFactory);
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

	private void addMenuActions(JPopupMenu popupMenu, InventoryItem inventoryItem, InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		for(Action inventoryDialogAction : inventoryActionFactory.getInventoryActions(inventoryItem.getId())) {
			JMenuItem actionMenuItem = MenuFactory.createJMenuItem(inventoryDialogAction);
			actionMenuItem.setToolTipText((String) inventoryDialogAction.getValue(Action.LONG_DESCRIPTION));
			popupMenu.add(actionMenuItem);
		}
	}

	public InventoryDialog(InventoryDialogModel inventoryDialogModel, ImageInfoReader imageInfoReader, InventoryActionFactory inventoryActionFactory) {
		this(inventoryDialogModel, null, imageInfoReader, inventoryActionFactory);
	}
	
	private class DefaultInventoryDialogAction implements InventoryDialogAction {

		private final WorldObjectContainer inventory;

		private DefaultInventoryDialogAction(WorldObjectContainer inventory) {
			this.inventory = inventory;
		}

		@Override
		public String getDescription() {
			return "OK";
		}
		
		@Override
		public String getDescription2() {
			return null;
		}

		@Override
		public ActionListener getGuiAction() {
			return new ApplyAndCloseAction(inventory);
		}
		
		@Override
		public ActionListener getGuiAction2() {
			return null;
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			return true;
		}

		@Override
		public InventoryItem getSelectedItem(InventoryDialog dialog) {
			return InventoryDialog.this.getPlayerCharacterSelectedValue();
		}
	}

	private void addActions(InventoryDialogAction inventoryDialogAction) {
		cancelButton.addActionListener(new CloseDialogAction());
		
		inventoryJList.addListSelectionListener(new InventoryListSelectionListener(inventoryDialogAction));
		inventoryJList.setSelectedIndex(0);

		if (targetInventoryList != null) {
			targetInventoryList.addListSelectionListener(new InventoryListSelectionListener(inventoryDialogAction));
			targetInventoryList.setSelectedIndex(0);
		}
		
		pricesButton.addActionListener(new ShowPriceDialogAction());
	}
	
	private static class ShowPriceDialogAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
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
	
	private class InventoryListSelectionListener implements ListSelectionListener {

		private final InventoryDialogAction inventoryDialogAction;
		
		private InventoryListSelectionListener(InventoryDialogAction inventoryDialogAction) {
			this.inventoryDialogAction = inventoryDialogAction;
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			InventoryItem inventoryItem = inventoryDialogAction.getSelectedItem(InventoryDialog.this);
			if (inventoryItem != null) {
				okButton.setEnabled(inventoryDialogAction.isPossible(inventoryItem));
			} else {
				okButton.setEnabled(false);
			}
		}		
	}
}