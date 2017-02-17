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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class BreakupWithMateConversation implements Conversation {
	private static final int BREAKUP_RELATIONSHIP_PENALTY = -500; 
	
	private static final int OK = 0;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = OK;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(new Question(Text.QUESTION_BREAKUP));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(OK, Text.ANSWER_BREAKUP_YES)
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
			breakup(performer, target, world);
		}
	}

	public void breakup(WorldObject performer, WorldObject target, World world) {
		RelationshipPropertyUtils.changeRelationshipValue(performer, target, BREAKUP_RELATIONSHIP_PENALTY, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		if (performer.getProperty(Constants.MATE_ID).intValue() == target.getProperty(Constants.ID).intValue()) {
			performer.setProperty(Constants.MATE_ID, null);
		}
		if (target.getProperty(Constants.MATE_ID).intValue() == performer.getProperty(Constants.ID).intValue()) {
			target.setProperty(Constants.MATE_ID, null);
		}
		
		world.logAction(Actions.TALK_ACTION, performer, target, Args.EMPTY, performer.getProperty(Constants.NAME) + " and " + target.getProperty(Constants.NAME) + " are no longer mates");
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking to my mate about breaking up";
	}
}
