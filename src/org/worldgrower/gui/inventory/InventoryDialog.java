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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
	private JTextArea detailsTextArea;
	private JLabel moneyValueLabel;
	
	private JRadioButton noSellRadioButton;
	private JRadioButton sellRadioButton;
	private JTextField priceTextField;
	
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
	public InventoryDialog(int money, WorldObjectContainer inventory, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader) {
		
		if (inventoryDialogAction == null) {
			inventoryDialogAction = new DefaultInventoryDialogAction(inventory);
		}
		
		initializeGUI(money, inventory, inventoryDialogAction, imageInfoReader);
		
		okButton.addActionListener(inventoryDialogAction.getGuiAction());
		addActions(inventoryDialogAction);
	}

	private void initializeGUI(int money, WorldObjectContainer inventory, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 550, 416);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(235, 321, 285, 35);
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
		inventoryScrollPane.setBounds(12, 27, 200, 291);
		getContentPane().add(inventoryScrollPane);
		
		final JLabel moneyLabel = new JLabel("Money:");
		moneyLabel.setBounds(12, 331, 53, 25);
		getContentPane().add(moneyLabel);
		
		moneyValueLabel = new JLabel(Integer.toString(money));
		moneyValueLabel.setBounds(77, 334, 100, 22);
		getContentPane().add(moneyValueLabel);
		
		detailsTextArea = new JTextArea();
		detailsTextArea.setBounds(224, 25, 200, 148);
		detailsTextArea.setEditable(false);
		getContentPane().add(detailsTextArea);
		
		noSellRadioButton = new JRadioButton("Don't sell");
		noSellRadioButton.setBounds(224, 200, 100, 40);
		getContentPane().add(noSellRadioButton);
		
		sellRadioButton = new JRadioButton("Sell at price: ");
		sellRadioButton.setBounds(224, 240, 142, 40);
		getContentPane().add(sellRadioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(noSellRadioButton);
		group.add(sellRadioButton);
		
		priceTextField = new JTextField("0");
		priceTextField.setEditable(false);
		priceTextField.setBounds(374, 240, 50, 40);
		getContentPane().add(priceTextField);
		
		if (inventoryJList.getModel().getSize() == 0) {
			noSellRadioButton.setEnabled(false);
			sellRadioButton.setEnabled(false);
			priceTextField.setEnabled(false);
			okButton.setEnabled(false);
		}
	}
	
	public InventoryDialog(int money, WorldObjectContainer inventory, ImageInfoReader imageInfoReader) {
		this(money, inventory, null, imageInfoReader);
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
		JList<InventoryItem> inventoryList = new JList<>(listModel);
		inventoryList.setCellRenderer(new InventoryListCellRenderer(imageInfoReader));
		return inventoryList;
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

	public void refresh(WorldObjectContainer inventory, int playerCharacterGold) {
		inventoryJList.setModel(getInventoryListModel(inventory));
		moneyValueLabel.setText(Integer.toString(playerCharacterGold));
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
				detailsTextArea.setText(inventoryItem.getLongDescription());
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