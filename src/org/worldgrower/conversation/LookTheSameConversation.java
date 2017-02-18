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

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.text.TextId;

public class LookTheSameConversation implements InterceptedConversation {

	private static final int YOU_LOOK = -1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (isConversationAvailable(performer, target, world)) {
			int replyId = YOU_LOOK;
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
					new Response(YOU_LOOK, TextId.LOOK_SAME)
				);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		if (FacadeUtils.performerIsSuccessFullyDisguised(performer)) {
			return performer.equals(target);
		}
		return false;
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext, Conversation originalConversation) {
	}
}
