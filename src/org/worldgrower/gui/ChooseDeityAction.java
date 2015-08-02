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
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.worldgrower.DungeonMaster;
import org.worldgrower.Main;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;

public class ChooseDeityAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	
	public ChooseDeityAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, WorldPanel parent, DungeonMaster dungeonMaster) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] deityNames = Deity.getAllDeityNames().toArray(new String[0]);
		String deityName = (String) JOptionPane.showInputDialog(parent, "Choose Deity", "Choose Deity", JOptionPane.QUESTION_MESSAGE, null, deityNames, deityNames[0]);
		if (deityName != null) {
			int indexOfDeity = Arrays.asList(deityNames).indexOf(deityName);
			
			Main.executeAction(playerCharacter, Actions.CHOOSE_DEITY_ACTION, new int[] { indexOfDeity }, world, dungeonMaster, playerCharacter, parent);
		}
	}
}