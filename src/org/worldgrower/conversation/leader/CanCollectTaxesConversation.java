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
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.ConversationContext;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.PreviousResponseIdUtils;
import org.worldgrower.conversation.QueryableConversation;
import org.worldgrower.conversation.Question;
import org.worldgrower.conversation.Response;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.ProfessionPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Professions;
import org.worldgrower.text.TextId;

public class CanCollectTaxesConversation implements QueryableConversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId;
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		boolean canBecomeEmployee = EmployeeUtils.canBecomePublicEmployee(performer, target, world);
		if (canBecomeEmployee) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(TextId.QUESTION_CAN_COLLECT_TAXES));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, TextId.ANSWER_CAN_COLLECT_TAXES_YES),
			new Response(NO, TextId.ANSWER_CAN_COLLECT_TAXES_NO)
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		boolean canCollectTaxes = (performer.hasProperty(Constants.CAN_COLLECT_TAXES)) && (performer.getProperty(Constants.CAN_COLLECT_TAXES));
		return (!canCollectTaxes && (GroupPropertyUtils.performerIsLeaderOfVillagers(target, world)));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			ProfessionPropertyUtils.enableTaxCollecting(performer, world);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		if (performer.getProperty(Constants.PROFESSION) == null) {
			performer.setProperty(Constants.PROFESSION, Professions.TAX_COLLECTOR_PROFESSION);
		}
		performer.removeProperty(Constants.WANTED_PROFESSION);
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about permission to collect taxes";
	}
	
	@Override
	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, performer, target, world);
	}
}
