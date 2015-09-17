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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.util.IconUtils;

public class InventoryDialog extends JDialog {

	private JList<InventoryItem> inventoryJList;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel moneyValueLabel;
	private JLabel weightLabelValue;

	private JList<InventoryItem> targetInventoryList;
	private JLabel targetMoney;
	private JLabel targetWeight;
	
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
	public InventoryDialog(InventoryDialogModel inventoryDialogModel, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		
		if (inventoryDialogAction == null) {
			inventoryDialogAction = new DefaultInventoryDialogAction(inventoryDialogModel.getPlayerCharacterInventory());
		}
		
		initializeGUI(inventoryDialogModel, inventoryDialogAction, imageInfoReader, inventoryActions);
		
		okButton.addActionListener(inventoryDialogAction.getGuiAction());
		addActions(inventoryDialogAction);
	}

	private void initializeGUI(InventoryDialogModel inventoryDialogModel, InventoryDialogAction inventoryDialogAction, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 762, 690);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		IconUtils.setIcon(this);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(new CloseDialogAction(), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(478, 595, 254, 35);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		okButton = new JButton(inventoryDialogAction.getDescription());
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		inventoryJList = createInventoryList(inventoryDialogModel.getPlayerCharacterInventory(), imageInfoReader);
		
		JScrollPane inventoryScrollPane = new JScrollPane();
		inventoryScrollPane.setViewportView(inventoryJList);
		inventoryScrollPane.setBounds(12, 82, 200, 450);
		getContentPane().add(inventoryScrollPane);
		
		final JLabel moneyLabel = new JLabel("Money:");
		moneyLabel.setBounds(12, 555, 64, 25);
		getContentPane().add(moneyLabel);
		
		moneyValueLabel = new JLabel(Integer.toString(inventoryDialogModel.getPlayerCharacterMoney()));
		moneyValueLabel.setBounds(77, 558, 50, 22);
		getContentPane().add(moneyValueLabel);
		
		JLabel lblWeight = new JLabel("Weight:");
		lblWeight.setBounds(127, 555, 64, 25);
		getContentPane().add(lblWeight);
		
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue = new JLabel(weightString);
		weightLabelValue.setBounds(203, 558, 64, 22);
		getContentPane().add(weightLabelValue);
		
		
		actionsButton = new JButton("Actions");
		actionsButton.setBounds(224, 359, 100, 25);
		getContentPane().add(actionsButton);

		JLabel lblPlayercharacter = new JLabel(new ImageIcon(inventoryDialogModel.getPlayerCharacterImage(imageInfoReader)));
		lblPlayercharacter.setBounds(12, 30, 48, 48);
		getContentPane().add(lblPlayercharacter);
		
		

		if (inventoryDialogModel.hasTarget()) {
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(532, 82, 200, 450);
			getContentPane().add(scrollPane);
			
			targetInventoryList = createInventoryList(inventoryDialogModel.getTargetInventory(), imageInfoReader);
			scrollPane.setViewportView(targetInventoryList);
			
			JLabel lblTarget = new JLabel(new ImageIcon(inventoryDialogModel.getTargetImage(imageInfoReader)));
			lblTarget.setBounds(532, 30, 48, 48);
			getContentPane().add(lblTarget);
			
			JLabel targetMoneyLabel = new JLabel("Money:");
			targetMoneyLabel.setBounds(477, 557, 64, 25);
			getContentPane().add(targetMoneyLabel);
			
			targetMoney = new JLabel(Integer.toString(inventoryDialogModel.getTargetMoney()));
			targetMoney.setBounds(542, 560, 50, 22);
			getContentPane().add(targetMoney);
			
			JLabel targetWeightLabel = new JLabel("Weight:");
			targetWeightLabel.setBounds(592, 557, 64, 25);
			getContentPane().add(targetWeightLabel);
			
			String targetWeightString = getTargetWeight(inventoryDialogModel);
			targetWeight = new JLabel(targetWeightString);
			targetWeight.setBounds(668, 560, 64, 22);
			getContentPane().add(targetWeight);
		}
		
		setInventoryActions(inventoryActions);
		addPopupMenuToInventoryList();
		
		if (inventoryJList.getModel().getSize() == 0) {
			okButton.setEnabled(false);
		}
	}
	
	private void addPopupMenuToInventoryList() {
		inventoryJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
		        if (SwingUtilities.isRightMouseButton(e)) {
		        	int row = inventoryJList.locationToIndex(e.getPoint());
		        	inventoryJList.setSelectedIndex(row);
			        InventoryItem inventoryItem = inventoryJList.getSelectedValue();
			        
			        JPopupMenu popupMenu = new JPopupMenu();
			        popupMenu.add(new JCheckBoxMenuItem(new SellableAction(inventoryItem)));
			        popupMenu.show(inventoryJList, e.getX(), e.getY());
			    }
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

	public InventoryDialog(InventoryDialogModel inventoryDialogModel, ImageInfoReader imageInfoReader, List<Action> inventoryActions) {
		this(inventoryDialogModel, null, imageInfoReader, inventoryActions);
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
            JScrollableToolTip tip = new JScrollableToolTip(250, 100);
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

	public void refresh(InventoryDialogModel inventoryDialogModel, List<Action> inventoryActions) {
		inventoryJList.setModel(getInventoryListModel(inventoryDialogModel.getPlayerCharacterInventory()));
		moneyValueLabel.setText(Integer.toString(inventoryDialogModel.getPlayerCharacterMoney()));
		String weightString = getPlayerCharacterWeight(inventoryDialogModel);
		weightLabelValue.setText(weightString);
		
		if (inventoryDialogModel.hasTarget()) {
			targetInventoryList.setModel(getInventoryListModel(inventoryDialogModel.getTargetInventory()));
			targetMoney.setText(Integer.toString(inventoryDialogModel.getTargetMoney()));
			String targetWeightString = getTargetWeight(inventoryDialogModel);
			targetWeight.setText(targetWeightString);
		}
		
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
			} else {
				okButton.setEnabled(false);
			}
		}		
	}
}