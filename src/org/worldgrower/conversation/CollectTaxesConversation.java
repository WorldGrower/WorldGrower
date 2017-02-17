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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.HousePropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class CollectTaxesConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		if (targetCanPay(target, world)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}
	
	private boolean targetCanPay(WorldObject target, World world) {
		int amountToCollect = GroupPropertyUtils.getAmountToCollect(target, world);
		return (target.getProperty(Constants.GOLD) >= amountToCollect);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		int amountToCollect = GroupPropertyUtils.getAmountToCollect(target, world);
		return Arrays.asList(new Question(Text.QUESTION_COLLECT_TAXES, amountToCollect));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		List<Response> responses = new ArrayList<>();
		if (targetCanPay(target, world)) {
			responses.add(new Response(YES, Text.ANSWER_COLLECT_TAXES_YES));
		}
		responses.add(new Response(NO, Text.ANSWER_COLLECT_TAXES_NO));
		
		return responses;
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		boolean canCollectTaxes = (performer.hasProperty(Constants.CAN_COLLECT_TAXES)) && (performer.getProperty(Constants.CAN_COLLECT_TAXES));
		return (canCollectTaxes && (GroupPropertyUtils.getAmountToCollect(target, world) > 0));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		WorldObject organization = GroupPropertyUtils.getVillagersOrganization(world);
		
		if (replyIndex == YES) {
			int amountToCollect = GroupPropertyUtils.getAmountToCollect(target, world);
			target.increment(Constants.GOLD, -amountToCollect);
			performer.increment(Constants.ORGANIZATION_GOLD, amountToCollect);
			
		} else if (replyIndex == NO) {
			seizeAssets(target, world);
		}
		adjustTaxesPaid(target, world, organization);
	}

	void seizeAssets(WorldObject target, World world) {
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		if (leader != null) {
			List<Integer> buildingIds = target.getProperty(Constants.BUILDINGS).getIds(BuildingType.SHACK, BuildingType.HOUSE);
			
			for(int buildingId : buildingIds) {
				HousePropertyUtils.transferProperty(buildingId, target, leader, world);
			}
			world.getWorldStateChangedListeners().fireAssetsSeized(target, buildingIds);
		}
	}

	private void adjustTaxesPaid(WorldObject target, World world, WorldObject organization) {
		IdMap taxesPaidTurn = organization.getProperty(Constants.TAXES_PAID_TURN);
		taxesPaidTurn.remove(target);
		taxesPaidTurn.incrementValue(target, world.getCurrentTurn().getValue());
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about taxes to collect";
	}

	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, performer, target, world);
	}
}
