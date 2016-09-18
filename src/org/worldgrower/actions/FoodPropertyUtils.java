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
package org.worldgrower.actions;

import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.condition.Conditions;

public class FoodPropertyUtils {

	public static final int FOOD_MULTIPLIER = 175;
	
	public static void checkFoodSourceExhausted(WorldObject foodSource) {
		int targetFoodSource = foodSource.getProperty(Constants.FOOD_SOURCE);
		if (targetFoodSource <= 200 && Constants.FOOD_PRODUCED.isAtMax(foodSource)) {
			foodSource.setProperty(Constants.HIT_POINTS, 0);
		}
	}
	
	public static boolean foodSourceHasEnoughFood(WorldObject target) {
		return (target.hasProperty(Constants.FOOD_SOURCE)) && (target.getProperty(Constants.FOOD_SOURCE) >= 100);
	}
	
	public static boolean leftHandContainsScythe(WorldObject performer) {
		WorldObject leftHand = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		return (leftHand != null && leftHand.hasProperty(Constants.SCYTHE_QUALITY));
	}
	
	private static boolean performerHasBoon(WorldObject performer) {
		Conditions conditions = performer.getProperty(Constants.CONDITIONS);
		return conditions.hasCondition(Condition.DEMETER_BOON_CONDITION);
	}
	
	public static int calculateFarmingQuantity(WorldObject performer) {
		int quantity = SkillUtils.getLogarithmicSkillBonus(performer, Constants.FARMING_SKILL);
		if (leftHandContainsScythe(performer)) {
			quantity += 1;
		}
		if (performerHasBoon(performer)) {
			quantity += 1;
		}
		return quantity;
	}
	
	public static int getFarmingGrapesQuantity(WorldObject performer) {
		int quantity = SkillUtils.getLogarithmicSkillBonus(performer, Constants.FARMING_SKILL);
		if (ConditionUtils.performerHasCondition(performer, Condition.DIONYSUS_BOON_CONDITION)) {
			quantity++;
		}
		return quantity;
	}
	
	public static boolean leftHandContainsButcherKnife(WorldObject performer) {
		WorldObject leftHand = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		return (leftHand != null && leftHand.hasProperty(Constants.BUTCHER_QUALITY));
	}
	
	public static List<WorldObject> getMaleCattle(List<WorldObject> ownedCattle) {
		return ownedCattle.stream().filter(w -> w.getProperty(Constants.GENDER).equals("male")).collect(Collectors.toList());
	}
}
