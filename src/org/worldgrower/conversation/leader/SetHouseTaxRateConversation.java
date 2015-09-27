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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.ConversationContext;
import org.worldgrower.conversation.Question;
import org.worldgrower.conversation.Response;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class SetHouseTaxRateConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = YES;		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		int[] houseTaxRates = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int currentHouseTaxRate = GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.HOUSE_TAX_RATE);
		
		List<Question> questions = new ArrayList<>();
		for(int newHouseTaxRate : houseTaxRates) {
			if (newHouseTaxRate != currentHouseTaxRate) {
				questions.add(new Question(null, "I want to change the house tax rate from " + currentHouseTaxRate + " to " + newHouseTaxRate + " gold pieces per 100 turns", newHouseTaxRate));
			}
		}
		
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, "Ok"),
			new Response(NO, "That's not possible")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		World world = conversationContext.getWorld();
		int newShackTaxRate = conversationContext.getAdditionalValue();
		
		if (replyIndex == YES) {
			GroupPropertyUtils.getVillagersOrganization(world).setProperty(Constants.HOUSE_TAX_RATE, newShackTaxRate);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about changing the tax rate for houses";
	}
}
