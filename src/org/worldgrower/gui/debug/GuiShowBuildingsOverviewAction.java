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
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class GuiShowBuildingsOverviewAction extends AbstractAction {
	private World world;
	
	public GuiShowBuildingsOverviewAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 400, 700);
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 500, 800);
		frame.setVisible(true);
	}
	
	private static class BuildingRow {
		private final String name;
		private final Integer ownerId;
		
		public BuildingRow(String name, Integer ownerId) {
			this.name = name;
			this.ownerId = ownerId;
		}

		public String getName() {
			return name;
		}

		public Integer getOwnerId() {
			return ownerId;
		}
	}
	
	private static class WorldModel extends AbstractTableModel {

		private List<BuildingRow> buildingRows = new ArrayList<>();
		
		public WorldModel(World world) {
			super();
			for(WorldObject worldObject : world.getWorldObjects()) {
				if (worldObject.hasProperty(Constants.BUILDING_TYPE)) {
					String name = worldObject.getProperty(Constants.NAME);
					List<WorldObject> owners = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.BUILDINGS) && w.getProperty(Constants.BUILDINGS).contains(worldObject));
					final Integer ownerId;
					if (owners.size() > 0) {
						ownerId = owners.get(0).getProperty(Constants.ID);
					} else {
						ownerId = null;
					}
					buildingRows.add(new BuildingRow(name, ownerId));
				}
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return buildingRows.size();
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "OwnerId";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return buildingRows.get(rowIndex).getName();
			} else if (columnIndex == 1) {
				return buildingRows.get(rowIndex).getOwnerId();
			} else {
				return null;
			}
		}
	}
}