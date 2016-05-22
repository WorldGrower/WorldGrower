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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.knowledge.ImageCellRenderer;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JLabelFactory;
import org.worldgrower.gui.util.JPanelFactory;
import org.worldgrower.gui.util.JTableFactory;
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
	private JTable inventoryTable;
	private JButton okButton;

	private JLabel moneyValueLabel;
	private JLabel weightLabelValue;

	private JPanel targetInventoryPanel;
	private JTable targetInventoryTable;
	private JLabel targetMoney;
	private JLabel targetWeight;
	
	private JButton pricesButton;
	
	private JPanel containersPanel;
	private JToggleButton playercharacterToggleButton;
	private JToggleButton targetToggleButton;
	
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

		okButton = JButtonFactory.createButton("Ok");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new CloseDialogAction());

		rootInventoryPanel = JPanelFactory.createBorderlessPanel();
		rootInventoryPanel.setBounds(12, 12, 650, 550);
		CardLayout cardLayout = new CardLayout();
		rootInventoryPanel.setLayout(cardLayout);
		addComponent(rootInventoryPanel);
		
		inventoryPanel = JPanelFactory.createBorderlessPanel();
		inventoryPanel.setBounds(0, 0, 650, 550);
		inventoryPanel.setLayout(null);
		inventoryPanel.setOpaque(true);
		inventoryPanel.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		rootInventoryPanel.add(inventoryPanel, "player");
		
		inventoryTable = createInventoryTable(inventoryDialogModel.getPlayerCharacterInventory(), imageInfoReader);
		
		JScrollPane inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setViewportView(inventoryTable);
		inventoryScrollPane.setBounds(12, 12, 400, 450);
		inventoryScrollPane.getViewport().setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		inventoryPanel.add(inventoryScrollPane);
		
		final JLabel moneyLabel = JLabelFactory.createJLabel("Money:");
		moneyLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyLabel.setBounds(412, 200, 64, 25);
		inventoryPanel.add(moneyLabel);
		
		moneyValueLabel = JLabelFactory.createJLabel(inventoryDialogModel.getPlayerCharacterMoney());
		moneyValueLabel.setToolTipText(MONEY_PLAYER_CHARACTER_TOOL_TIP);
		moneyValueLabel.setBounds(477, 200, 50, 25);
		inventoryPanel.add(moneyValueLabel);
		
		JLabel lblWeight = JLabelFactory.createJLabel("Weight:");
		lblWeight.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		lblWeight.setBounds(412, 250, 64, 25);
		inventoryPanel.add(lblWeight);
		
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue = JLabelFactory.createJLabel(weightString);
		weightLabelValue.setToolTipText(WEIGHT_PLAYER_CHARACTER_TOOL_TIP);
		weightLabelValue.setBounds(477, 250, 64, 25);
		inventoryPanel.add(weightLabelValue);
		
		pricesButton = JButtonFactory.createButton("Prices");
		pricesButton.setToolTipText(PRICES_TOOL_TIP);
		pricesButton.setBounds(424, 399, 100, 25);
		inventoryPanel.add(pricesButton);

		if (inventoryDialogModel.hasTarget()) {
			targetInventoryPanel = JPanelFactory.createBorderlessPanel();
			targetInventoryPanel.setLayout(null);
			targetInventoryPanel.setBounds(0, 0, 650, 550);
			targetInventoryPanel.setOpaque(true);
			targetInventoryPanel.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
			rootInventoryPanel.add(targetInventoryPanel, "target");
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 12, 400, 450);
			scrollPane.getViewport().setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
			targetInventoryPanel.add(scrollPane);
			
			targetInventoryTable = createInventoryTable(inventoryDialogModel.getTargetInventory(), imageInfoReader);
			scrollPane.setViewportView(targetInventoryTable);
			
			if (inventoryDialogModel.hasTargetMoney()) {
				JLabel targetMoneyLabel = JLabelFactory.createJLabel("Money:");
				targetMoneyLabel.setToolTipText(MONEY_TARGET_TOOL_TIP);
				targetMoneyLabel.setBounds(412, 200, 64, 25);
				targetInventoryPanel.add(targetMoneyLabel);
				
				targetMoney = JLabelFactory.createJLabel(inventoryDialogModel.getTargetMoney());
				targetMoney.setToolTipText(MONEY_TARGET_TOOL_TIP);
				targetMoney.setBounds(477, 200, 50, 25);
				targetInventoryPanel.add(targetMoney);
				
				Image stealGoldImage = imageInfoReader.getImage(Actions.STEAL_GOLD_ACTION.getImageIds(), null);
				JButton stealMoneyButton = JButtonFactory.createButton("Steal gold", new ImageIcon(stealGoldImage));
				stealMoneyButton.setToolTipText("steal gold");
				stealMoneyButton.setBounds(527, 200, 120, 50);
				stealMoneyButton.addActionListener(inventoryActionFactory.getTargetMoneyActions().get(0));
				targetInventoryPanel.add(stealMoneyButton);
			}
			
			if (inventoryDialogModel.hasTargetCarryingCapacity()) {
				JLabel targetWeightLabel = JLabelFactory.createJLabel("Weight:");
				targetWeightLabel.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeightLabel.setBounds(412, 250, 64, 25);
				targetInventoryPanel.add(targetWeightLabel);
				
				String targetWeightString = getTargetWeight(inventoryDialogModel);
				targetWeight = JLabelFactory.createJLabel(targetWeightString);
				targetWeight.setToolTipText(WEIGHT_TARGET_TOOL_TIP);
				targetWeight.setBounds(477, 250, 64, 25);
				targetInventoryPanel.add(targetWeight);
			}
			
			containersPanel = JPanelFactory.createBorderlessPanel();
			containersPanel.setLayout(null);
			containersPanel.setBounds(12, 565, 400, 50);
			addComponent(containersPanel);
			
			playercharacterToggleButton = JButtonFactory.createToggleButton(inventoryDialogModel.getPlayerCharacterName(), new ImageIcon(inventoryDialogModel.getPlayerCharacterImage(imageInfoReader)));
			playercharacterToggleButton.setToolTipText(inventoryDialogModel.getPlayerCharacterName());
			playercharacterToggleButton.setBounds(0, 0, 200, 50);
			playercharacterToggleButton.setOpaque(true);
			playercharacterToggleButton.addActionListener(this::setPlayerCharacterPanelOnTop);
			containersPanel.add(playercharacterToggleButton);
			
			targetToggleButton = JButtonFactory.createToggleButton(inventoryDialogModel.getTargetName(), new ImageIcon(inventoryDialogModel.getTargetImage(imageInfoReader)));
			targetToggleButton.setToolTipText(inventoryDialogModel.getTargetName());
			targetToggleButton.setBounds(200, 0, 200, 50);
			targetToggleButton.setOpaque(true);
			targetToggleButton.addActionListener(this::setTargetInventoryOnTop);
			containersPanel.add(targetToggleButton);
			
			setPlayerCharacterPanelOnTop(null);
		}
		
		setInventoryActions(inventoryDialogModel.getPlayerCharacterPrices());
		addPopupMenuToInventoryList(inventoryDialogModel, inventoryActionFactory);
	}

	private void setPlayerCharacterPanelOnTop(ActionEvent e) {
		CardLayout cardLayout = (CardLayout) rootInventoryPanel.getLayout();
		cardLayout.show(rootInventoryPanel, "player");
		playercharacterToggleButton.setFont(Fonts.BOLD_FONT);
		playercharacterToggleButton.setSelected(true);
		targetToggleButton.setFont(Fonts.FONT);
		targetToggleButton.setSelected(false);
	}
	
	private void setTargetInventoryOnTop(ActionEvent e) {
		CardLayout cardLayout = (CardLayout) rootInventoryPanel.getLayout();
		cardLayout.show(rootInventoryPanel, "target");
		playercharacterToggleButton.setFont(Fonts.FONT);
		playercharacterToggleButton.setSelected(false);
		targetToggleButton.setFont(Fonts.BOLD_FONT);
		targetToggleButton.setSelected(true);
	}

	private void addPopupMenuToInventoryList(InventoryDialogModel inventoryDialogModel, InventoryActionFactory inventoryActionFactory) {
		inventoryTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
		        InventoryItem inventoryItem = getPlayerCharacterSelectedValue();
		        
		        if (inventoryItem != null) {
			        JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
			        JCheckBoxMenuItem sellableMenuItem = createSellableMenuItem(inventoryItem);
					popupMenu.add(sellableMenuItem);
					addPlayerCharacterMenuActions(popupMenu, inventoryItem, inventoryDialogModel, inventoryActionFactory);
			        popupMenu.show(inventoryTable, e.getX(), e.getY());
		        }
			}

			private JCheckBoxMenuItem createSellableMenuItem(InventoryItem inventoryItem) {
				JCheckBoxMenuItem sellableMenuItem = MenuFactory.createJCheckBoxMenuItem(new SellableAction(inventoryItem));
				sellableMenuItem.setIcon(new ImageIcon(imageInfoReader.getImage(ImageIds.SILVER_COIN, null)));
				sellableMenuItem.setToolTipText("marks inventory item as sellable");
				return sellableMenuItem;
			}
		});
		
		if (targetInventoryTable != null) {
			targetInventoryTable.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
			        InventoryItem inventoryItem = getTargetSelectedValue();
			        
			        if (inventoryItem != null) {
				        JPopupMenu popupMenu = MenuFactory.createJPopupMenu();
						addTargetMenuActions(popupMenu, inventoryItem, inventoryDialogModel, inventoryActionFactory);
				        popupMenu.show(targetInventoryTable, e.getX(), e.getY());
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
			getPlayerCharacterSelectedValue().setSellable(source.isSelected());
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

	private JTable createInventoryTable(WorldObjectContainer inventory, ImageInfoReader imageInfoReader) {
		JTable inventoryTable = JTableFactory.createJTable(new InventoryModel(inventory));
		
		inventoryTable.setDefaultRenderer(ImageIds.class, new ImageCellRenderer(imageInfoReader));
		inventoryTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		inventoryTable.setRowHeight(50);
		inventoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(97);
		inventoryTable.getTableHeader().setReorderingAllowed(false);
		
		inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inventoryTable.setAutoCreateRowSorter(true);
		inventoryTable.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		inventoryTable.setOpaque(true);
		
		return inventoryTable;
	}
	
	private static List<InventoryItem> getInventoryList(WorldObjectContainer inventory) {
		List<InventoryItem> inventoryList = new ArrayList<>();
		for(int index=0; index < inventory.size(); index++) {
			WorldObject inventoryItem = inventory.get(index);
			if (inventoryItem != null) {
				inventoryList.add(new InventoryItem(index, inventoryItem));
			}
		}
		return inventoryList;
	}
	
	public void showMe() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void refresh(InventoryDialogModel inventoryDialogModel) {
		inventoryTable.setModel(new InventoryModel(inventoryDialogModel.getPlayerCharacterInventory()));
		moneyValueLabel.setText(Integer.toString(inventoryDialogModel.getPlayerCharacterMoney()));
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue.setText(weightString);
		
		if (inventoryDialogModel.hasTarget()) {
			targetInventoryTable.setModel(new InventoryModel(inventoryDialogModel.getTargetInventory()));
			
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
		return getSelectedValue(inventoryTable);
	}

	private InventoryItem getSelectedValue(JTable inventoryTable) {
		int selectedRow = inventoryTable.convertRowIndexToModel(inventoryTable.getSelectedRow());
		InventoryModel inventoryModel = (InventoryModel) inventoryTable.getModel();
		return inventoryModel.getInventoryItem(selectedRow);
	}
	
	public InventoryItem getTargetSelectedValue() {
		return getSelectedValue(targetInventoryTable);
	}
	
	private static class InventoryModel extends AbstractTableModel {

		private final List<InventoryItem> inventoryItems;
		
		public InventoryModel(WorldObjectContainer inventory) {
			inventoryItems = getInventoryList(inventory);
		}
		
		@Override
		public int getRowCount() {
			return inventoryItems.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Image";
			} else if (columnIndex == 1) {
				return "Description";
			} else if (columnIndex == 2) {
				return "Sellable";
			} else if (columnIndex == 3) {
				return "Weight";
			} else {
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return ImageIds.class;
			} else if (columnIndex == 2) {
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return inventoryItems.get(rowIndex).getImageId();
			} else if (columnIndex == 1) {
				String description = inventoryItems.get(rowIndex).getDescription();
				int quantity = inventoryItems.get(rowIndex).getQuantity();
				if (quantity == 1) {
					return description;
				} else {
					return description + "(" + quantity + ")";
				}
			} else if (columnIndex == 2) {
				return inventoryItems.get(rowIndex).isSellable();
			} else if (columnIndex == 3) {
				return inventoryItems.get(rowIndex).getWeight();
			}
			return null;
		}
		
		public InventoryItem getInventoryItem(int index) {
			return inventoryItems.get(index);
		}
	}
}