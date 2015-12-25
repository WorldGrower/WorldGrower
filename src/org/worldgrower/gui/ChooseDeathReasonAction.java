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

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.DeathReasonPropertyUtils;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.ListInputDialog;

public class ChooseDeathReasonAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private WorldObject target;
	
	public ChooseDeathReasonAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, WorldObject target) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.target = target;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] deathReasons = DeathReasonPropertyUtils.getAllDeathReasons().toArray(new String[0]);
		String deathReason = new ListInputDialog("Choose Death Reason", deathReasons).showMe();
		if (deathReason != null) {
			int indexOfDeathReason = Arrays.asList(deathReasons).indexOf(deathReason);
			
			Game.executeAction(playerCharacter, Actions.OBFUSCATE_DEATH_REASON_ACTION, new int[] { indexOfDeathReason }, world, dungeonMaster, target, parent);
		}
	}
}