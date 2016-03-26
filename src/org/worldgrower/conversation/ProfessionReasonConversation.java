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
import org.worldgrower.attribute.Reasons;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

public class ProfessionReasonConversation implements Conversation {

	private static final int REASON = 0;
	private static final int NO_PROFESSION = 1;
	private static final int STILL_THE_SAME = 2;
	private static final int NEW_PROFESSION = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject target = conversationContext.getTarget();
		Profession targetProfession = target.getProperty(Constants.PROFESSION);
		Reasons reasons = target.getProperty(Constants.REASONS);
		String reason = reasons.getReason(Constants.PROFESSION);
		
		final int replyId;
		if (historyItems.size() == 0) {
			if (reason != null) {
				replyId = REASON;
			} else {
				replyId = NO_PROFESSION;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			Profession professionInLastConversation = lastHistoryItem.getOperationInfo().getTarget().getProperty(Constants.PROFESSION);
			if (targetProfession == professionInLastConversation) {
				replyId = STILL_THE_SAME;
			} else {
				replyId = NEW_PROFESSION;
			}
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "Why did you choose your profession?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		
		Reasons reasons = target.getProperty(Constants.REASONS);
		String reason = reasons.getReason(Constants.PROFESSION);
		return Arrays.asList(
			new Response(REASON, reason),
			new Response(NO_PROFESSION, "I don't have a profession"),
			new Response(STILL_THE_SAME, "It's still the same as the last time you asked, " + (reason != null ? reason : "I don't have a profession")),
			new Response(NEW_PROFESSION, "Like I said before, " + reason)
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
		return "talking about my reasons for choosing my profession";
	}
}
