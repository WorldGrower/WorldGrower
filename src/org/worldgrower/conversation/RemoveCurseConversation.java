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
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.TextId;

public class RemoveCurseConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int GET_LOST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (relationshipValue >= 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(
			new Question(TextId.QUESTION_REMOVE_CURSE)
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, TextId.ANSWER_REMOVE_CURSE_YES),
			new Response(NO, TextId.ANSWER_REMOVE_CURSE_NO),
			new Response(GET_LOST, TextId.ANSWER_REMOVE_CURSE_GETLOST));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			Actions.REMOVE_CURSE_ACTION.execute(target, performer, Args.EMPTY, world);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -70, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		Curse performerCurse = performer.getProperty(Constants.CURSE);
		return performerCurse != null && Curse.BESTOWABLE_CURSES.contains(performerCurse) 
				&& Actions.REMOVE_CURSE_ACTION.hasRequiredEnergy(target) && MagicSpellUtils.canCast(target, Actions.REMOVE_CURSE_ACTION);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "removing curse of " + target.getProperty(Constants.NAME);
	}
	
	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, GET_LOST, performer, target, world);
	}
}
