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
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class GuiShowReasonsOverviewAction extends AbstractAction {
	private World world;
	
	public GuiShowReasonsOverviewAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 700, 700);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 800, 800);
		frame.setVisible(true);
	}
	
	private List<WorldObject> getNPCs() {
		return world.findWorldObjects(w -> w.isControlledByAI() && w.hasIntelligence() && !w.getProperty(Constants.CREATURE_TYPE).isCattle());
	}
	
	private class WorldModel extends AbstractTableModel {

		private World world;
		
		public WorldModel(World world) {
			super();
			this.world = world;
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public int getRowCount() {
			return getNPCs().size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "Profession";
			} else if (columnIndex == 2) {
				return "Profession Reason";
			} else if (columnIndex == 3) {
				return "Deity";
			} else if (columnIndex == 4) {
				return "Deity Reason";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			WorldObject npc = getNPCs().get(rowIndex);
			if (columnIndex == 0) {
				return npc.getProperty(Constants.NAME);
			} else if (columnIndex == 1) {
				return npc.getProperty(Constants.PROFESSION);
			} else if (columnIndex == 2) {
				return npc.getProperty(Constants.REASONS).getReason(Constants.PROFESSION);
			} else if (columnIndex == 3) {
				return npc.getProperty(Constants.DEITY);
			} else if (columnIndex == 4) {
				return npc.getProperty(Constants.REASONS).getReason(Constants.DEITY);
			} else {
				return null;
			}
		}
	}
}