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
package org.worldgrower.gui.chooseworldobject;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.DisguiseTargetFactory;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.music.SoundIdReader;

public class GuiDisguiseAction extends AbstractAction {

	private WorldObject playerCharacter;
	private DisguiseDialog dialog;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private DisguiseTargetFactory disguiseAction;
	
	public GuiDisguiseAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, DisguiseTargetFactory disguiseAction) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.disguiseAction = disguiseAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<WorldObject> disguiseWorldObjects = disguiseAction.getDisguiseTargets(playerCharacter, world);
		
		dialog = new DisguiseDialog(playerCharacter, imageInfoReader, soundIdReader, disguiseWorldObjects, parent, world, dungeonMaster, disguiseAction);
		dialog.showMe();
	}
}