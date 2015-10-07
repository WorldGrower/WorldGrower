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

public class OrganizationConversation implements Conversation {

	private static final int MY_GROUP = 0;
	private static final int NO_GROUP = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		final int replyId;
		if (target.getProperty(Constants.GROUP).size() > 0) {
			replyId = MY_GROUP;
		} else {
			replyId = NO_GROUP;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, "What organizations do you belong to?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		StringBuilder organizationsBuilder = new StringBuilder();
		IdList organizations = target.getProperty(Constants.GROUP);
		if (organizations.size() > 0) {
			for(int i=0; i<organizations.getIds().size(); i++) {
				int organizationId = organizations.getIds().get(i);
				WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
				organizationsBuilder.append("the ").append(organization.getProperty(Constants.NAME));
				if (i < organizations.getIds().size() - 1) {
					organizationsBuilder.append(", ");
				}
			}
		} else {
			organizationsBuilder.append("no organization");
		}
		
		return Arrays.asList(
			new Response(MY_GROUP, "I belong to " + organizationsBuilder.toString()),
			new Response(NO_GROUP, "I don't belong to any organizations")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about organization membership";
	}
}
