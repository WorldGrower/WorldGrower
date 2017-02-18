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
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.TextId;

public class NameConversation implements Conversation {

	private static final int GET_LOST = 4;
	private static final int MY_NAME = 0;
	private static final int TOLD_WHILE = 1;
	private static final int LIKE_I_TOLD = 2;
	private static final int MY_NAME_DIDNT = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if (relationshipValue < 0) {
			replyId = GET_LOST;
		} else if (historyItems.size() == 0) {
			replyId = MY_NAME;
		} else if (historyItems.size() == 1) {
			replyId = TOLD_WHILE;
		} else if (historyItems.size() < 4) {
			replyId = LIKE_I_TOLD;
		} else {
			replyId = MY_NAME_DIDNT;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(TextId.QUESTION_NAME));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		return Arrays.asList(
			new Response(MY_NAME, TextId.ANSWER_NAME, target.getProperty(Constants.NAME)),
			new Response(GET_LOST, TextId.ANSWER_NAME_GETLOST),
			new Response(TOLD_WHILE, TextId.ANSWER_NAME_TOLD_WHILE, target.getProperty(Constants.NAME)),
			new Response(LIKE_I_TOLD, TextId.ANSWER_NAME_LIKE, target.getProperty(Constants.NAME)),
			new Response(MY_NAME_DIDNT, TextId.ANSWER_NAME_DIDNT, target.getProperty(Constants.NAME))
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
		World world = conversationContext.getWorld();
		
		if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -100, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about my name";
	}
}
