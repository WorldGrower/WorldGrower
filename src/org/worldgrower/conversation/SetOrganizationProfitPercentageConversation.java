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
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class SetOrganizationProfitPercentageConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = YES;		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		List<Question> questions = new ArrayList<>();
		
		for(WorldObject organization : organizations) {
			if (GroupPropertyUtils.canChangeLeaderOfOrganization(organization)) {
				if (!isVillagersOrganization(organization, world)) {
					if (target.getProperty(Constants.GROUP).contains(organization)) {
						for(int profitPercentage = -100; profitPercentage<=100; profitPercentage+=50) {
							questions.add(new Question(organization, "I'd like to set the profit percentage for " + organization.getProperty(Constants.NAME) + " to " + profitPercentage + "%, can you take care of this?", profitPercentage));
						}		
					}
				}
			}
		}

		return questions;
	}
	
	private boolean isVillagersOrganization(WorldObject organization, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		return organization.equals(villagersOrganization);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject organization = conversationContext.getSubject();
		int profitPercentage = conversationContext.getAdditionalValue();
		
		return Arrays.asList(
			new Response(YES, "Yes, I'll set the profit percentage for the " + organization.getProperty(Constants.NAME) + " to " + profitPercentage + "%"),
			new Response(NO, "No")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == YES) {
			WorldObject organization = conversationContext.getSubject();
			World world = conversationContext.getWorld();
			int profitPercentage = conversationContext.getAdditionalValue();
			
			List<WorldObject> members = GroupPropertyUtils.findOrganizationMembers(organization, world);
			
			for(WorldObject member : members) {
				member.setProperty(Constants.PROFIT_PERCENTAGE, profitPercentage);
			}
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about setting the profit percentage for an organization";
	}
}
