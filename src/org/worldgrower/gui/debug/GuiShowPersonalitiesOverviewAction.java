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
import org.worldgrower.personality.PersonalityTrait;

public class GuiShowPersonalitiesOverviewAction extends AbstractAction {
	private World world;
	
	public GuiShowPersonalitiesOverviewAction(World world) {
		super();
		this.world = world;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel(world));
		table.setBounds(50, 50, 700, 700);
		for(int i=1; i<table.getColumnCount() -1; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(10);
		}		
		table.getColumnModel().getColumn(table.getColumnCount() -1).setPreferredWidth(600);
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
			return 1 + PersonalityTrait.ALL_TRAITS.size() + 1;
		}

		@Override
		public int getRowCount() {
			return getNPCs().size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIsTrait(columnIndex)) {
				return getPersonalityTrait(columnIndex).getDescription();
			} else if (columnIsAngryReason(columnIndex)) {
				return "Angry reasons";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			WorldObject npc = getNPCs().get(rowIndex);
			if (columnIndex == 0) {
				return npc.getProperty(Constants.NAME);
			} else if (columnIsTrait(columnIndex)) {
				PersonalityTrait personalityTrait = getPersonalityTrait(columnIndex);
				return npc.getProperty(Constants.PERSONALITY).getValue(personalityTrait);
			} else if (columnIsAngryReason(columnIndex)) {
				return npc.getProperty(Constants.BACKGROUND).toStringAngryReasons();
			} else {
				return null;
			}
		}

		private PersonalityTrait getPersonalityTrait(int columnIndex) {
			return PersonalityTrait.ALL_TRAITS.get(columnIndex - 1);
		}

		private boolean columnIsAngryReason(int columnIndex) {
			return columnIndex == PersonalityTrait.ALL_TRAITS.size() + 1;
		}

		private boolean columnIsTrait(int columnIndex) {
			return columnIndex <= PersonalityTrait.ALL_TRAITS.size();
		}
	}
}