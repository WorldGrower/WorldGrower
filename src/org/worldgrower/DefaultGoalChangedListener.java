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
package org.worldgrower;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

/**
 * default implementation if a goal changes:
 * if the npc wants to hide the goal, a better goal is added to the facade.
 */
public class DefaultGoalChangedListener implements GoalChangedListener {

	private Map<Goal, GoalChangedHandler> goalChangedMap = new HashMap<>();
	
	public DefaultGoalChangedListener() {
		goalChangedMap.put(Goals.ASSASSINATE_TARGET_GOAL, new GoalChangedHandler(Goals.HEAL_OTHERS_GOAL, Actions.MINOR_HEAL_ACTION));
		goalChangedMap.put(Goals.BECOME_LICH_GOAL, new GoalChangedHandler(Goals.RESEARCH_MAGIC_SKILLS_KNOWLEDGE_GOAL, Actions.RESEARCH_EVOCATION_SKILL_ACTION));
		goalChangedMap.put(Goals.CREATE_POISON_GOAL, new GoalChangedHandler(Goals.CREATE_WINE_GOAL, Actions.BREW_WINE_ACTION));
		goalChangedMap.put(Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL, new GoalChangedHandler(Goals.SHRINE_TO_DEITY_GOAL, Actions.BUILD_SHRINE_ACTION));
		goalChangedMap.put(Goals.FILL_SOUL_GEM_GOAL, new GoalChangedHandler(Goals.MINT_GOLD_GOAL, Actions.MINE_GOLD_ACTION));
		goalChangedMap.put(Goals.FIND_ASSASSINATION_CLIENT_GOAL, new GoalChangedHandler(Goals.BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL, Actions.VOTE_FOR_LEADER_ACTION));
		goalChangedMap.put(Goals.GHOUL_GOAL, new GoalChangedHandler(Goals.HEAL_OTHERS_GOAL, Actions.MINOR_HEAL_ACTION));
		goalChangedMap.put(Goals.KILL_VILLAGERS_GOAL, new GoalChangedHandler(Goals.HOUSE_GOAL, Actions.BUILD_HOUSE_ACTION));
		goalChangedMap.put(Goals.LEGALIZE_VAMPIRISM_GOAL, new GoalChangedHandler(Goals.BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL, Actions.VOTE_FOR_LEADER_ACTION));
		goalChangedMap.put(Goals.SACRIFICE_PEOPLE_TO_DEITY_GOAL, new GoalChangedHandler(Goals.SHRINE_TO_DEITY_GOAL, Actions.BUILD_SHRINE_ACTION));
		goalChangedMap.put(Goals.SCRIBE_NECROMANCER_SPELLS_GOAL, new GoalChangedHandler(Goals.SCRIBE_WIZARD_SPELLS_GOAL, Actions.RESEARCH_EVOCATION_SKILL_ACTION));
		goalChangedMap.put(Goals.SOUL_GEM_GOAL, new GoalChangedHandler(Goals.STONE_GOAL, Actions.MINE_STONE_ACTION));
		goalChangedMap.put(Goals.STEAL_GOAL, new GoalChangedHandler(Goals.FOOD_GOAL, Actions.EAT_FROM_INVENTORY_ACTION));
		goalChangedMap.put(Goals.VAMPIRE_BLOOD_LEVEL_GOAL, new GoalChangedHandler(Goals.WINE_GOAL, Actions.BREW_WINE_ACTION));
		goalChangedMap.put(Goals.GHOUL_MEAT_LEVEL_GOAL, new GoalChangedHandler(Goals.GATHER_REMAINS_GOAL, Actions.GATHER_REMAINS_ACTION));
		goalChangedMap.put(Goals.SWINDLE_MONEY_GOAL, new GoalChangedHandler(Goals.SOCIALIZE_GOAL, Actions.TALK_ACTION));
	}
	
	@Override
	public void goalChanged(WorldObject performer, Goal oldGoal, Goal newGoal) {
		if (performer.hasProperty(Constants.FACADE)) {
			WorldObject facade = performer.getProperty(Constants.FACADE);
			if (facade != null) {
				MetaInformation metaInformation = facade.getProperty(Constants.META_INFORMATION);
				if (metaInformation == null) {
					metaInformation = new MetaInformation(performer);
					facade.setProperty(Constants.META_INFORMATION, metaInformation);
				}
				
				GoalChangedHandler goalChangedHandler = goalChangedMap.get(newGoal);
				if (goalChangedHandler != null) {
					goalChangedHandler.setGoalAndTasks(performer, metaInformation);
				} else {
					facade.removeProperty(Constants.META_INFORMATION);
				}
			}
		}
	}
	
	private static class GoalChangedHandler implements Serializable {
		
		private final Goal goal;
		private final ManagedOperation action;
		
		public GoalChangedHandler(Goal goal, ManagedOperation action) {
			this.goal = goal;
			this.action = action;
		}

		public void setGoalAndTasks(WorldObject performer, MetaInformation metaInformation) {
			metaInformation.setFinalGoal(goal);
			metaInformation.setCurrentTask(Arrays.asList(new OperationInfo(performer, performer, Args.EMPTY, action)), GoalChangedReason.EMPTY_META_INFORMATION);
		}
	}
	
	public boolean wantsGoalHidden(WorldObject performer, Goal goal) {
		return goalChangedMap.containsKey(goal);
	}
	
	private static final DefaultGoalChangedListener INSTANCE = new DefaultGoalChangedListener();
	
	public static boolean wantsToKeepGoalHidden(WorldObject performer, Goal goal) {
		return INSTANCE.wantsGoalHidden(performer, goal);
	}
}
