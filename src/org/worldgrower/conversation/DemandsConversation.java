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
import org.worldgrower.WorldObjectContainer;
import org.worldgrower.history.HistoryItem;

public class DemandsConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		final int replyId;
		WorldObjectContainer demands = target.getProperty(Constants.DEMANDS);
		if (demands.size() > 0) {
			replyId = 0;
		} else {
			replyId = 1;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "What would you like to buy?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		WorldObjectContainer demands = target.getProperty(Constants.DEMANDS);
		StringBuilder demandsBuilder = new StringBuilder();
		for(int i=0; i<demands.size(); i++) {
			demandsBuilder.append(demands.get(i).getProperty(Constants.NAME));
			if (i < demands.size() - 1) {
				demandsBuilder.append(", ");
			}
		}
		return Arrays.asList(
			new Response(0, "I'd like to buy " + demandsBuilder.toString()),
			new Response(1, "I'm not looking for anything to buy right now")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
}
