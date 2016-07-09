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
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.util.SentenceUtils;

public class WhyNotIntelligentConversation implements InterceptedConversation {

	private static final int WHY_NOT_INTELLIGENT = -1;
	private static final int SEE_THROUGH = -2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (isConversationAvailable(performer, target, world)) {
			int replyId;
			if (FacadeUtils.performerIsSuccessFullyDisguised(performer)) {
				replyId = WHY_NOT_INTELLIGENT;
			} else {
				replyId = SEE_THROUGH;
			}
			return getReply(getReplyPhrases(conversationContext), replyId);
		}
		return null;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (isConversationAvailable(performer, target, world)) {
			String performerName = performer.getProperty(Constants.NAME);
			String article = SentenceUtils.getArticle(performerName);
			return Arrays.asList(
					new Response(WHY_NOT_INTELLIGENT, "What are you? Why am I talking with " + article + " " + performerName + "?"),
					new Response(SEE_THROUGH, "A good try, " + performerName + ", but I see through your disguise")
				);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		WorldObject facade = performer.getProperty(Constants.FACADE);
		return ((facade != null) && (facade.getProperty(Constants.ID) != null) && (!world.findWorldObjectById(facade.getProperty(Constants.ID)).hasIntelligence()));
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext, Conversation originalConversation) {
		
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (isConversationAvailable(performer, target, world)) {
			if (replyIndex == SEE_THROUGH) {
				RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(originalConversation), world);
			}
		}
	}
}
