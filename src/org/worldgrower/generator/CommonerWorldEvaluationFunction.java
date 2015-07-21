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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectPriorities;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.profession.Profession;

public class CommonerWorldEvaluationFunction implements WorldObjectPriorities {

	@Override
	public List<Goal> getPriorities(WorldObject performer, World world) {
		Curse curse = performer.getProperty(Constants.CURSE);
		
		if (curse != null) {
			return curse.getCurseGoals();
		} else {
			Profession profession = performer.getProperty(Constants.PROFESSION);
			
			List<Goal> professionGoals;
			if (profession != null) {
				professionGoals = profession.getProfessionGoals();
			} else {
				professionGoals = new ArrayList<>();
			}
			professionGoals = new ArrayList<>(professionGoals);
			professionGoals.add(Goals.BECOME_ORGANIZATION_MEMBER_GOAL);
			
			List<Goal> genericGoals = Arrays.asList(
					Goals.PROTECT_ONSE_SELF_GOAL, 
					Goals.FOOD_GOAL, 
					Goals.WATER_GOAL,
					Goals.REST_GOAL,
					Goals.SHACK_GOAL, 
					Goals.HOUSE_GOAL, 
					Goals.CHOOSE_PROFESSION_GOAL);
			
			List<Goal> backgroundGoals = performer.getProperty(Constants.BACKGROUND).getPersonalGoals(performer, world);
			
			List<Goal> personalGoals = Arrays.asList(
					Goals.SOCIALIZE_GOAL,
					Goals.MATE_GOAL,
					Goals.CHILDREN_GOAL,
					Goals.IDLE_GOAL);
					
			
			List<Goal> result = new ArrayList<>();
			result.addAll(genericGoals);
			result.addAll(backgroundGoals);
			result.addAll(professionGoals);
			result.addAll(personalGoals);
			return result;
		}
	}
}