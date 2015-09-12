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

import javax.swing.JComponent;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.CreatureTypeChangedListener;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.util.MessageDialogUtils;

public class GuiShowEventHappenedAction implements CreatureTypeChangedListener {

	private WorldObject playerCharacter;
	private World world;
	private JComponent container;
	private ImageInfoReader imageInfoReader;
	
	public GuiShowEventHappenedAction(WorldObject playerCharacter, World world, JComponent container, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
	}

	@Override
	public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description) {
		if (worldObject == playerCharacter) {
			MessageDialogUtils.showMessage(description, "Changing creature type ", worldObject, container, imageInfoReader);
		}
		
	}
}