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
import javax.swing.Action;
import javax.swing.JComponent;

import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageInfoReader;

public class ChooseWorldObjectAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ChooseWorldObjectDialog dialog;
	private ImageInfoReader imageInfoReader;
	private World world;
	private JComponent parent;
	private DungeonMaster dungeonMaster;
	private Action guiAction;
	
	public ChooseWorldObjectAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, World world, JComponent parent, DungeonMaster dungeonMaster, Action guiAction) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.guiAction = guiAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<WorldObject> disguiseWorldObjects = world.findWorldObjects(w -> w.getProperty(Constants.WIDTH) == 1 && w.getProperty(Constants.HEIGHT) == 1 && w.getProperty(Constants.ID) != playerCharacter.getProperty(Constants.ID));
		
		dialog = new ChooseWorldObjectDialog(playerCharacter, imageInfoReader, disguiseWorldObjects, parent, world, dungeonMaster, guiAction);
		dialog.showMe();
	}
}