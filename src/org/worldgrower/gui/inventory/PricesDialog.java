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

import org.worldgrower.attribute.Prices;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.AbstractDialog;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.util.JButtonFactory;
import org.worldgrower.gui.util.JTableFactory;

public final class PricesDialog extends AbstractDialog {
	private final Prices pricesOnPlayer;
	
	public PricesDialog(Prices pricesOnPlayer, SoundIdReader soundIdReader) {
		super(400, 800);
		this.pricesOnPlayer = pricesOnPlayer;
		
		initializeGUI(soundIdReader);
	}

	public void initializeGUI(SoundIdReader soundIdReader) {
		PricesModel worldModel = new PricesModel(pricesOnPlayer);
		JTable table = JTableFactory.createJTable(worldModel);
		table.setAutoCreateRowSorter(true);
		table.getRowSorter().toggleSortOrder(0);
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
		addActionHandlers(okButton, worldModel, this, pricesOnPlayer);
		getRootPane().setDefaultButton(okButton);
		
		SwingUtils.makeTransparant(table, scrollPane);
	}
	
	public void showMe() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addActionHandlers(JButton okButton, PricesModel model, JDialog dialog, Prices pricesOnPlayer) {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] args = model.getArgs();
				Prices prices = pricesOnPlayer;
				for(int i=0; i<args.length; i++) {
					Item item = Item.value(i);
					prices.setPrice(item, args[i]);
				}
				//Game.executeAction(playerCharacter, Actions.SET_PRICES_ACTION, args, world, dungeonMaster, playerCharacter, parent);
				dialog.dispose();
			}
		});
	}

	private static class PricesModel extends AbstractTableModel {
		private List<ItemPrice> prices = new ArrayList<>();
		
		public PricesModel(Prices pricesOnPlayer) {
			super();
			for(Item item : Item.values()) {
				int price = pricesOnPlayer.getPrice(item);
				prices.add(new ItemPrice(item, price));
			}
		}

		public int[] getArgs() {
			int[] args = new int[prices.size()];
			for(int i=0; i<args.length; i++) {
				args[i] = prices.get(i).getPrice();
			}
			return args;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return prices.size();
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			if (column == 0) {
				return String.class;
			} else {
				return Integer.class;
			}
		}
		
		@Override		
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			prices.get(row).setPrice((Integer)value);
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Item";
			} else if (columnIndex == 1) {
				return "Price";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return prices.get(rowIndex).getItem().getDescription();
			} else if (columnIndex == 1) {
				return prices.get(rowIndex).getPrice();
			} else {
				return null;
			}
		}
	}
	
	private static class ItemPrice {
		private Item item;
		private int price;
		
		public ItemPrice(Item item, int price) {
			super();
			this.item = item;
			this.price = price;
		}
		public Item getItem() {
			return item;
		}
		public int getPrice() {
			return price;
		}
		public void setPrice(int price) {
			this.price = price;
		}
	}
}