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
package org.worldgrower.gui.start;

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.WerewolfUtils;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;
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
				MessageDialogUtils.showMessage("Now right-click on your character and choose build - build shack. \nChoose an empty space around your character and place the shack. \nMove next to the shack and right-click on it to rest in it.", "Building Shack", target, container, imageInfoReader);
			} else if (managedOperation == Actions.SLEEP_ACTION) {
				MessageDialogUtils.showMessage("Resting restores energy, which is used for some actions like cutting wood. Energy is indicated by the green bar at the lower right of the screen. \nNow use the down arrow to move down to the berry bush.\n Then right-click on the berry bush to harvest food from it.", "Harvesting food", target, container, imageInfoReader);
			} else if (managedOperation == Actions.HARVEST_FOOD_ACTION) {
				MessageDialogUtils.showMessage("Harvested food is stored in the inventory. Press the I key or right-click on the character and choose inventory to show the inventory. \nIn the inventory screen, click the Actions button to eat the berry. Eating restores food, which keeps up energy. Food is indicated by the yellow bar at the lower right of the screen.", "Eating food", target, container, imageInfoReader);
			} else if (managedOperation == Actions.EAT_FROM_INVENTORY_ACTION) {
				MessageDialogUtils.showMessage("Now use the left arrow to move left to the other character.\n Then right-click on it to talk with it and ask its name. \n Asking a name is under personal information.", "Talking", target, container, imageInfoReader);
			} else if (managedOperation == Actions.TALK_ACTION) {
				switchToHostileRat();
				MessageDialogUtils.showMessage("A hostile rat has been added. Press the C key or right-click on the character and choose character screen to equip weapons and armor. \n In the character screen, equip an iron cuirass as equipment and an iron claymore as a weapon. \nThen move next to the rat and right-click on it to attack it until it is dead.", "Equiping", target, container, imageInfoReader);
			} else if (managedOperation == Actions.MELEE_ATTACK_ACTION && !performer.isControlledByAI()) {
				MessageDialogUtils.showMessage("Now that you know the basic actions, you can start a new game. \nClose the talk dialog, press the escape key to bring up the main menu and start a non-tutorial game.", "Talking", target, container, imageInfoReader);
			}
		}

		private void switchToHostileRat() {
			List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.isControlledByAI());
			worldObjects.forEach(w -> world.removeWorldObject(w));
			WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", world);
			new CreatureGenerator(verminOrganization).generateRat(10, 0, world);
		}
	}
}
