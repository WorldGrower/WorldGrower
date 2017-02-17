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
import org.worldgrower.attribute.IdList;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class WhoIsLeaderOrganizationConversation implements Conversation {

	private static final int LEADER = 0;
	private static final int NONE_OF = 1;
	private static final int ALREADY_ASKED = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		if (givesAnswer(target, performer) && historyItems.size() == 0) {
			replyId = LEADER;
		} else if (givesAnswer(target, performer) && historyItems.size() > 0) {
			replyId = ALREADY_ASKED;
		} else {
			replyId = NONE_OF;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private boolean givesAnswer(WorldObject target, WorldObject performer) {
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		return relationshipValue >= 0;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		IdList targetOrganizations = target.getProperty(Constants.GROUP);
		
		List<Question> questions = new ArrayList<>();
		for(int organizationId : targetOrganizations.getIds()) {
			WorldObject organization = world.findWorldObjectById(organizationId);
			if (GroupPropertyUtils.canChangeLeaderOfOrganization(organization)) {
				questions.add(new Question(organization, Text.QUESTION_LEADER, organization.getProperty(Constants.NAME)));
			}
		}
		
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject organization = conversationContext.getSubject();
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		WorldObject leader = getLeader(organization, world);
		
		String leaderDescription = getLeaderDescription(organization, performer, target, leader);
		
		return Arrays.asList(
			new Response(LEADER, Text.ANSWER_LEADER_YES, leaderDescription),
			new Response(NONE_OF, Text.ANSWER_LEADER_NONE),
			new Response(ALREADY_ASKED, Text.ANSWER_LEADER_ALREADY, leaderDescription)
			);
	}

	private String getLeaderDescription(WorldObject organization, WorldObject performer, WorldObject target, WorldObject leader) {
		StringBuilder leaderBuilder = new StringBuilder();
		if (leader != null && leader.equals(target)) {
			leaderBuilder.append("I'm the leader of the ").append(organization.getProperty(Constants.NAME));
		} else if (leader != null && leader.equals(performer)) {
			leaderBuilder.append("You are the leader of the ").append(organization.getProperty(Constants.NAME));
		} else if (leader != null) {
			leaderBuilder.append(leader.getProperty(Constants.NAME)).append(" is the leader of the ").append(organization.getProperty(Constants.NAME));
		} else {
			leaderBuilder.append(organization.getProperty(Constants.NAME)).append(" has no leader at the moment");
		}
		return leaderBuilder.toString();
	}

	private WorldObject getLeader(WorldObject organization, World world) {
		Integer leaderId = organization.getProperty(Constants.ORGANIZATION_LEADER_ID);
		WorldObject leader = null;
		if (leaderId != null) {
			leader = world.findWorldObjectById(leaderId);
		}
		return leader;
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == LEADER || replyIndex == ALREADY_ASKED) {
			WorldObject performer = conversationContext.getPerformer();
			WorldObject organization = conversationContext.getSubject();
			World world = conversationContext.getWorld();
			WorldObject leader = getLeader(organization, world);
			
			if (leader != null) {
				performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, 0);
			}
		} else if (replyIndex == NONE_OF) {
			WorldObject performer = conversationContext.getPerformer();
			WorldObject target = conversationContext.getTarget();
			World world = conversationContext.getWorld();
			
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -10, -5, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about who leads an organization";
	}
}
