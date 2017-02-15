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
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class GiveItemConversation implements Conversation {

	private final IntProperty intPropertyToGive;
	private final int quantity;
	
	private static final int THANKS = 0;
	private static final int GET_LOST = 1;
	private static final int THANKS_AGAIN = 2;

	public GiveItemConversation(IntProperty intPropertyToGive, int quantity) {
		super();
		this.intPropertyToGive = intPropertyToGive;
		this.quantity = quantity;
	}

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
		return Arrays.asList(new Question(null, Text.QUESTION_GIVE_ITEM.get(intPropertyToGive.getName())));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
				new Response(THANKS, Text.ANSWER_GIVE_ITEM_THANKS),
				new Response(GET_LOST, Text.ANSWER_GIVE_ITEM_GETLOST),
				new Response(THANKS_AGAIN, Text.ANSWER_GIVE_ITEM_AGAIN));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == THANKS || replyIndex == THANKS_AGAIN) {
			int relationshipBonus = calculateRelationshipIncrease(target, intPropertyToGive);
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, relationshipBonus, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			BuySellUtils.performerGivesItemToTarget(performer, target, intPropertyToGive, quantity);
			
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	private static int calculateRelationshipIncrease(WorldObject target, IntProperty property) {
		int relationshipBonus;
		
		if (target.getProperty(Constants.INVENTORY).getQuantityFor(property) > 0) {
			relationshipBonus = 5;
		} else {
			relationshipBonus = 10;
		}
		return relationshipBonus;
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(intPropertyToGive) >= quantity;
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "giving " + target.getProperty(Constants.NAME) + " some " + intPropertyToGive.getName();
	}
}
