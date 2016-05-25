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
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.AttackActionLegalHandler;
import org.worldgrower.actions.legal.DefaultActionLegalHandler;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.LegalActionsPropertyUtils;

public class VampireUtils {

	public static void vampirizePerson(WorldObject worldObject, WorldStateChangedListeners creatureTypeChangedListeners) {
		worldObject.setProperty(Constants.VAMPIRE_BLOOD_LEVEL, 1000);
		worldObject.setProperty(Constants.CREATURE_TYPE, CreatureType.VAMPIRE_CREATURE_TYPE);
		worldObject.setProperty(Constants.CURSE, Curse.VAMPIRE_CURSE);
		
		creatureTypeChangedListeners.fireCreatureTypeChanged(worldObject, CreatureType.VAMPIRE_CREATURE_TYPE, "You crave blood, you must have become a vampire");
	}
	
	public static boolean canBecomeVampire(WorldObject worldObject) {
		return worldObject.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}
	
	public static int getVampireCount(World world) {
		return world.findWorldObjects(w -> w.hasProperty(Constants.CURSE) && w.getProperty(Constants.CURSE) == Curse.VAMPIRE_CURSE).size();
	}
	
	public static boolean isBitingPeopleLegal(World world) {
		return LegalActionsPropertyUtils.getLegalActions(world).getLegalActions().get(new LegalAction(Actions.VAMPIRE_BITE_ACTION, new AttackActionLegalHandler()));
	}
}
