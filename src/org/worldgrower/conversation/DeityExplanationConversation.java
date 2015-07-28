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
import org.worldgrower.goal.DeityPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class DeityExplanationConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		WorldObject targetDeity = target.getProperty(Constants.DEITY);
		
		final int replyId;
		if (targetDeity == subject) {
			replyId = 0;
		} else {
			replyId = 1;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		WorldObject deity = DeityPropertyUtils.findDeity("Demeter", world);
		return Arrays.asList(new Question(deity, "What can you tell me about " + deity.getProperty(Constants.NAME)));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject subject = conversationContext.getSubject();
		return Arrays.asList(
			new Response(0, subject, subject.getProperty(Constants.NAME) + " is the God of harvest, sacred laws and life and death."),
			new Response(1, subject, "I don't know more about " + subject.getProperty(Constants.NAME))
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about my deity";
	}
}
