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

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;

public class GuiMoveAction extends AbstractAction {
	private int[] args;
	private WorldObject playerCharacter;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldPanel container;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	
	public GuiMoveAction(int[] args, WorldObject playerCharacter, World world, DungeonMaster dungeonMaster, WorldPanel container, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super();
		this.args = args;
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (Game.canActionExecute(playerCharacter, playerCharacter.getOperation(Actions.MOVE_ACTION), args, world, playerCharacter)) {
			Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, playerCharacter.getOperation(Actions.MOVE_ACTION), args, world, dungeonMaster, playerCharacter, container, imageInfoReader, soundIdReader);
		}		
	}
}