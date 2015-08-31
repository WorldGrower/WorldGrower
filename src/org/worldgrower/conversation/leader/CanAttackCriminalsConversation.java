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
package org.worldgrower.conversation.leader;

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.ConversationContext;
import org.worldgrower.conversation.Question;
import org.worldgrower.conversation.Response;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class CanAttackCriminalsConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = YES;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "I'd like permission to attack criminals and uphold the law. In short, I want to be a sheriff. Is that ok?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, "Yes, you may become a sheriff"),
			new Response(NO, "No, you may not become a sheriff")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		boolean canAttackCriminals = (performer.hasProperty(Constants.CAN_ATTACK_CRIMINALS)) && (performer.getProperty(Constants.CAN_ATTACK_CRIMINALS));
		return (!canAttackCriminals && (GroupPropertyUtils.performerIsLeaderOfVillagers(target, world)));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		
		if (replyIndex == YES) {
			performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about permission to attack criminals";
	}
}
