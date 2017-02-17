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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class OrganizationConversation implements Conversation {

	private static final int MY_GROUP = 0;
	private static final int NO_GROUP = 1;
	private static final int ALREADY_ASKED_SAME = 2;
	private static final int ALREADY_ASKED_DIFFERENT = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getTarget();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		List<HistoryItem> historyItems = findSameConversation(conversationContext);
		final int replyId;
		if (historyItems.size() == 0) {
			if (target.getProperty(Constants.GROUP).size() > 0) {
				replyId = MY_GROUP;
			} else {
				replyId = NO_GROUP;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			if (organizationDesciptionRemainsSame(lastHistoryItem, target, world)) {
				replyId = ALREADY_ASKED_SAME;
			} else {
				replyId = ALREADY_ASKED_DIFFERENT;
			}
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}
	
	private boolean organizationDesciptionRemainsSame(HistoryItem lastHistoryItem, WorldObject target, World world) {
		String organizationsDescription = getOrganizationsDescription(target, world);
		String lastOrganizationsDescription = (String)lastHistoryItem.getAdditionalValue();
		return organizationsDescription.equals(lastOrganizationsDescription);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(Text.QUESTION_ORG));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		String organizationsDescription = getOrganizationsDescription(target, world);
		
		return Arrays.asList(
			new Response(MY_GROUP, Text.ANSWER_ORG_GROUP, organizationsDescription),
			new Response(NO_GROUP, Text.ANSWER_ORG_NO_GROUP),
			new Response(ALREADY_ASKED_SAME, Text.ANSWER_ORG_SAME, organizationsDescription),
			new Response(ALREADY_ASKED_DIFFERENT, Text.ANSWER_ORG_DIFFERENT, organizationsDescription)
			);
	}

	private String getOrganizationsDescription(WorldObject target, World world) {
		StringBuilder organizationsBuilder = new StringBuilder();
		IdList organizations = target.getProperty(Constants.GROUP);
		if (organizations.size() > 0) {
			for(int i=0; i<organizations.getIds().size(); i++) {
				int organizationId = organizations.getIds().get(i);
				WorldObject organization = world.findWorldObjectById(organizationId);
				organizationsBuilder.append("the ").append(organization.getProperty(Constants.NAME));
				if (i == organizations.getIds().size() - 2) {
					organizationsBuilder.append(" and ");
				} else if (i < organizations.getIds().size() - 1) {
					organizationsBuilder.append(", ");
				}
			}
		} else {
			organizationsBuilder.append("no organization");
		}
		return organizationsBuilder.toString();
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		World world = conversationContext.getWorld();
		WorldObject target = conversationContext.getTarget();
		String organizationsDescription = getOrganizationsDescription(target, world);
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(organizationsDescription);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about organization membership";
	}
}
