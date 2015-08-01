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
			
			final List<Goal> professionGoals;
			final List<Goal> organizationGoals;
			if (profession != null) {
				professionGoals = new ArrayList<>(profession.getProfessionGoals());
				professionGoals.add(Goals.IMPROVE_ORGANIZATION_GOAL);
				
				organizationGoals = new ArrayList<>();
				organizationGoals.add(Goals.BECOME_ORGANIZATION_MEMBER_GOAL);
				organizationGoals.add(Goals.LEARN_SKILL_GOAL);
			} else {
				professionGoals = new ArrayList<>();
				organizationGoals = new ArrayList<>();
			}
			
			
			List<Goal> genericGoals = Arrays.asList(
					Goals.GET_POISON_CURED_GOAL,
					Goals.PROTECT_ONSE_SELF_GOAL, 
					Goals.FOOD_GOAL, 
					Goals.DRINK_WATER_GOAL,
					Goals.REST_GOAL,
					Goals.SHACK_GOAL, 
					Goals.HOUSE_GOAL, 
					Goals.CHOOSE_PROFESSION_GOAL);
			
			List<Goal> backgroundGoals = performer.getProperty(Constants.BACKGROUND).getPersonalGoals(performer, world);
			
			List<Goal> personalGoals = Arrays.asList(
					Goals.SOCIALIZE_GOAL,
					Goals.MATE_GOAL,
					Goals.CHILDREN_GOAL,
					Goals.SEX_GOAL,
					Goals.IDLE_GOAL);
					
			
			List<Goal> result = new ArrayList<>();
			result.addAll(genericGoals);
			result.addAll(backgroundGoals);
			result.addAll(organizationGoals);
			result.addAll(professionGoals);
			result.addAll(personalGoals);
			return result;
		}
	}
}