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
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class CureDiseaseConversation implements Conversation {

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
			new Question(null, "Can you cure my diseases?")
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, "Yes"),
			new Response(NO, "No"),
			new Response(GET_LOST, "Get lost"));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			Actions.CURE_DISEASE_ACTION.execute(target, performer, Args.EMPTY, world);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		boolean performerIsDiseased = performer.getProperty(Constants.CONDITIONS).hasDiseaseCondition();
		return performerIsDiseased && Actions.CURE_DISEASE_ACTION.hasRequiredEnergy(target) && MagicSpellUtils.canCast(target, Actions.CURE_DISEASE_ACTION);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "getting diseases cured";
	}
	
	public boolean previousAnswerWasGetLost(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, GET_LOST, performer, target, world);
	}
}
