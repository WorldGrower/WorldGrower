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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.goal.HousePropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.util.SentenceUtils;

public class SellBuildingConversation implements Conversation {

	private final BuildingType buildingType;
	
	private static final int YES = 0;
	private static final int NO = 1;
	
	public SellBuildingConversation(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		int buildingId = performer.getProperty(Constants.BUILDINGS).getIds(buildingType).get(0);
		WorldObject building = world.findWorldObjectById(buildingId);
		
		boolean targetWillBuy = BuySellUtils.buyerWillBuyGoods(performer, target, building, world);
		
		final int replyId;
		if (targetWillBuy) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		String description = buildingType.getDescription();
		return Arrays.asList(new Question(null, "Do you want to buy " + SentenceUtils.getArticle(description) + " " + description + "?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, "Yes"),
			new Response(NO, "No")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return HousePropertyUtils.hasBuildings(performer, buildingType);
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			int buildingId = performer.getProperty(Constants.BUILDINGS).getIds(buildingType).get(0);
			Item buildingTypeItem = Item.mapBuildingTypeToItem(buildingType);
			int price = performer.getProperty(Constants.PRICES).getPrice(buildingTypeItem);
			
			HousePropertyUtils.transferProperty(buildingId, performer, target, world);
			
			target.increment(Constants.GOLD, -price);
			performer.increment(Constants.GOLD, price);
		}
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about selling a " + buildingType.getDescription();
	}
}
