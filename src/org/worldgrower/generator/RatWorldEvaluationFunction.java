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
package org.worldgrower.generator;

import java.util.Arrays;
import java.util.List;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectPriorities;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class RatWorldEvaluationFunction implements WorldObjectPriorities {

	@Override
	public List<Goal> getPriorities(WorldObject performer, World world) {
		return Arrays.asList(Goals.KILL_OUTSIDERS_GOAL, Goals.ANIMAL_FOOD_GOAL, Goals.OFFSPRING_GOAL, Goals.REST_GOAL, Goals.IDLE_GOAL);
	}
}