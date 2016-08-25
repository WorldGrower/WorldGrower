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
import org.worldgrower.condition.Condition;

public class ThieveryPropertyUtils {

	public static void addThievingKnowledge(WorldObject performer, WorldObject target, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
	}

	public static boolean isThieverySuccess(WorldObject performer, WorldObject target, World world, WorldObject worldObjectToSteal) {
		int amount = worldObjectToSteal.getProperty(Constants.PRICE);
		int weight = getWeight(worldObjectToSteal);
		
		int randomValue = getRandomValueBetween0and99(performer, target, world);
		int thieverySuccessPercentage = getThieverySuccessPercentage(performer, target, amount, weight);
		return randomValue >= thieverySuccessPercentage;
	}

	private static int getWeight(WorldObject worldObjectToSteal) {
		Integer weightInteger = worldObjectToSteal.getProperty(Constants.WEIGHT);
		return weightInteger != null ? weightInteger.intValue() : 0;
	}

	public static boolean isThieverySuccess(WorldObject performer, WorldObject target, World world, int amount) {
		int randomValue = getRandomValueBetween0and99(performer, target, world);
		int thieverySuccessPercentage = getThieverySuccessPercentage(performer, target, amount, 0);
		return randomValue <= thieverySuccessPercentage;
	}
	
	private static int getRandomValueBetween0and99(WorldObject performer, WorldObject target, World world) {
		int currentTurn = world.getCurrentTurn().getValue();
		String performerName = performer.getProperty(Constants.NAME);
		String targetName = target.getProperty(Constants.NAME);
		return (performerName.length() + targetName.length() + currentTurn) % 100;
	}
	
	public static boolean isOpenLockSuccess(WorldObject performer, WorldObject target, World world) {
		int randomValue = getRandomValueBetween0and99(performer, target, world);
		int openLockSuccessPercentage = getOpenLockSuccessPercentage(performer, target);
		return randomValue <= openLockSuccessPercentage;
	}
	
	static int getThieverySuccessPercentage(WorldObject performer, WorldObject target, int moneyValue, int weight) {
		int thievery = getThieveryLevel(performer);
		
		int successPercentage = (int)(10 + (thievery + 90) / (Math.log(moneyValue + weight + 2)));
		if (successPercentage > 99) {
			successPercentage = 99;
		}
		return successPercentage;
	}
	
	static int getOpenLockSuccessPercentage(WorldObject performer, WorldObject target) {
		int thievery = getThieveryLevel(performer);
		int lockStrength = target.getProperty(Constants.LOCK_STRENGTH);
		int successPercentage = (int)(10 + (thievery * 3 + 50) / (Math.log(lockStrength + 3)));
		if (successPercentage > 99) {
			successPercentage = 99;
		}
		return successPercentage;
	}

	static int getThieveryLevel(WorldObject performer) {
		int thievery = Constants.THIEVERY_SKILL.getLevel(performer);
		
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION)) {
			thievery += 5;
		}
		return thievery;
	}
}
