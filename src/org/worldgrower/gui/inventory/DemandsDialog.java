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

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import org.worldgrower.Constants;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.ImageTableRenderer;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JTableFactory;
import org.worldgrower.gui.util.JTextFieldFactory;

public final class DemandsDialog extends AbstractDialog {
	private final PropertyCountMap<ManagedProperty<?>> demands;
	
	public DemandsDialog(PropertyCountMap<ManagedProperty<?>> demands, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super(400, 800);
		this.demands = demands;
		
		initializeGUI(imageInfoReader, soundIdReader);
	}

	public void initializeGUI(ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		DemandsModel worldModel = new DemandsModel(demands);
		JTable table = JTableFactory.createJTable(worldModel);
		table.setDefaultRenderer(ImageIds.class, new ImageTableRenderer(imageInfoReader));
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(50);
		table.getRowSorter().toggleSortOrder(1);
		
		TableCellEditor fce = new PositiveIntegerCellEditor(JTextFieldFactory.createJTextField());
        table.setDefaultEditor(Integer.class, fce);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(15, 15, 368, 700);
		addComponent(scrollPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		buttonPane.setBounds(0, 720, 378, 75);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addComponent(buttonPane);
		
		JButton okButton = JButtonFactory.createButton("OK", soundIdReader);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		addActionHandlers(okButton, worldModel, this, demands);
		getRootPane().setDefaultButton(okButton);
		
		SwingUtils.makeTransparant(table, scrollPane);
	}
	
	private static class PositiveIntegerCellEditor extends DefaultCellEditor {

	    private static final Border red = new LineBorder(Color.red);
	    private static final Border black = new LineBorder(Color.black);
	    private JTextField textField;

	    public PositiveIntegerCellEditor(JTextField textField) {
	        super(textField);
	        this.textField = textField;
	        this.textField.setHorizontalAlignment(JTextField.RIGHT);
	    }

	    @Override
	    public boolean stopCellEditing() {
	        try {
	            int v = Integer.valueOf(textField.getText());
	            if (v < 0) {
	                throw new NumberFormatException();
	            }
	        } catch (NumberFormatException e) {
	            textField.setBorder(red);
	            return false;
	        }
	        return super.stopCellEditing();
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table,
	        Object value, boolean isSelected, int row, int column) {
	        textField.setBorder(black);
	        return super.getTableCellEditorComponent(
	            table, value, isSelected, row, column);
	    }
	}
	
	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, DemandsModel model, JDialog dialog, PropertyCountMap<ManagedProperty<?>> demands) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.apply(demands);
				dialog.dispose();
			}
		});
	}

	
	
	private static class DemandsModel extends AbstractTableModel {
		private List<DemandItem> demandItems = new ArrayList<>();
		
		public DemandsModel(PropertyCountMap<ManagedProperty<?>> demands) {
			super();
			for(ManagedProperty<?> property : Constants.getPossibleDemandProperties()) {
				int quantityDemanded = demands.count(property);
				demandItems.add(new DemandItem(property, quantityDemanded));
			}
		}

		public void apply(PropertyCountMap<ManagedProperty<?>> demands) {
			for(DemandItem demandItem : demandItems) {
				if (demandItem.getQuantityDemanded() > 0) {
					demands.add(demandItem.getProperty(), demandItem.getQuantityDemanded());
				} else {
					demands.remove(demandItem.getProperty());
				}
			}
			
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public int getRowCount() {
			return demandItems.size();
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return ImageIds.class;
			} else if (column == 1) {
				return String.class;
			} else {
				return Integer.class;
			}
		}
		
		@Override		
		public boolean isCellEditable(int row, int column) {
			if (column == 0 || column == 1) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			demandItems.get(row).setQuantityDemanded(Integer.parseInt((String)value));
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Image";
			} else if (columnIndex == 1) {
				return "Item";
			} else if (columnIndex == 2) {
				return "Quantity";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return Item.getItemFor(demandItems.get(rowIndex).getProperty()).getImageId();
			} else if (columnIndex == 1) {
				return demandItems.get(rowIndex).getProperty().getName();
			} else if (columnIndex == 2) {
				return demandItems.get(rowIndex).getQuantityDemanded();
			} else {
				return null;
			}
		}
	}
	
	private static class DemandItem {
		private ManagedProperty<?> property;
		private int quantityDemanded;
		
		public DemandItem(ManagedProperty<?> property, int quantityDemanded) {
			super();
			this.property = property;
			this.quantityDemanded = quantityDemanded;
		}
		public ManagedProperty<?> getProperty() {
			return property;
		}
		public int getQuantityDemanded() {
			return quantityDemanded;
		}
		public void setQuantityDemanded(int quantityDemanded) {
			this.quantityDemanded = quantityDemanded;
		}
	}
}