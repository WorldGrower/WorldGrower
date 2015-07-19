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
import org.worldgrower.history.HistoryItem;

public class WhyAngryConversation implements Conversation {

	private static final int REAL_REASON = 0;
	private static final int GET_LOST = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = REAL_REASON;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(
			new Question(null, "Why are you angry with me?")
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		List<String> angryReasons = target.getProperty(Constants.BACKGROUND).getAngryReasons(true, performer, world);
		
		StringBuilder angryReasonBuilder = new StringBuilder();
		for(String angryReason : angryReasons) {
			angryReasonBuilder.append(angryReason).append("; ");
		}
		
		return Arrays.asList(
			new Response(REAL_REASON, angryReasonBuilder.toString()),
			new Response(GET_LOST, "Get lost")
		);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		if (replyIndex == REAL_REASON) {
			performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 10);
			target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 10);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		return relationshipValue < 0;
	}
}
