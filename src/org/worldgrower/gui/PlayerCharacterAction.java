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
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;

public class PlayerCharacterAction extends AbstractAction {
	private WorldObject playerCharacter;
	private World world;
	private WorldPanel container;
	private DungeonMaster dungeonMaster;
	private ManagedOperation action;
	private WorldObject target;
	private final ImageInfoReader imageInfoReader;
	private final SoundIdReader soundIdReader;
	
	public PlayerCharacterAction(WorldObject playerCharacter, World world, WorldPanel container, DungeonMaster dungeonMaster, ManagedOperation action, WorldObject target, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.dungeonMaster = dungeonMaster;
		this.action = action;
		this.target = target;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, action, Args.EMPTY, world, dungeonMaster, target, container, imageInfoReader, soundIdReader);
	}
}