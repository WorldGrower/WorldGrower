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
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.magic.IllusionSpell;
import org.worldgrower.gui.ActionContainingArgs;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.music.SoundIdReader;

public class ChooseWorldObjectAction extends AbstractAction {
	private IllusionSpell illusionSpell;
	private WorldObject playerCharacter;
	private ChooseWorldObjectDialog dialog;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private JComponent parent;
	private DungeonMaster dungeonMaster;
	private ActionContainingArgs guiAction;
	private JFrame parentFrame;
	
	public ChooseWorldObjectAction(IllusionSpell illusionSpell, WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, JComponent parent, DungeonMaster dungeonMaster, ActionContainingArgs guiAction, JFrame parentFrame) {
		super();
		this.illusionSpell = illusionSpell;
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.guiAction = guiAction;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<WorldObject> disguiseWorldObjects = illusionSpell.getIllusionSources(playerCharacter, world);
		
		dialog = new ChooseWorldObjectDialog(playerCharacter, imageInfoReader, soundIdReader, disguiseWorldObjects, parent, world, dungeonMaster, guiAction, parentFrame);
		dialog.showMe();
	}
}