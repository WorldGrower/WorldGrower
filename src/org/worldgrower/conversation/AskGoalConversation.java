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
package org.worldgrower.conversation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class AskGoalConversation implements Conversation {

	private static final int YES = 0;
	private static final int ALREADY = 1;
	private static final int NO = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		Goal goal = getGoal(conversationContext);
		boolean goalMet = goal.isGoalMet(target, world);
		
		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (goalMet) {
			replyId = ALREADY;
		} else if (relationshipValue >= 500 && findSameConversation(conversationContext).size() == 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private Goal getGoal(ConversationContext conversationContext) {
		int goalIndex = conversationContext.getAdditionalValue();
		Goal goal = getAllGoals().get(goalIndex);
		return goal;
	}
	
	private List<Goal> getAllGoals() {
		// Lazy init because Actions shouldn't use Goals.xxx
		// at class init. Doing this leads to cyclic dependency
		return Arrays.asList(
					Goals.CREATE_OR_PLANT_WOOD_GOAL,
					Goals.MINE_STONE_GOAL,
					Goals.MINE_ORE_GOAL,
					Goals.MINE_GOLD_GOAL,
					Goals.MINE_SOUL_GEMS_GOAL,
					Goals.GATHER_FOOD_GOAL,
					Goals.COLLECT_WATER_GOAL,
					Goals.CRAFT_EQUIPMENT_GOAL,
					Goals.CREATE_WINE_GOAL,
					Goals.CHILDREN_GOAL);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		
		List<Goal> allGoals = getAllGoals();
		
		for(int goalIndex = 0; goalIndex<allGoals.size(); goalIndex++) {
			Goal goal = allGoals.get(goalIndex);
			questions.add(new Question(null, Text.QUESTION_ASK_GOAL.get(goal.getDescription()), goalIndex));
		}
		
		return questions;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		Goal goal = getGoal(conversationContext);
		
		return Arrays.asList(
			new Response(YES, Text.ANSWER_ASK_GOAL_YES.get(goal.getDescription())),
			new Response(ALREADY, Text.ANSWER_ASK_GOAL_EXPLAIN.get(goal.getDescription())),
			new Response(NO, Text.ANSWER_ASK_GOAL_NO.get()));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		Goal goal = getGoal(conversationContext);
		
		if (replyIndex == YES) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			target.setProperty(Constants.GIVEN_ORDER, goal);
		} else if (replyIndex == ALREADY) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -5, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "asking " + target.getProperty(Constants.NAME) + " to do something";
	}
}
