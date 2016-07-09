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
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class SwitchDeityConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int GET_LOST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		final int replyId;
		if (relationshipValue > 500) {
			replyId = YES;
		} else if (relationshipValue > -500) {
			replyId = NO;
		} else {
			replyId = GET_LOST;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		
		List<Question> questions = new ArrayList<>();
		if (performerDeity != null) {
			questions.add(new Question(null, "Would you like to worship " + performerDeity.getName() + " instead of your current deity?"));
		}
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		Deity targetDeity = target.getProperty(Constants.DEITY);
		return Arrays.asList(
			new Response(YES, "Yes, I'll worship " + performerDeity.getName() + " instead of " + targetDeity.getName() + "."),
			new Response(NO, "No"),
			new Response(GET_LOST, "Get lost")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return performer.getProperty(Constants.DEITY) != null
				&& target.getProperty(Constants.DEITY) != null
				&& performer.getProperty(Constants.DEITY) != target.getProperty(Constants.DEITY);
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			target.setProperty(Constants.DEITY, performer.getProperty(Constants.DEITY));
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -100, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about switching deities";
	}
	
	public boolean previousAnswerWasGetLost(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, GET_LOST, performer, target, world);
	}
}
