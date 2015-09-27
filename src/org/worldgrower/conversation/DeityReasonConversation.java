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
import org.worldgrower.deity.Deity;
import org.worldgrower.history.HistoryItem;

public class DeityReasonConversation implements Conversation {

	private static final int SEEMED_LIKE = Integer.MAX_VALUE;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity deity = target.getProperty(Constants.DEITY);
		String reason = target.getProperty(Constants.REASONS).getReason(Constants.DEITY);
		
		
		final int replyId;
		if (reason != null) {
			replyId = deity.getReasons().indexOf(reason);
		} else {
			replyId = SEEMED_LIKE;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		Deity deity = target.getProperty(Constants.DEITY);
		return Arrays.asList(new Question(null, "Why did you choose to follow " + deity.getName() + " ?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity deity = target.getProperty(Constants.DEITY);
		List<String> reasons = deity.getReasons();
		
		List<Response> responses = new ArrayList<>();
		int responseId = 0;
		for (String reason : reasons) {
			responses.add(new Response(responseId, reason));
			responseId++;
		}
		responses.add(new Response(SEEMED_LIKE, "I don't care that much about which deity to follow, it seemed a good idea at the time"));
		return responses;
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return target.hasProperty(Constants.DEITY);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about the reasons I follow my deity";
	}
}
