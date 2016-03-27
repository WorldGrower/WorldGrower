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
import org.worldgrower.deity.Deity;
import org.worldgrower.history.HistoryItem;

public class DeityConversation implements Conversation {

	private static final int I_WORSHIP = 0;
	private static final int I_DONT_WORSHIP = 1;
	private static final int ALREADY_ASKED = 2;
	private static final int DEITY_CHANGED = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject target = conversationContext.getTarget();
		final int replyId;
		Deity deity = target.getProperty(Constants.DEITY);
		if (historyItems.size() == 0) {
			if (deity != null) {
				replyId = I_WORSHIP;
			} else {
				replyId = I_DONT_WORSHIP;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			Deity deityInLastConversation = lastHistoryItem.getOperationInfo().getTarget().getProperty(Constants.DEITY);
			if (deity == deityInLastConversation) {
				replyId = ALREADY_ASKED;
			} else {
				replyId = DEITY_CHANGED;
			}
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "If you worshop a deity, which one?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity deity = target.getProperty(Constants.DEITY);
		String deityName = (deity != null ? deity.getName() : "no one");
		return Arrays.asList(
			new Response(I_WORSHIP, "I worship " + deityName),
			new Response(I_DONT_WORSHIP, "I don't worship a deity"),
			new Response(ALREADY_ASKED, "I still worship " + deityName),
			new Response(DEITY_CHANGED, "I now worship " + deityName)
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(target, Constants.DEITY, target.getProperty(Constants.DEITY));
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about which deity I worship";
	}
}
