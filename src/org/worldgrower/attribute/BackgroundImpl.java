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
import org.worldgrower.creaturetype.CreatureTypeUtils;
import org.worldgrower.goal.ChildrenPropertyUtils;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Professions;

public class BackgroundImpl implements Background, Serializable {

	private final Map<Integer, List<AngryReason>> angryReasons = new HashMap<>();
	private final List<Integer> revengeTargets = new ArrayList<>();
	
	@Override
	public ProfessionExplanation chooseProfession(WorldObject performer, World world) {
		if (hasFoodDemand(performer)) {
			return new ProfessionExplanation(Professions.FARMER_PROFESSION, "I was hungry in the past");
		}

		List<WorldObject> parents = ChildrenPropertyUtils.getParents(performer, world);
		if (parents.size() < 2) {
			return new ProfessionExplanation(Professions.GRAVE_DIGGER_PROFESSION, "one of my parents died");
		} else {
			// TODO: traditional personality trait, authority following?
		}

		return null;
	}
	
	private boolean hasFoodDemand(WorldObject performer) {
		return performer.getProperty(Constants.DEMANDS).count(Constants.FOOD) > 0;
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
			revengeTargets.add(operationInfo.getPerformer().getProperty(Constants.ID));
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
			return (target == backgroundPerformer) && (managedOperation == Actions.MELEE_ATTACK_ACTION) && (CreatureTypeUtils.isUndead(performer));
		}	
		
		public WorldObject getAttacker() {
			return attacker;
		}
	}
	
	@Override
	public void addGoalObstructed(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		int performerId = performer.getProperty(Constants.ID);
		
		List<AngryReason> angryReasonList = angryReasons.get(performerId);
		if (angryReasonList == null) {
			angryReasonList = new ArrayList<>();
			angryReasons.put(performerId, angryReasonList);
		}
		
		String angryReason = managedOperation.getDescription(performer, actionTarget, args, world);
		
		WorldObject copyTarget = actionTarget.deepCopy();
		copyTarget.setProperty(Constants.NAME, "me");
		String angryReasonWithTargetTalking = managedOperation.getDescription(performer, copyTarget, args, world);
		
		angryReasonList.add(new AngryReason(angryReason, angryReasonWithTargetTalking, actionTarget.getProperty(Constants.ID)));
	}
	
	@Override
	public List<String> getAngryReasons(boolean firstPerson, int personTalkingId, WorldObject performer, World world) {
		List<String> result = new ArrayList<>();
		int performerId = performer.getProperty(Constants.ID);
		List<AngryReason> list = angryReasons.get(performerId);
		List<AngryReason> angryReasonsList = new ArrayList<>(list != null ? list : new ArrayList<>()); // return copy of List
		if (angryReasonsList.size() > 0) {
			for(int i = 0; i< angryReasonsList.size(); i++)  {
				AngryReason angryReason = angryReasonsList.get(i);
				
				String angryReasonDescription;
				if (personTalkingId == angryReason.getTargetId()) {
					angryReasonDescription = angryReason.getAngryReasonWithTargetTalking();
				} else {
					angryReasonDescription = angryReason.getAngryReason();
				}
				
				String pronoun = performer.getProperty(Constants.GENDER).equals("female") ? "She" : "He";
				String prefix = firstPerson ? "You were " : (pronoun + " was ");
				result.add(i, prefix + angryReasonDescription);
			}
			angryReasonsList = new ArrayList<>(new HashSet<>(angryReasonsList));
			
			return result;
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
	public boolean hasRevengeTarget(World world) {
		return revengeTargets.size() > 0;
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
		backgroundImpl.angryReasons.putAll(angryReasons);
		backgroundImpl.revengeTargets.addAll(revengeTargets);
		return backgroundImpl;
	}
	
	private static class AngryReason implements Serializable {
		private final String angryReason;
		private final String angryReasonWithTargetTalking;
		private final int targetId;
		
		public AngryReason(String angryReason, String angryReasonWithTargetTalking, int targetId) {
			this.angryReason = angryReason;
			this.angryReasonWithTargetTalking = angryReasonWithTargetTalking;
			this.targetId = targetId;
		}

		public String getAngryReason() {
			return angryReason;
		}

		public String getAngryReasonWithTargetTalking() {
			return angryReasonWithTargetTalking;
		}

		public int getTargetId() {
			return targetId;
		}
	}
}