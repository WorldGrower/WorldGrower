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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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

	private static final ManagedProperty<?>[] PROPERTIES = new ManagedProperty<?>[] { 
		Constants.FOOD, 
		Constants.WATER,
		Constants.WOOD,
		Constants.STONE,
		Constants.ORE,
		Constants.COTTON,
		Constants.ALCOHOL_LEVEL,
		Constants.GOLD,
		Constants.GRAPE,
		Constants.OIL,
		Constants.POISON_DAMAGE,
		Constants.SOUL_GEM
		};
	
	private static class DemandsModel extends AbstractTableModel {
		private List<DemandItem> demandItems = new ArrayList<>();
		
		public DemandsModel(PropertyCountMap<ManagedProperty<?>> demands) {
			super();
			for(ManagedProperty<?> property : PROPERTIES) {
				boolean isDemanded = demands.count(property) > 0;
				demandItems.add(new DemandItem(property, isDemanded));
			}
		}

		public void apply(PropertyCountMap<ManagedProperty<?>> demands) {
			for(DemandItem demandItem : demandItems) {
				if (demandItem.isDemanded()) {
					demands.add(demandItem.getProperty(), 5);
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
				return Boolean.class;
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
			demandItems.get(row).setDemanded((Boolean)value);
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Image";
			} else if (columnIndex == 1) {
				return "Item";
			} else if (columnIndex == 2) {
				return "Is Demanded";
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
				return demandItems.get(rowIndex).isDemanded();
			} else {
				return null;
			}
		}
	}
	
	private static class DemandItem {
		private ManagedProperty<?> property;
		private boolean isDemanded;
		
		public DemandItem(ManagedProperty<?> property, boolean isDemanded) {
			super();
			this.property = property;
			this.isDemanded = isDemanded;
		}
		public ManagedProperty<?> getProperty() {
			return property;
		}
		public boolean isDemanded() {
			return isDemanded;
		}
		public void setDemanded(boolean isDemanded) {
			this.isDemanded = isDemanded;
		}
	}
}