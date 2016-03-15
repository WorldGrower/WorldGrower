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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.start.KeyBindings;
import org.worldgrower.gui.util.ShowTextDialog;

public class GuiGameOverAction implements ManagedOperationListener {

	private WorldObject playerCharacter;
	private World world;
	private WorldPanel container;
	private ImageInfoReader imageInfoReader;
	private KeyBindings keyBindings;
	
	public GuiGameOverAction(WorldObject playerCharacter, World world, WorldPanel container, ImageInfoReader imageInfoReader, KeyBindings keyBindings) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
		this.keyBindings = keyBindings;
		
		world.addListener(this);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (playerCharacter.getProperty(Constants.HIT_POINTS) <= 0) {
			String text = "Your hit points are reduced to zero, the game is over";			
			new ShowTextDialog(text).showMe();
			Game.closeMainPanel();
			new ShowStartScreenAction(container, imageInfoReader, keyBindings, world).actionPerformed(null);
		}
	}
}