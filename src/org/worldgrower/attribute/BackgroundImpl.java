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
package org.worldgrower.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.OperationInfoEvaluator;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.history.HistoryItem;

public class BackgroundImpl implements Background, Serializable {

	private final List<Goal> importantUnmetGoals = new ArrayList<>();
	private final Map<Integer, List<String>> angryReasons = new HashMap<>();
	private final List<Integer> revengeTargets = new ArrayList<>();
	
	@Override
	public <T> T chooseValue(WorldObject backgroundPerformer, ManagedProperty<T> property, World world) {
		Deity demeter = Deity.DEMETER;
		Deity hephaestus = Deity.HEPHAESTUS;
		Deity hades = Deity.HADES;
		
		if (property == Constants.DEITY) {
			if (importantUnmetGoals.contains(Goals.FOOD_GOAL)) {
				return (T)demeter;
			}
			
			List<HistoryItem> importantHistoryItems = world.getHistory().findHistoryItems(Actions.MELEE_ATTACK_ACTION);
			for(HistoryItem historyItem : importantHistoryItems) {
				OperationInfo operationInfo = historyItem.getOperationInfo();
				if (operationInfo.evaluate(new PerformerWasAttackedByUndead(backgroundPerformer))) {
					return (T)hades;
				}
				
			}
		}
		
		if (property == Constants.PROFESSION) {
			if (importantUnmetGoals.contains(Goals.FOOD_GOAL)) {
				return (T)"farmer";
			}
		}
		
		
		return null;
	}

	@Override
	public List<Goal> getPersonalGoals(WorldObject backgroundPerformer, World world) {
		List<Goal> personalGoals = new ArrayList<>();
		
		if (revengeTargets.size() > 0) {
			personalGoals.add(Goals.REVENGE_GOAL);
		}
		
		return personalGoals;
	}

	@Override
	public void checkForNewGoals(WorldObject performer, World world) {
		checkForNewRevengeTargets(performer, world);
	}
	
	private void checkForNewRevengeTargets(WorldObject backgroundPerformer, World world) {
		Collection<HistoryItem> importantHistoryItems = world.getHistory().getAllLastPerformedOperations();
		for(HistoryItem historyItem : importantHistoryItems) {
			OperationInfo operationInfo = historyItem.getOperationInfo();
			if (operationInfo.getTarget().equals(backgroundPerformer)) {
				handlePerformerWasAttacked(backgroundPerformer, operationInfo);
			}
		}
	}

	private void handlePerformerWasAttacked(WorldObject backgroundPerformer, OperationInfo operationInfo) {
		PerformerWasAttacked performerWasAttacked = new PerformerWasAttacked(backgroundPerformer);
		if (operationInfo.evaluate(performerWasAttacked)) {
			revengeTargets.add(operationInfo.getTarget().getProperty(Constants.ID));
		}
	}
	
	private static class PerformerWasAttacked implements OperationInfoEvaluator {
		private final WorldObject backgroundPerformer;
		private WorldObject attacker;
		
		public PerformerWasAttacked(WorldObject backgroundPerformer) {
			this.backgroundPerformer = backgroundPerformer;
		}

		@Override
		public boolean evaluate(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
			this.attacker = performer;
			return (target.equals(backgroundPerformer)) && (managedOperation == Actions.MELEE_ATTACK_ACTION);
		}	
		
		public WorldObject getAttacker() {
			return attacker;
		}
	}
	
	private static class PerformerWasAttackedByUndead implements OperationInfoEvaluator {
		private final WorldObject backgroundPerformer;
		private WorldObject attacker;
		
		public PerformerWasAttackedByUndead(WorldObject backgroundPerformer) {
			this.backgroundPerformer = backgroundPerformer;
		}

		@Override
		public boolean evaluate(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
			this.attacker = performer;
			return (target == backgroundPerformer) && (managedOperation == Actions.MELEE_ATTACK_ACTION) && (performer.getProperty(Constants.CREATURE_TYPE) == CreatureType.UNDEAD_CREATURE_TYPE);
		}	
		
		public WorldObject getAttacker() {
			return attacker;
		}
	}
	
	@Override
	public String toString() {
		return "importantUnmetGoals = " + importantUnmetGoals;
	}

	@Override
	public void addGoalObstructed(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		int performerId = performer.getProperty(Constants.ID);
		
		List<String> angryReasonList = angryReasons.get(performerId);
		if (angryReasonList == null) {
			angryReasonList = new ArrayList<>();
			angryReasons.put(performerId, angryReasonList);
		}
		
		angryReasonList.add(managedOperation.getDescription(performer, actionTarget, args, world));
	}
	
	@Override
	public List<String> getAngryReasons(boolean firstPerson, WorldObject performer, World world) {
		int performerId = performer.getProperty(Constants.ID);
		List<String> angryReasonsList = angryReasons.get(performerId);
		if (angryReasonsList != null) {
			for(int i = 0; i< angryReasonsList.size(); i++)  {
				String angryReason = angryReasonsList.get(i);
				String pronoun = performer.getProperty(Constants.GENDER).equals("female") ? "She" : "He";
				String prefix = firstPerson ? "You were " : pronoun + " was";
				angryReasonsList.set(i, prefix + angryReason);
			}
			angryReasonsList = new ArrayList<>(new HashSet<>(angryReasonsList));
			
			return angryReasonsList;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void remove(WorldObject worldObject, ManagedProperty<?> property, int id) {
		BackgroundProperty backgroundProperty = (BackgroundProperty) property;
		worldObject.getProperty(backgroundProperty).remove(id);
	}

	@Override
	public WorldObject getRevengeTarget(World world) {
		int id = revengeTargets.get(0);
		return world.findWorldObject(Constants.ID, id);
	}

	@Override
	public void remove(int id) {
		Integer key = Integer.valueOf(id);
		angryReasons.remove(key);
		revengeTargets.remove(key);
	}

	@Override
	public Background copy() {
		BackgroundImpl backgroundImpl = new BackgroundImpl();
		backgroundImpl.importantUnmetGoals.addAll(importantUnmetGoals);
		backgroundImpl.angryReasons.putAll(angryReasons);
		backgroundImpl.revengeTargets.addAll(revengeTargets);
		return backgroundImpl;
	}
}