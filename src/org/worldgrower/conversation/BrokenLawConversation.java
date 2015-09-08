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
import org.worldgrower.history.HistoryItem;

public class BrokenLawConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = 0;
		//TODO: implement
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "You are accused of the following crime: " + questionHistoryItem.getSecondPersonDescription(world)));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		int bounty = 50; //questionHistoryItem
		List<Response> responses = new ArrayList<>();
		responses.add(
				new Response(0, "I will pay the bounty", bounty <= conversationContext.getTarget().getProperty(Constants.GOLD)));
		responses.addAll(Arrays.asList(
				new Response(1, "I will spend my time in jail"),
				new Response(2, "I resist arrest")));
		
		return responses;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == 0) {
			conversationContext.getTarget().increment(Constants.GOLD, -50);
		} else if (replyIndex == 1) {
			//TODO: implement deteriorating skills
		} else if (replyIndex == 2) {
			//TODO: implement performer becoming hostile
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return false;
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "accusing " + target.getProperty(Constants.NAME) + " of a crime";
	}
}
