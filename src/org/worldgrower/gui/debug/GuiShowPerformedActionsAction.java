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
package org.worldgrower.gui.debug;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.history.HistoryItem;

public class GuiShowPerformedActionsAction extends AbstractAction {
	private World world;
	
	public GuiShowPerformedActionsAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setAutoCreateRowSorter(true);
		table.setBounds(50, 50, 400, 700);
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 500, 800);
		frame.setVisible(true);
	}
	
	private static class WorldModel extends AbstractTableModel {

		private Map<ManagedOperation, Integer> actionStatistics = new HashMap<>();
		private List<ManagedOperation> actions = new ArrayList<>();
		
		public WorldModel(World world) {
			super();
			for(int i=0; i<world.getHistory().size(); i++) {
				HistoryItem historyItem = world.getHistory().getHistoryItem(i);
				ManagedOperation action = historyItem.getManagedOperation();
				Integer count = actionStatistics.get(action);
				if (count == null) {
					count = 1;
					actions.add(action);
				} else {
					count = count + 1;
				}
				actionStatistics.put(action, count);
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}
		
		

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 1) {
				return Integer.class;
			} else {
				return super.getColumnClass(columnIndex);
			}
		}

		@Override
		public int getRowCount() {
			return actionStatistics.size();
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "Count";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return actions.get(rowIndex).getClass().getName();
			} else if (columnIndex == 1) {
				return actionStatistics.get(actions.get(rowIndex));
			} else {
				return null;
			}
		}
	}
}