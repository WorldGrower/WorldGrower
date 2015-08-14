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
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.goal.HousePropertyUtils;
import org.worldgrower.history.HistoryItem;

public class BuyHouseConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		int replyId = YES;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "I'd like to buy a house"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(0, "yes"),
			new Response(1, "no")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return HousePropertyUtils.hasHouseForSale(target, world);
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			WorldObject house = HousePropertyUtils.getHouseForSale(target, world);
			int price = BuySellUtils.getPrice(target, house);
			
			performer.getProperty(Constants.HOUSES).add(house);
			target.getProperty(Constants.HOUSES).remove(house);
			house.setProperty(Constants.SELLABLE, Boolean.FALSE);
			
			target.increment(Constants.GOLD, price);
			performer.increment(Constants.GOLD, -price);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about selling a house";
	}
}
