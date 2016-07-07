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
package org.worldgrower.condition;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class WerewolfUtils {

	public static void makePersonIntoWerewolf(WorldObject worldObject, World world) {
		worldObject.setProperty(Constants.CREATURE_TYPE, CreatureType.WEREWOLF_CREATURE_TYPE);
		worldObject.setProperty(Constants.CURSE, Curse.WEREWOLF_CURSE);

		GroupPropertyUtils.throwPerformerOutOfAllGroups(worldObject, world);
		worldObject.setProperty(Constants.IMAGE_ID, ImageIds.WEREWOLF);
		
		world.getWorldStateChangedListeners().fireCreatureTypeChanged(worldObject, CreatureType.WEREWOLF_CREATURE_TYPE, "Your teeth and nails grow, you are covered in fur, you must have become a werewolf");
	}

	public static boolean canBecomeWerewolf(WorldObject worldObject) {
		return worldObject.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}
	
	public static int getWerewolfCount(World world) {
		return world.findWorldObjects(w -> w.hasProperty(Constants.CURSE) && w.getProperty(Constants.CURSE) == Curse.WEREWOLF_CURSE).size();
	}
}
