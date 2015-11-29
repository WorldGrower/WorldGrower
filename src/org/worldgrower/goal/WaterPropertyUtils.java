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
}
