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
import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageInfoReader;

public class InventoryDialog extends JDialog {

	private JList<InventoryItem> inventoryJList;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel moneyValueLabel;
	
	private JRadioButton noSellRadioButton;
	private JRadioButton sellRadioButton;
	private JTextField priceTextField;
	
	private JButton actionsButton;
	
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
	public InventoryDialog(InventoryDialogModel inventoryDialogModel, WorldObjectContainer inventory, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		
		if (inventoryDialogAction == null) {
			inventoryDialogAction = new DefaultInventoryDialogAction(inventory);
		}
		
		initializeGUI(inventoryDialogModel, inventory, inventoryDialogAction, imageInfoReader, inventoryActions);
		
		okButton.addActionListener(inventoryDialogAction.getGuiAction());
		addActions(inventoryDialogAction);
	}

	private void initializeGUI(InventoryDialogModel inventoryDialogModel, WorldObjectContainer inventory, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 550, 582);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(266, 487, 254, 35);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		okButton = new JButton(inventoryDialogAction.getDescription());
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		inventoryJList = createInventoryList(inventory, imageInfoReader);
		
		JScrollPane inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setViewportView(inventoryJList);
		inventoryScrollPane.setBounds(12, 27, 200, 450);
		getContentPane().add(inventoryScrollPane);
		
		final JLabel moneyLabel = new JLabel("Money:");
		moneyLabel.setBounds(12, 497, 64, 25);
		getContentPane().add(moneyLabel);
		
		moneyValueLabel = new JLabel(Integer.toString(inventoryDialogModel.getMoney()));
		moneyValueLabel.setBounds(77, 500, 50, 22);
		getContentPane().add(moneyValueLabel);
		
		noSellRadioButton = new JRadioButton("Don't sell");
		noSellRadioButton.setBounds(220, 393, 100, 40);
		getContentPane().add(noSellRadioButton);
		
		sellRadioButton = new JRadioButton("Sell at price: ");
		sellRadioButton.setBounds(220, 438, 142, 40);
		getContentPane().add(sellRadioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(noSellRadioButton);
		group.add(sellRadioButton);
		
		priceTextField = new JTextField("0");
		priceTextField.setEditable(false);
		priceTextField.setBounds(370, 434, 50, 40);
		getContentPane().add(priceTextField);
		
		JLabel lblWeight = new JLabel("Weight:");
		lblWeight.setBounds(127, 497, 64, 25);
		getContentPane().add(lblWeight);
		
		String weightString = Integer.toString(inventoryDialogModel.getWeight())
							+ "/"
							+Integer.toString(inventoryDialogModel.getCarryingCapacity());
		JLabel weightLabelValue = new JLabel(weightString);
		weightLabelValue.setBounds(203, 500, 64, 22);
		getContentPane().add(weightLabelValue);
		
		
		actionsButton = new JButton("Actions");
		actionsButton.setBounds(436, 25, 84, 25);
		getContentPane().add(actionsButton);
		
		setInventoryActions(inventoryActions);
		
		if (inventoryJList.getModel().getSize() == 0) {
			noSellRadioButton.setEnabled(false);
			sellRadioButton.setEnabled(false);
			priceTextField.setEnabled(false);
			okButton.setEnabled(false);
		}
	}

	private void setInventoryActions(List<Action> inventoryActions) {
		if (inventoryActions.size() > 0) {
			actionsButton.setEnabled(true);
			addActionsToActionsButton(inventoryActions);
		} else {
			actionsButton.setEnabled(false);
		}
	}

	private void addActionsToActionsButton(List<Action> inventoryActions) {
		actionsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				for(Action inventoryDialogAction : inventoryActions) {
					popupMenu.add(new JMenuItem(inventoryDialogAction));
				}
				popupMenu.show(actionsButton, actionsButton.getWidth(), 0);
			}
		});
	}

	public InventoryDialog(InventoryDialogModel inventoryDialogModel, WorldObjectContainer inventory, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		this(inventoryDialogModel, inventory, null, imageInfoReader, inventoryActions);
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
		public ActionListener getGuiAction() {
			return new ApplyAndCloseAction(inventory);
		}

		@Override
		public boolean isPossible(InventoryItem inventoryItem) {
			return true;
		}
	}

	private void addActions(InventoryDialogAction inventoryDialogAction) {
		cancelButton.addActionListener(new CloseDialogAction());
		inventoryJList.addListSelectionListener(new InventoryListSelectionListener(inventoryDialogAction));
		noSellRadioButton.addChangeListener(new NoSellRadioButtonListener());
		priceTextField.addMouseListener(new PriceTextFieldMouseListener());
		sellRadioButton.addChangeListener(new SellRadioButtonListener());
		
		inventoryJList.setSelectedIndex(0);
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
            JScrollableToolTip tip = new JScrollableToolTip(200, 80);
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

	public void refresh(WorldObjectContainer inventory, int playerCharacterGold, List<Action> inventoryActions) {
		inventoryJList.setModel(getInventoryListModel(inventory));
		moneyValueLabel.setText(Integer.toString(playerCharacterGold));
		setInventoryActions(inventoryActions);
	}

	public InventoryItem getSelectedValue() {
		return inventoryJList.getSelectedValue();
	}
	
	private class InventoryListSelectionListener implements ListSelectionListener {

		private final InventoryDialogAction inventoryDialogAction;
		
		private InventoryListSelectionListener(InventoryDialogAction inventoryDialogAction) {
			this.inventoryDialogAction = inventoryDialogAction;
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			InventoryItem inventoryItem = inventoryJList.getSelectedValue();
			if (inventoryItem != null) {
				okButton.setEnabled(inventoryDialogAction.isPossible(inventoryItem));
				
				if (inventoryItem.isSellable()) {
					noSellRadioButton.setSelected(false);
					sellRadioButton.setSelected(true);
				} else {
					noSellRadioButton.setSelected(true);
					sellRadioButton.setSelected(false);
				}					
				priceTextField.setText(Integer.toString(inventoryItem.getPrice()));
			} else {
				noSellRadioButton.setSelected(false);
				sellRadioButton.setSelected(false);
				priceTextField.setText("0");
				okButton.setEnabled(false);
			}
		}		
	}
	
	private class PriceTextFieldMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			noSellRadioButton.setSelected(false);
			sellRadioButton.setSelected(true);
		}
	}
	
	private class NoSellRadioButtonListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (noSellRadioButton.isSelected()) {
				if (inventoryJList.getSelectedValue() != null) {
					inventoryJList.getSelectedValue().setSellable(false);
				}
			}
		}
	}
	
	private class SellRadioButtonListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (sellRadioButton.isSelected()) {
				if (inventoryJList.getSelectedValue() != null) {
					inventoryJList.getSelectedValue().setSellable(true);
				}
			}
		}
	}
}