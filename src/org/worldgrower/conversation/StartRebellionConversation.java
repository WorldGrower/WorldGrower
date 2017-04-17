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
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.TextId;

public class StartRebellionConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int GET_LOST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		final int replyId;
		int leaderRelationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(leader);
		int performerRelationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (performerRelationshipValue < 0) {
			replyId = GET_LOST;
		} else if (Constants.RELATIONSHIP_VALUE.isAtMin(leaderRelationshipValue) && !Constants.RELATIONSHIP_VALUE.isAtMin(performerRelationshipValue)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		List<Question> questions = new ArrayList<>();
		
		String organizationName = subject.getProperty(Constants.NAME);
		questions.add(new Question(subject, TextId.QUESTION_START_REBELLION, organizationName));
		return questions;
	}
	
	@Override
	public List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(GroupPropertyUtils.getVillagersOrganization(world));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, TextId.ANSWER_START_REBELLION_YES),
			new Response(NO, TextId.ANSWER_START_REBELLION_NO),
			new Response(GET_LOST, TextId.ANSWER_START_REBELLION_GET_LOST)
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return GroupPropertyUtils.getLeaderOfVillagers(world) != null;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		if (replyIndex == YES) {
			WorldObject organization = conversationContext.getSubject();
			organization.getProperty(Constants.ORGANIZATION_REBEL_IDS).addUnique(performer);
			organization.getProperty(Constants.ORGANIZATION_REBEL_IDS).addUnique(target);	
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -100, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about starting a rebellion";
	}

	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, GET_LOST, performer, target, world);
	}
}
