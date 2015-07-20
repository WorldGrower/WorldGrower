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
import org.worldgrower.attribute.IdList;
import org.worldgrower.history.HistoryItem;

public class JoinOrganizationConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (relationshipValue > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdList performerOrganizations = performer.getProperty(Constants.GROUP);
		IdList targetOrganizations = target.getProperty(Constants.GROUP);
		List<Integer> organizationsToJoin = performerOrganizations.getIdsNotPresentInOther(targetOrganizations);
		
		List<Question> questions = new ArrayList<>();
		for(int organizationId : organizationsToJoin) {
			WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
			questions.add(new Question(organization, "Would you like to join the " + organization.getProperty(Constants.NAME) + " ?"));
		}
		
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject organization = conversationContext.getSubject();
		
		return Arrays.asList(
			new Response(YES, "Yes, I'll join the " + organization.getProperty(Constants.NAME)),
			new Response(NO, "No")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == YES) {
			WorldObject target = conversationContext.getTarget();
			WorldObject organization = conversationContext.getSubject();
			
			target.getProperty(Constants.GROUP).add(organization);
		}
	}
}
