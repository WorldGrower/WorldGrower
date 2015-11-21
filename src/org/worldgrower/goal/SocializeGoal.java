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
package org.worldgrower.goal;

import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.history.HistoryItem;

public class SocializeGoal implements Goal {

	public SocializeGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		
		List<WorldObject> targets = GoalUtils.findNearestNewTargets(performer, Actions.TALK_ACTION, Conversations.createArgs(Conversations.NAME_CONVERSATION), w -> isFirstTimeSocializeTargetForPerformer(performer, w), world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.NAME_CONVERSATION), Actions.TALK_ACTION);
		} else {
			targets = GoalUtils.findNearestNewTargets(performer, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), w -> isSocializeTargetForPerformer(performer, w), world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), Actions.TALK_ACTION);
			} else {
				targets = GoalUtils.findNearestNewTargets(performer, Actions.TALK_ACTION, Conversations.createArgs(Conversations.FAMILY_CONVERSATION), w -> isSocializeTargetForPerformer(performer, w), world);
				if (targets.size() > 0) {
					return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.FAMILY_CONVERSATION), Actions.TALK_ACTION);
				} else {
					targets = GoalUtils.findNearestNewTargets(performer, Actions.TALK_ACTION, Conversations.createArgs(Conversations.DEITY_CONVERSATION), w -> isSocializeTargetForPerformer(performer, w), world);
					if (targets.size() > 0) {
						return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.DEITY_CONVERSATION), Actions.TALK_ACTION);
					} else {
						OperationInfo shareKnowledgeOperationInfo = getShareKnowledgeOperationInfo(performer, world);
						if (shareKnowledgeOperationInfo != null) {
							return shareKnowledgeOperationInfo;
						}
					}
				}
			}
		}
		return null;
	}

	OperationInfo getShareKnowledgeOperationInfo(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.TALK_ACTION, w -> isTargetForShareKnowledgeConversation(performer, w, world), world);
		for(WorldObject target : targets) {
			if (performer.getProperty(Constants.RELATIONSHIPS).getValue(target) >= 0) {
				KnowledgeMap performerOnlyKnowledge = KnowledgePropertyUtils.getPerformerOnlyKnowledge(performer, target);
				for(Integer subjectId : performerOnlyKnowledge.getIds()) {
					WorldObject subject = world.findWorldObject(Constants.ID, subjectId);
					if (!target.equals(subject)) {
						int[] args = Conversations.createArgs(Conversations.SHARE_KNOWLEDGE_CONVERSATION, subject);
						return new OperationInfo(performer, target, args, Actions.TALK_ACTION);
					}
				}
			}
		}
		return null;
	}

	static boolean isTargetForShareKnowledgeConversation(WorldObject performer, WorldObject target, World world) {
		return KnowledgePropertyUtils.performerKnowsMoreThanTarget(performer, target) 
				&& !Conversations.SHARE_KNOWLEDGE_CONVERSATION.previousAnswerWasGetLost(getPreviousResponseIds(performer, target, Conversations.SHARE_KNOWLEDGE_CONVERSATION, world));
	}
	
	static List<Integer> getPreviousResponseIds(WorldObject performer, WorldObject target, Conversation conversation, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(performer, target, Actions.TALK_ACTION);
		historyItems = historyItems.stream().filter(h -> h.getOperationInfo().firstArgsIs(Conversations.createArgs(conversation)[0])).collect(Collectors.toList());
		return historyItems.stream().map(h -> (Integer)h.getAdditionalValue()).collect(Collectors.toList());
	}
	
	static boolean isFirstTimeSocializeTargetForPerformer(WorldObject performer, WorldObject w) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		return !relationships.contains(w) && isSocializeTargetForPerformer(performer, w);
	}
	
	static boolean isSocializeTargetForPerformer(WorldObject performer, WorldObject w) {
		return !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return (performer.getProperty(Constants.SOCIAL) > 750);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return (performer.getProperty(Constants.SOCIAL) > 250);
	}

	@Override
	public String getDescription() {
		return "socializing";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.SOCIAL);
	}
}