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

import javax.swing.AbstractAction;

import org.worldgrower.Args;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.TextInputDialog;
import org.worldgrower.util.NumberUtils;

public class RestAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	
	public RestAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String turnsString = new TextInputDialog("Rest how many turns?", true, soundIdReader).showMe();
		if ((turnsString != null) && (NumberUtils.isNumeric(turnsString) && turnsString.length() > 0)) {
			int turns = Integer.parseInt(turnsString);
			
			Game.executeMultipleTurns(playerCharacter, Actions.REST_ACTION, Args.EMPTY, world, dungeonMaster, playerCharacter, parent, turns, soundIdReader);
		}
	}
}