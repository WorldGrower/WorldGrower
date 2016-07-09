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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.Constants;
import org.worldgrower.DefaultGoalChangedListener;
import org.worldgrower.DefaultGoalObstructedHandler;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.OperationInfoEvaluator;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.ChildrenPropertyUtils;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
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
		
		if (isHarmed(performer)) {
			if (performer.getProperty(Constants.WISDOM) > performer.getProperty(Constants.STRENGTH)) {
				return new ProfessionExplanation(Professions.PRIEST_PROFESSION, "I wanted to be able to heal myself");
			} else {
				return new ProfessionExplanation(Professions.SHERIFF_PROFESSION, "I wanted to be able to protect myself");
			}
		}
		
		if (hasDiseases(performer)) {
			return new ProfessionExplanation(Professions.PRIEST_PROFESSION, "I wanted to be able to cure my diseases");
		}

		return null;
	}
	
	private boolean hasDiseases(WorldObject performer) {
		return performer.getProperty(Constants.CONDITIONS).hasDiseaseCondition();
	}

	private boolean isHarmed(WorldObject performer) {
		return performer.getProperty(Constants.HIT_POINTS) < performer.getProperty(Constants.HIT_POINTS_MAX);
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
		Collection<OperationInfo> importantOperationInfos = world.getHistory().getAllLastPerformedOperations();
		for(OperationInfo operationInfo : importantOperationInfos) {
			// performer should exist, this method can be called in the onTurn method
			// during the onTurn method the performer may have been deleted
			if (operationInfo.getTarget().equals(backgroundPerformer) && world.exists(backgroundPerformer) && world.exists(operationInfo.getPerformer())) {
				handlePerformerWasAttacked(backgroundPerformer, operationInfo);
			}
		}
		cleanupRevengeTargets(world);
	}

	private void cleanupRevengeTargets(World world) {
		Iterator<Integer> revengeTargetIterator = revengeTargets.iterator();
		while (revengeTargetIterator.hasNext()) {
			int revengeTarget = revengeTargetIterator.next();
			if (!world.exists(revengeTarget)) {
				revengeTargetIterator.remove();
			}
		}
		
	}

	private void handlePerformerWasAttacked(WorldObject backgroundPerformer, OperationInfo operationInfo) {
		PerformerWasAttacked performerWasAttacked = new PerformerWasAttacked(backgroundPerformer);
		if (operationInfo.evaluate(performerWasAttacked) && !DefaultGoalObstructedHandler.isLegallyFighting(backgroundPerformer, operationInfo.getTarget(), operationInfo.getManagedOperation())) {
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
			return (target.equals(backgroundPerformer)) && (DefaultGoalObstructedHandler.performerAttacked(managedOperation));
		}	
		
		public WorldObject getAttacker() {
			return attacker;
		}
	}
	
	@Override
	public void addGoalObstructed(Goal obstructedGoal, WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
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
		
		angryReasonList.add(new AngryReason(angryReason, angryReasonWithTargetTalking, actionTarget.getProperty(Constants.ID), obstructedGoal));
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
				if (!DefaultGoalChangedListener.wantsToKeepGoalHidden(performer, angryReason.getObstructedGoal())) {
					String angryReasonDescription;
					if (personTalkingId == angryReason.getTargetId()) {
						angryReasonDescription = angryReason.getAngryReasonWithTargetTalking();
					} else {
						angryReasonDescription = angryReason.getAngryReason();
					}
					
					String pronoun = performer.getProperty(Constants.GENDER).equals("female") ? "She" : "He";
					String prefix = firstPerson ? "You were " : (pronoun + " was ");
					result.add(prefix + angryReasonDescription);
				}
			}
			angryReasonsList = new ArrayList<>(new HashSet<>(angryReasonsList));
			
			return result;
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public String getConcatenatedAngryReasons(boolean firstPerson, int personTalkingId, WorldObject performer, World world) {
		StringBuilder angryReasonBuilder = new StringBuilder();
		List<String> angryReasons = getAngryReasons(firstPerson, personTalkingId, performer, world);
		
		if (angryReasons.size() > 0) {
			for(int i=0; i<angryReasons.size(); i++) {
				String angryReason = angryReasons.get(i);
				angryReasonBuilder.append(angryReason);
				if (i < angryReasons.size() -1) {
					angryReasonBuilder.append("; ");	
				}
			}
		} else {
			angryReasonBuilder.append("I don't remember");
		}
		return angryReasonBuilder.toString();
	}

	@Override
	public void remove(WorldObject worldObject, ManagedProperty<?> property, int id) {
		BackgroundProperty backgroundProperty = (BackgroundProperty) property;
		worldObject.getProperty(backgroundProperty).remove(id);
	}

	@Override
	public boolean hasRevengeTarget(World world) {
		return revengeTargets.size() > 0 && world.exists(revengeTargets.get(0));
	}
	
	@Override
	public WorldObject getRevengeTarget(World world) {
		int id = revengeTargets.get(0);
		return world.findWorldObjectById(id);
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
	
	@Override
	public String toStringAngryReasons() {
		StringBuilder angryReasonsStringBuilder = new StringBuilder();
		for(Entry<Integer, List<AngryReason>> entry : angryReasons.entrySet()) {
			int id = entry.getKey();
			angryReasonsStringBuilder.append("id = ").append(id).append(":");
			List<AngryReason> angryReasonsList = entry.getValue();
			for(AngryReason angryReason : angryReasonsList) {
				angryReasonsStringBuilder.append(angryReason.getAngryReason()).append("<--").append(angryReason.getObstructedGoal().getDescription()).append("|");
			}
		}
		
		return angryReasonsStringBuilder.toString();
	}
	
	private static class AngryReason implements Serializable {
		private final String angryReason;
		private final String angryReasonWithTargetTalking;
		private final int targetId;
		private final Goal obstructedGoal;
		
		public AngryReason(String angryReason, String angryReasonWithTargetTalking, int targetId, Goal obstructedGoal) {
			this.angryReason = angryReason;
			this.angryReasonWithTargetTalking = angryReasonWithTargetTalking;
			this.targetId = targetId;
			this.obstructedGoal = obstructedGoal;
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

		public Goal getObstructedGoal() {
			return obstructedGoal;
		}
	}
}