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
package org.worldgrower;

import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.WorldPanel;
import org.worldgrower.gui.util.MessageDialogUtils;

public class TutorialAdditionalManagedOperationListenerFactory implements AdditionalManagedOperationListenerFactory {

	@Override
	public List<ManagedOperationListener> create(World world, WorldPanel container, ImageInfoReader imageInfoReader) {
		return Arrays.asList(new ManagedOperationListenerImpl(world, container, imageInfoReader));
	}
	
	private static final class ManagedOperationListenerImpl implements ManagedOperationListener {

		private final World world;
		private final WorldPanel container;
		private final ImageInfoReader imageInfoReader;
		
		public ManagedOperationListenerImpl(World world, WorldPanel container, ImageInfoReader imageInfoReader) {
			this.world = world;
			this.container = container;
			this.imageInfoReader = imageInfoReader;
		}

		@Override
		public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
			if (managedOperation == Actions.CUT_WOOD_ACTION && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 6) {
				MessageDialogUtils.showMessage("You can keep cutting wood six times, so that you can build somewhere to sleep.", "Cutting Wood", target, container, imageInfoReader);
			} else if (managedOperation == Actions.CUT_WOOD_ACTION && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= 6) {
				MessageDialogUtils.showMessage("Now right-click on your character and choose build - build shack. \nChoose an empty space around your character and place the shack. \nNow Right-click on the shack to rest in it.", "Building Shack", target, container, imageInfoReader);
			} else if (managedOperation == Actions.SLEEP_ACTION) {
				MessageDialogUtils.showMessage("Resting restores energy, which is used for some actions like cutting wood. \nEnergy is indicated by the green bar at the lower right of the screen. \n", "Resting", target, container, imageInfoReader);
			}
		}
	}
}
