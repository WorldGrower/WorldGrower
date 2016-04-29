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
package org.worldgrower.goal;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;

public class ThieveryPropertyUtils {

	public static void addThievingKnowledge(WorldObject performer, WorldObject target, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
	}

	public static boolean isThieverySuccess(WorldObject performer, World world, WorldObject worldObjectToSteal) {
		int price = worldObjectToSteal.getProperty(Constants.PRICE);
		int weight = getWeight(worldObjectToSteal);
		
		SkillUtils.useSkill(performer, Constants.THIEVERY_SKILL, world.getWorldStateChangedListeners());
		int thievery = Constants.THIEVERY_SKILL.getLevel(performer);
		
		return price + weight < thievery + 5;
	}

	private static int getWeight(WorldObject worldObjectToSteal) {
		Integer weightInteger = worldObjectToSteal.getProperty(Constants.WEIGHT);
		return weightInteger != null ? weightInteger.intValue() : 0;
	}

	public static boolean isThieverySuccess(WorldObject performer, World world, int amount) {
		SkillUtils.useSkill(performer, Constants.THIEVERY_SKILL, world.getWorldStateChangedListeners());
		int thievery = Constants.THIEVERY_SKILL.getLevel(performer);
		
		return (amount / 10) < thievery + 5;
	}
}
