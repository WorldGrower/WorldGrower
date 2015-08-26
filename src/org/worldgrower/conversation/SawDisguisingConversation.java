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
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;

public class SawDisguisingConversation implements InterceptedConversation {

	private static final int SEE_THROUGH = -3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (isConversationAvailable(performer, target, world)) {
			int replyId = SEE_THROUGH;
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
			return Arrays.asList(
					new Response(SEE_THROUGH, "A good try, " + performer.getProperty(Constants.NAME) + ", but I saw you disguise yourself earlier")
				);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		WorldObject facade = performer.getProperty(Constants.FACADE);
		KnowledgeMap knowledgeMap = target.getProperty(Constants.KNOWLEDGE_MAP);
		boolean targetHasKnowledgeOverFacade = knowledgeMap != null ? knowledgeMap.hasProperty(performer, Constants.FACADE) : false;
		return ((facade != null) && (!FacadeUtils.performerIsSuccessFullyDisguised(performer)) && targetHasKnowledgeOverFacade);
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
