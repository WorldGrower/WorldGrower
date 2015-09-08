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
import org.worldgrower.history.HistoryItem;

public class FamilyConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		if ((target.getProperty(Constants.MATE_ID) != null) || (target.getProperty(Constants.CHILDREN).size() > 0)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "Do you have a family?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		YesNoResponse yesNoResponse = getYesNoResponse(target, world);
		
		return Arrays.asList(
			new Response(YES, "Yes" + yesNoResponse.getYesResponse()),
			new Response(NO, "No" + yesNoResponse.getNoResponse())
			);
	}

	private YesNoResponse getYesNoResponse(WorldObject target, World world) {
		StringBuilder responseBuilder = new StringBuilder(", ");
		int numberOfYesResponses = 0;
		if (target.getProperty(Constants.MATE_ID) != null) {
			int mateId = target.getProperty(Constants.MATE_ID);
			WorldObject mate = world.findWorldObject(Constants.ID, mateId);
			responseBuilder.append("I have a mate named ").append(mate.getProperty(Constants.NAME));
			numberOfYesResponses++;
		} else {
			responseBuilder.append("I don't have a mate");
		}
		if (target.getProperty(Constants.CHILDREN).size() > 0) {
			responseBuilder.append(" and I have ").append(target.getProperty(Constants.CHILDREN).size()).append(" children");
			numberOfYesResponses++;
		} else {
			responseBuilder.append(" and I don't have children");
		}
		
		if (numberOfYesResponses > 0) {
			return new YesNoResponse(responseBuilder.toString(), "");
		} else {
			return new YesNoResponse("", responseBuilder.toString());
		}
	}
	
	private static class YesNoResponse {
		private final String yesResponse;
		private final String noResponse;
		
		private YesNoResponse(String yesResponse, String noResponse) {
			this.yesResponse = yesResponse;
			this.noResponse = noResponse;
		}

		public String getYesResponse() {
			return yesResponse;
		}

		public String getNoResponse() {
			return noResponse;
		}
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
		return "talking about my family";
	}
}
