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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.util.MessageDialogUtils;

public class GuiShowReadAction implements ManagedOperationListener {

	private WorldObject playerCharacter;
	private World world;
	private WorldPanel container;
	private ImageInfoReader imageInfoReader;
	
	public GuiShowReadAction(WorldObject playerCharacter, World world, WorldPanel container, ImageInfoReader imageInfoReader) {
		super();
		this.playerCharacter = playerCharacter;
		this.world = world;
		this.container = container;
		this.imageInfoReader = imageInfoReader;
		
		world.addListener(this);
	}

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if ((performer == playerCharacter) && (managedOperation == Actions.READ_ACTION)) {
			String text = (String) value;			
			String targetName = target.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(text, "Reading " + targetName, target, container, imageInfoReader);
		} else if ((performer == playerCharacter) && (managedOperation == Actions.DETERMINE_DEATH_REASON_ACTION)) {
			String text = (String) value;			
			String targetName = target.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(text, "Determining cause of death for " + targetName, target, container, imageInfoReader);
		} else if ((performer == playerCharacter) && (managedOperation == Actions.DETECT_POISON_AND_DISEASE_ACTION)) {
			String text = (String) value;			
			String targetName = target.getProperty(Constants.NAME);
			MessageDialogUtils.showMessage(text, "Detect poison and disease on " + targetName, target, container, imageInfoReader);
		}
	}
	
	
}