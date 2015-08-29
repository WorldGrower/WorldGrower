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
package org.worldgrower.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;

public class GuiShowLegalActionsAction extends AbstractAction {
	private World world;
	
	public GuiShowLegalActionsAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 200, 700);
		frame.add(new JScrollPane(table));
		
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
			return getLegalActions().size();
		}
		
		private Map<ManagedOperation, Boolean> getLegalActions() {
			WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
			return villagersOrganization.getProperty(Constants.LEGAL_ACTIONS);
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Action Name";
			} else if (columnIndex == 1) {
				return "Is Legal";
			} else {
				return null;
			}
		}

		private static class ActionComparator implements Comparator<ManagedOperation> {

			@Override
			public int compare(ManagedOperation o1, ManagedOperation o2) {
				return o1.getSimpleDescription().compareTo(o2.getSimpleDescription());
			}
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Map<ManagedOperation, Boolean> legalActions = getLegalActions();
			List<ManagedOperation> actions = new ArrayList<>(legalActions.keySet());
			Collections.sort(actions, new ActionComparator());
			if (columnIndex == 0) {
				return actions.get(rowIndex).getSimpleDescription();
			} else if (columnIndex == 1) {
				return legalActions.get(actions.get(rowIndex));
			} else {
				return null;
			}
		}
	}
}