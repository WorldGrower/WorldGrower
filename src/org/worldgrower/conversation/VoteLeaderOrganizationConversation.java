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
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.attribute.IdList;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class VoteLeaderOrganizationConversation implements Conversation {

	private static final int LETS_PUT = 0;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = LETS_PUT;
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdList targetOrganizations = target.getProperty(Constants.GROUP);
		
		List<Question> questions = new ArrayList<>();
		for(int organizationId : targetOrganizations.getIds()) {
			WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
			if (GroupPropertyUtils.canJoinOrChangeLeaderOfOrganization(organization)) {
				if (performer.getProperty(Constants.GROUP).contains(organization)) {
					boolean voteAlreadyInProgress = world.findWorldObjects(w -> VotingPropertyUtils.isVotingBoxForOrganization(w, organization)).size() > 0;
					if (!voteAlreadyInProgress) {
						questions.add(new Question(organization, "I want to vote on leadership for the " + organization.getProperty(Constants.NAME)));
					}
				}
			}
		}
		
		return questions;
	}
	

	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(LETS_PUT, "Let's put it to a vote. From now for " + VotingPropertyUtils.getNumberOfTurnsCandidatesMayBeProposed() + " turns anyone can become a candidate for leader, and after that voting starts.")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		IdList performerOrganizations = performer.getProperty(Constants.GROUP);
		IdList targetOrganizations = target.getProperty(Constants.GROUP);
		return performerOrganizations.intersects(targetOrganizations);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		WorldObject organization = conversationContext.getSubject();
		World world = conversationContext.getWorld();
		
		if (replyIndex == LETS_PUT) {
			VotingPropertyUtils.createVotingBox(target, organization, world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about who leads an organization";
	}
}
