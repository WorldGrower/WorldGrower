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
package org.worldgrower.personality;

import java.util.List;

import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goals;

public class PowerHungryTrait implements PersonalityTrait {

	public PowerHungryTrait(List<PersonalityTrait> allTraits) {
		allTraits.add(this);
	}

	@Override
	public String getAdjective(int value, int relationshipValue) {
		if (value > 0) {
			if (relationshipValue < 0) {
				return "power hungry";
			} else if (relationshipValue < 250) {
				return "power-loving";
			} else if (relationshipValue < 500) {
				return "determined";
			} else {
				return "ambitious";
			}
		} else {
			if (relationshipValue < 0) {
				return "lazy";
			} else if (relationshipValue < 250) {
				return "relaxed";
			} else if (relationshipValue < 500) {
				return "calm";
			} else {
				return "easy-going";
			}
		}
	}

	@Override
	public int calculateInitialValue(WorldObject performer) {
		int sign = Goals.FOOD_GOAL.calculateSign(performer, this);
		return sign * 500;
	}
}
