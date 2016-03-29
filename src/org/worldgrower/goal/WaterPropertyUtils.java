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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class WaterPropertyUtils {

	public static WorldObject findWaterSource(WorldObject performer, World world) {
		KnowledgeMap knowledgeMap = performer.getProperty(Constants.KNOWLEDGE_MAP);
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.DRINK_ACTION, w -> !knowledgeMap.hasProperty(w, Constants.POISON_DAMAGE) && Reach.distance(performer, w) < 15, world);
		if (targets.size() > 0) {
			return targets.get(0);
		} else {
			return null;
		}
	}

	public static void everyoneInVicinityKnowsOfPoisoning(WorldObject performer, WorldObject target, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(performer, target, Constants.POISON_DAMAGE, target.getProperty(Constants.POISON_DAMAGE), world);
	}

	public static boolean isWaterSafeToDrink(WorldObject performer, WorldObject target) {
		KnowledgeMap knowledgeMap = performer.getProperty(Constants.KNOWLEDGE_MAP);
		return (!knowledgeMap.hasProperty(target, Constants.POISON_DAMAGE));
	}
	
	public static void drink(WorldObject performer, WorldObject waterTarget, World world) {
		if (isWaterPoisoned(waterTarget)) {
			Conditions.add(performer, Condition.POISONED_CONDITION, 20, world);
			
			if (isWorldObject(waterTarget, world)) {
				WaterPropertyUtils.everyoneInVicinityKnowsOfPoisoning(performer, waterTarget, world);
			}
		}
		
		if (waterTarget.hasProperty(Constants.ALCOHOL_LEVEL)) {
			performer.increment(Constants.ALCOHOL_LEVEL, waterTarget.getProperty(Constants.ALCOHOL_LEVEL));
			if (performer.getProperty(Constants.ALCOHOL_LEVEL) > AlcoholLevelPropertyUtils.getIntoxicatedLimit(performer)) {
				Conditions.add(performer, Condition.INTOXICATED_CONDITION, Integer.MAX_VALUE, world);
			}
		}
		
		if (waterTarget.hasProperty(Constants.VAMPIRE_BLOOD_LEVEL)) {
			performer.increment(Constants.VAMPIRE_BLOOD_LEVEL, waterTarget.getProperty(Constants.VAMPIRE_BLOOD_LEVEL));
		}
		
		if (waterTarget.hasProperty(Constants.SLEEP_INDUCING_DRUG_STRENGTH)) {
			Conditions.add(performer, Condition.UNCONSCIOUS_CONDITION, 20, world);
		}
	}

	private static boolean isWorldObject(WorldObject waterTarget, World world) {
		return waterTarget.hasProperty(Constants.ID) && world.exists(waterTarget);
	}

	private static boolean isWaterPoisoned(WorldObject waterTarget) {
		return waterTarget.hasProperty(Constants.POISON_DAMAGE) && waterTarget.getProperty(Constants.POISON_DAMAGE) > 0;
	}
}
