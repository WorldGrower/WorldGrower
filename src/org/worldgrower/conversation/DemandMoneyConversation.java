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

public class DemandMoneyConversation implements Conversation {

	private static final int GET_LOST = -999;
	private static final int SURE = 0;
	private static final int NO = 1;
	private static final int I_CAN_ONLY = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if ((relationshipValue < 0) || (historyItems.size() > 0)) {
			replyId = GET_LOST;
		} else if (relationshipValue > 500 && target.getProperty(Constants.GOLD) >= 100) {
			replyId = SURE;
		} else if (relationshipValue < 500) {
			replyId = NO;
		} else if (relationshipValue > 500 && target.getProperty(Constants.GOLD) < 100) {
			replyId = I_CAN_ONLY;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "Can you give me 100 gold?"));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(GET_LOST, "Get lost"),
			new Response(SURE, "I'll give you the money"),
			new Response(NO, "No"),
			new Response(I_CAN_ONLY, "I can  only spare a bit of gold")
		);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == SURE) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			
			performer.increment(Constants.GOLD, 100);
			target.increment(Constants.GOLD, -100);
			
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			
		} else if (replyIndex == I_CAN_ONLY) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 20, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			
			int goldQuantity = target.getProperty(Constants.GOLD);
			performer.increment(Constants.GOLD, goldQuantity);
			target.increment(Constants.GOLD, -goldQuantity);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "demanding money";
	}
}
