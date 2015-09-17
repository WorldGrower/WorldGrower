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
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Profession;

public class CommonerWorldEvaluationFunction implements WorldObjectPriorities {

	@Override
	public List<Goal> getPriorities(WorldObject performer, World world) {	
		Profession profession = performer.getProperty(Constants.PROFESSION);
		
		final List<Goal> professionGoals;
		final List<Goal> professionOrganizationGoals;
		if (profession != null) {
			professionGoals = new ArrayList<>(profession.getProfessionGoals());
			professionGoals.add(Goals.IMPROVE_ORGANIZATION_GOAL);
			
			professionOrganizationGoals = new ArrayList<>();
			professionOrganizationGoals.add(Goals.BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL);
			professionOrganizationGoals.add(Goals.LEARN_SKILL_GOAL);
		} else {
			professionGoals = new ArrayList<>();
			professionOrganizationGoals = new ArrayList<>();
		}
		
		List<Goal> religionOrganizationGoal = getReligionOrganizationGoals(performer, world);
		
		
		List<Goal> genericGoals = Arrays.asList(
				Goals.BRAWL_GOAL,
				Goals.FIGHT_IN_ARENA_GOAL,
				Goals.GET_POISON_CURED_GOAL,
				Goals.GET_HEALED_GOAL,
				Goals.PROTECT_ONSE_SELF_GOAL, 
				Goals.FOOD_GOAL, 
				Goals.DRINK_WATER_GOAL,
				Goals.REST_GOAL,
				Goals.SHACK_GOAL, 
				Goals.HOUSE_GOAL, 
				Goals.CHOOSE_PROFESSION_GOAL,
				Goals.CHOOSE_DEITY_GOAL,
				Goals.START_ORGANIZATION_VOTE_GOAL,
				Goals.ORGANIZATION_CANDIDATE_GOAL,
				Goals.ORGANIZATION_VOTE_GOAL,
				Goals.SET_TAXES_GOAL,
				Goals.BUY_CLOTHES_GOAL);
		
		List<Goal> backgroundGoals = performer.getProperty(Constants.BACKGROUND).getPersonalGoals(performer, world);
		
		List<Goal> personalGoals = Arrays.asList(
				Goals.SOCIALIZE_GOAL,
				Goals.MATE_GOAL,
				Goals.CHILDREN_GOAL,
				Goals.FURNITURE_GOAL,
				Goals.DONATE_MONEY_TO_ARENA_GOAL,
				Goals.SEX_GOAL,
				Goals.IDLE_GOAL);
				
		
		List<Goal> result = new ArrayList<>();
		result.addAll(genericGoals);
		result.addAll(backgroundGoals);
		result.addAll(professionOrganizationGoals);
		result.addAll(professionGoals);
		result.addAll(religionOrganizationGoal);
		result.addAll(personalGoals);
	
		Curse curse = performer.getProperty(Constants.CURSE);
		if (curse != null) {
			result = curse.getCurseGoals(result);
		}
		return result;
	}

	private List<Goal> getReligionOrganizationGoals(WorldObject performer, World world) {
		List<Goal> religionOrganizationGoals = new ArrayList<>();
		List<WorldObject> organizations = GroupPropertyUtils.getOrganizations(performer, world);
		for(WorldObject organization : organizations) {
			if (organization.hasProperty(Constants.DEITY)) {
				Goal organizationGoal = organization.getProperty(Constants.ORGANIZATION_GOAL);
				if (organizationGoal != null) {
					religionOrganizationGoals.add(organizationGoal);
				}
			}
		}
		
		return religionOrganizationGoals;
	}
}