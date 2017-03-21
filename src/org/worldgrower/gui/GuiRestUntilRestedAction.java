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
import javax.swing.JFrame;

import org.worldgrower.Args;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.EnergyPropertyUtils;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;

public class GuiRestUntilRestedAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private WorldObject target;
	private JFrame parentFrame;
	
	public GuiRestUntilRestedAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, WorldObject target, JFrame parentFrame) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.target = target;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int turns = getTurns();
		Game.executeMultipleTurns(playerCharacter, Actions.SLEEP_ACTION, Args.EMPTY, world, dungeonMaster, target, parent, turns, imageInfoReader, soundIdReader);
	}

	public int getTurns() {
		return EnergyPropertyUtils.calculateTurnsUntilRested(playerCharacter, target);
	}
}