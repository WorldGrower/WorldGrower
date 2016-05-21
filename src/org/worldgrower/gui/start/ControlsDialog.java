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
package org.worldgrower.gui.start;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JComboBoxFactory;
import org.worldgrower.gui.util.JRadioButtonFactory;
import org.worldgrower.gui.util.JTableFactory;

public class ControlsDialog extends AbstractDialog {

	public ControlsDialog(KeyBindings keyBindings) {
		super(400, 800);
		
		addKeyBindingsTable(keyBindings);
		addMouseControlPanel(keyBindings);
		addButtonPane();
	}

	private void addKeyBindingsTable(KeyBindings keyBindings) {
		JTable table = JTableFactory.createJTable(new ControlsTableModel(keyBindings));
		JComboBox<Character> comboBox = JComboBoxFactory.createJComboBox(new Character[]{'A', 'B', 'C'});
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
		
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() > -1) {
                	comboBox.setModel(new DefaultComboBoxModel<>(keyBindings.getPossibleValues(table.getSelectedRow()).toArray(new Character[0])));
                	comboBox.setSelectedItem(table.getValueAt(table.getSelectedRow(), 1));
                }
            }
        });
        
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 15, 368, 600);
		addComponent(scrollPane);
		
		SwingUtils.makeTransparant(table, scrollPane);
	}

	private void addMouseControlPanel(KeyBindings keyBindings) {
		JPanel mouseControlPanel = new JPanel();
		
		mouseControlPanel.setOpaque(false);
		mouseControlPanel.setBounds(0, 620, 378, 75);
		mouseControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JRadioButton defaultMouseControl = JRadioButtonFactory.createJRadioButton("left-click: center map, right-click: show possible actions");
		defaultMouseControl.setSelected(keyBindings.leftMouseClickCentersMap());
		defaultMouseControl.setOpaque(false);
		mouseControlPanel.add(defaultMouseControl);
		
		JRadioButton alternateMouseControl = JRadioButtonFactory.createJRadioButton("right-click: center map, left-click: show possible actions");
		alternateMouseControl.setSelected(!keyBindings.leftMouseClickCentersMap());
		alternateMouseControl.setOpaque(false);
		mouseControlPanel.add(alternateMouseControl);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(defaultMouseControl);
		buttonGroup.add(alternateMouseControl);
		
		addComponent(mouseControlPanel);
		
		defaultMouseControl.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				keyBindings.setLeftMouseClickCentersMap(defaultMouseControl.isSelected());
			}
		});
	}

	private void addButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 720, 378, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, this);
		getRootPane().setDefaultButton(okButton);
	}
	
	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, JDialog dialog) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
			}
		});
	}
	
	private final class CloseDialogAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ControlsDialog.this.dispose();
		}
	}
	
	private static class ControlsTableModel extends AbstractTableModel {

		private final KeyBindings keyBindings;
		
		public ControlsTableModel(KeyBindings keyBindings) {
			super();
			this.keyBindings = keyBindings;
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "Description";
			} else if (column == 1) {
				return "Value";
			} else {
				return null;
			}
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return keyBindings.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return keyBindings.getDescription(row);
			} else if (column == 1) {
				return keyBindings.getValue(row);
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			keyBindings.setValue(row, (Character) value);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}
	}
}
