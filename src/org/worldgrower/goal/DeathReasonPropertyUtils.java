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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.DeadlyAction;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.DeadlyCondition;

public class DeathReasonPropertyUtils {

	public static void targetDiesByPerformerAction(WorldObject performer, WorldObject target, DeadlyAction action, World world) {
		setDeathReason(target, target.getProperty(Constants.NAME) + " was " + action.getDeathDescription(performer, target), world);
	}

	public static void targetDiesByCondition(DeadlyCondition condition, WorldObject target, World world) {
		setDeathReason(target, target.getProperty(Constants.NAME) + " was " + condition.getDeathDescription(), world);
	}
	
	public static void targetDiesByDrowning(WorldObject target, World world) {
		setDeathReason(target, target.getProperty(Constants.NAME) + " was killed by drowning", world);
	}
	
	private static void setDeathReason(WorldObject target, String reason, World world) {
		target.setProperty(Constants.DEATH_REASON, reason);
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(target, target, Constants.DEATH_REASON, reason, world);
	}
	
	public static List<String> getAllDeathReasons() {
		List<String> allDeathReasons = new ArrayList<>();
		allDeathReasons.addAll(Actions.getDeadlyActionDescriptions());
		allDeathReasons.addAll(Condition.getDeadlyConditions());
		allDeathReasons.add("drowning");
		return allDeathReasons;
	}
}
