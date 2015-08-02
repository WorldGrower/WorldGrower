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
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.actions.Actions;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class GuiShowEconomicOverviewAction extends AbstractAction {
	private World world;
	
	public GuiShowEconomicOverviewAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 200, 700);
		frame.add(new JScrollPane(table));
		
		Timer timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				table.repaint();
			}
			
		});
		
		timer.start();
		
		frame.setBounds(100,  100, 300, 800);
		frame.setVisible(true);
	}
	
	private static class WorldModel extends AbstractTableModel {

		private World world;
		
		public WorldModel(World world) {
			super();
			this.world = world;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return 5;
		}

		private List<HistoryItem> getOperations(ManagedOperation managedOperation) {
			return world.getHistory().findHistoryItems(managedOperation);
		}
		
		private List<HistoryItem> getOperationsByNonProfessionals(ManagedOperation managedOperation, Profession profession) {
			List<HistoryItem> historyItems = getOperations(managedOperation);
			return historyItems.stream().filter(h -> h.getOperationInfo().getPerformer().getProperty(Constants.PROFESSION) == null || h.getOperationInfo().getPerformer().getProperty(Constants.PROFESSION) != profession).collect(Collectors.toList());
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
				if (rowIndex == 0) {
					return "CutWoodAction";
				} else if (rowIndex == 1) {
					return "CutWoodAction by non-professionals";
				} else if (rowIndex == 2) {
					return "HarvestFood";
				} else if (rowIndex == 3) {
					return "HarvestFood by non-professionals";
				} else if (rowIndex == 4) {
					return "EatFood";
				} else {
					return null;
				}
			} else if (columnIndex == 1) {
				if (rowIndex == 0) {
					return getOperations(Actions.CUT_WOOD_ACTION).size();
				} else if (rowIndex == 1) {
					return getOperationsByNonProfessionals(Actions.CUT_WOOD_ACTION, Professions.LUMBERJACK_PROFESSION).size();
				} else if (rowIndex == 2) {
					return getOperations(Actions.HARVEST_FOOD_ACTION).size();
				} else if (rowIndex == 3) {
					return getOperationsByNonProfessionals(Actions.HARVEST_FOOD_ACTION, Professions.FARMER_PROFESSION).size();
				} else if (rowIndex == 4) {
					return getOperations(Actions.EAT_ACTION).size();
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
}