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
import org.worldgrower.attribute.BuildingType;
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
			if (managedOperation == Actions.READ_ACTION) {
				setStatusMessage("Well done. Now use the right arrow key to move your player character to the right, next to the tree.\n Then left-click on the tree to cut wood from it.", "Reading signpost", target);
			} else if (managedOperation == Actions.CUT_WOOD_ACTION && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 6 && performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.SHACK).size() == 0) {
				setStatusMessage("You can keep cutting wood six times, so that you can build somewhere to sleep.", "Cutting Wood", target);
			} else if (managedOperation == Actions.CUT_WOOD_ACTION && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= 6 && performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.SHACK).size() == 0) {
				setStatusMessage("Now left-click on your character and choose build - build shack. \nChoose an empty space around your character and place the shack. \nMove next to the shack and left-click on it to sleep in it.", "Building Shack", target);
			} else if (managedOperation == Actions.SLEEP_ACTION) {
				setStatusMessage("Resting restores energy, which is used for some actions like cutting wood. Energy is indicated by the green bar at the lower right of the screen. \nNow use the down arrow to move down to the berry bush.\n Then left-click on the berry bush to harvest food from it.", "Harvesting food", target);
			} else if (managedOperation == Actions.HARVEST_FOOD_ACTION) {
				setStatusMessage("Harvested food is stored in the inventory. Press the I key or left-click on the character and choose inventory to show the inventory. \nIn the inventory screen, left-click on the berries and choose to eat the berry.\n Eating restores food, which keeps up energy. Food is indicated by the yellow bar at the lower right of the screen.", "Eating food", target);
			} else if (managedOperation == Actions.EAT_FROM_INVENTORY_ACTION) {
				setStatusMessage("Now use the left arrow to move left to the other character.\n Then left-click on it to talk with it and ask its name. \n Asking a name is under personal information.", "Talking", target);
			} else if (managedOperation == Actions.TALK_ACTION) {
				switchToHostileRat();
				setStatusMessage("A hostile rat has been added. Press the C key or left-click on the character and choose character screen to equip weapons and armor. \n In the character screen, equip an iron cuirass as equipment and an iron claymore as a weapon. \nThen move next to the rat and left-click on it to attack it until it is dead.", "Equiping", target);
			} else if (managedOperation == Actions.MELEE_ATTACK_ACTION && !performer.isControlledByAI()) {
				setStatusMessage("To learn magic spells, build a library. \nTo build a library move your character next to the tree to cut wood. \nWhen you have gathered 6 wood, left-click on your character and choose build - build library", "Eating food", target);
			} else if (managedOperation == Actions.BUILD_LIBRARY_ACTION) {	
				setStatusMessage("Let's learn firebolt, move next to the library and left-click and choose research firebolt. \nTo learn faster, you can research multiple turns at one.", "Researching firebolt", performer);
			} else if (managedOperation == Actions.getResearchSpellActionFor(Actions.FIRE_BOLT_ATTACK_ACTION)) {
				setStatusMessage("Spells known to your characters are shown in the magic overview. \nPress the M key or left-click on the character and choose Magic Overview to show the magic overview. \nOnce you've learned firebolt and have no weapon equiped, shoot a firebolt at the tree: left-click the tree, go to the Evocation menu and choose fire bolt.", "Eating food", performer);
			} else if (managedOperation == Actions.FIRE_BOLT_ATTACK_ACTION) {
				setStatusMessage("Now that you know the basic actions, you can start a new game. \nClose any dialog windows, press the escape key to bring up the main menu and start a non-tutorial game.", "Talking", target);
			}
		}
		
		private void setStatusMessage(String message, String title, WorldObject target) {
			container.clearStatusMessages();
			MessageDialogUtils.showMessage(message, title, target, container, imageInfoReader);
		}

		private void switchToHostileRat() {
			List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.isControlledByAI());
			worldObjects.forEach(w -> world.removeWorldObject(w));
			WorldObject verminOrganization = GroupPropertyUtils.create(null, "vermin", world);
			new CreatureGenerator(verminOrganization).generateRat(10, 0, world);
		}
	}
}
