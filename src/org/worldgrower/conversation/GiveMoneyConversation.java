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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class GiveMoneyConversation implements Conversation {

	private static final int THANKS = 0;
	private static final int GET_LOST = 1;
	private static final int THANKS_AGAIN = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		final int replyId;
		if (historyItems.size() == 0) {
			replyId = THANKS;
		} else {
			replyId = THANKS_AGAIN;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "Would you like to have 100 gold?"));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
				new Response(THANKS, "Thanks"),
				new Response(GET_LOST, "Get lost"),
				new Response(THANKS_AGAIN, "Thanks again"));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == THANKS || replyIndex == THANKS_AGAIN) {
			int relationshipBonus;
			
			if (target.getProperty(Constants.GOLD) >= 100) {
				relationshipBonus = 5;
			} else {
				relationshipBonus = 10;
			}
			
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, relationshipBonus, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			
			performerGivesMoneyToTarget(performer, target, 100);
			
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	private void performerGivesMoneyToTarget(WorldObject performer, WorldObject target, int amount) {
		performer.increment(Constants.GOLD, -amount);
		target.increment(Constants.GOLD, amount);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return performer.getProperty(Constants.GOLD) >= 100;
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "giving " + target.getProperty(Constants.NAME) + " some money";
	}

	public boolean previousAnswerWasNegative(List<Integer> previousResponseIds) {
		return previousResponseIds.contains(GET_LOST);
	}
}
