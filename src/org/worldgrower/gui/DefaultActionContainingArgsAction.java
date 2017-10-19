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

public class DefaultActionContainingArgsAction extends AbstractAction implements ActionContainingArgs {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private WorldPanel worldPanel;
	private ManagedOperation action;
	private World world;
	private DungeonMaster dungeonMaster;
	private WorldObject target;
	private SoundIdReader soundIdReader;
	private int[] args = Args.EMPTY;
	
	public DefaultActionContainingArgsAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, WorldPanel worldPanel, ManagedOperation action, World world, DungeonMaster dungeonMaster, WorldObject target, SoundIdReader soundIdReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.worldPanel = worldPanel;
		this.action = action;
		this.world = world;
		this.dungeonMaster = dungeonMaster;
		this.target = target;
		this.soundIdReader = soundIdReader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.executeActionAndMoveIntelligentWorldObjects(playerCharacter, action, args, world, dungeonMaster, target, worldPanel, imageInfoReader, soundIdReader);
	}

	@Override
	public void setArgs(int[] args) {
		this.args = args;
	}
}