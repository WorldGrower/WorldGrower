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

public class KissConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int ALREADY_ASKED_SAME = 2;
	private static final int ALREADY_ASKED_DIFFERENT = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		if (historyItems.size() == 0) {
			if (targetAccepts(target, performer)) {
				replyId = YES;
			} else {
				replyId = NO;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			boolean targetAcceptedInPast = targetAccepts(lastHistoryItem.getOperationInfo().getTarget(), performer);
			boolean targetAcceptsNow = targetAccepts(target, performer);
			if (targetAcceptedInPast == targetAcceptsNow) {
				replyId = ALREADY_ASKED_SAME;
			} else {
				replyId = ALREADY_ASKED_DIFFERENT;
			}
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private boolean targetAccepts(WorldObject target, WorldObject performer) {
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		return relationshipValue > 0;
	}
	
	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "May I kiss you?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		boolean targetAccepts = targetAccepts(target, performer);
		
		return Arrays.asList(
			new Response(YES, "Yes"),
			new Response(NO, "No"),
			new Response(ALREADY_ASKED_SAME, "My answer is still the same as the last time you asked, " + (targetAccepts ? "yes" : "no")),
			new Response(ALREADY_ASKED_DIFFERENT, "This time my answer is " + (targetAccepts ? "yes" : "no"))
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about kissing";
	}
}
