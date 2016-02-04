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
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class BreakupWithMateConversation implements Conversation {

	private static final int OK = 0;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = OK;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(new Question(null, "I want to break up with you"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(OK, "Ok, I'll respect your wishes")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return performer.getProperty(Constants.MATE_ID) != null;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == OK) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -500, Actions.TALK_ACTION, Conversations.createArgs(this), world);
			breakup(performer, target, world);
		}
	}

	private void breakup(WorldObject performer, WorldObject target, World world) {
		if (performer.getProperty(Constants.MATE_ID).intValue() == target.getProperty(Constants.ID).intValue()) {
			performer.setProperty(Constants.MATE_ID, null);
		}
		if (target.getProperty(Constants.MATE_ID).intValue() == performer.getProperty(Constants.ID).intValue()) {
			target.setProperty(Constants.MATE_ID, null);
		}
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking to my mate about breaking up";
	}
}
