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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class GiveFoodConversation implements Conversation {

	private static final int THANKS = 0;
	private static final int GET_LOST = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = THANKS;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "Would you like to have some food?"));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
				new Response(THANKS, "Thanks"),
				new Response(GET_LOST, "Get lost"));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == THANKS) {
			int relationshipBonus;
			
			if (target.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) > 0) {
				relationshipBonus = 5;
			} else {
				relationshipBonus = 10;
			}
			
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, relationshipBonus, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			
			performerGivesFoodToTarget(performer, target);
			
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	private void performerGivesFoodToTarget(WorldObject performer, WorldObject target) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int indexOfFood = performerInventory.getIndexFor(Constants.FOOD);
		WorldObject food = performerInventory.get(indexOfFood);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		targetInventory.addQuantity(food, 1);
		performerInventory.removeQuantity(Constants.FOOD, 1);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) > 0;
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "giving " + target.getProperty(Constants.NAME) + " some food";
	}
}
