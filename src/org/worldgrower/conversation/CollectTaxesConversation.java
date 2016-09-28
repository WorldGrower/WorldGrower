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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

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
		return Arrays.asList(new Question(null, "I'm here to collect your taxes. The taxes are " + amountToCollect + " gold. Will you pay your taxes?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		List<Response> responses = new ArrayList<>();
		if (targetCanPay(target, world)) {
			responses.add(new Response(YES, "Yes, I'll pay my taxes"));
		}
		responses.add(new Response(NO, "No, I won't pay my taxes"));
		
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
			
			IdMap taxesPaidTurn = organization.getProperty(Constants.TAXES_PAID_TURN);
			taxesPaidTurn.remove(target);
			taxesPaidTurn.incrementValue(target, world.getCurrentTurn().getValue());
		} else if (replyIndex == NO) {
			//TODO: organization sells house
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about taxes to collect";
	}
}
