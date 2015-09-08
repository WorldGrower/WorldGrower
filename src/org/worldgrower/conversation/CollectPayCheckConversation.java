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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

public class CollectPayCheckConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
		
		final int replyId;
		if (target.getProperty(Constants.ORGANIZATION_GOLD) >= amountToCollect) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
		return Arrays.asList(new Question(null, "I'm here to collect my pay check of " + amountToCollect + " gold. Will you pay?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, "Yes, I'll pay your pay check"),
			new Response(NO, "No, I won't pay your pay check")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		return ((performerProfession != null) && (performerProfession.isPaidByVillagerLeader()) && (GroupPropertyUtils.getPayCheckAmount(performer, world) > 0));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		WorldObject organization = GroupPropertyUtils.getVillagersOrganization(world);
		
		if (replyIndex == YES) {
			int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
			target.increment(Constants.ORGANIZATION_GOLD, -amountToCollect);
			performer.increment(Constants.GOLD, amountToCollect);
			
			IdMap payCheckPaidTurn = organization.getProperty(Constants.PAY_CHECK_PAID_TURN);
			payCheckPaidTurn.remove(performer);
			payCheckPaidTurn.incrementValue(performer, world.getCurrentTurn().getValue());
		} else if (replyIndex == NO) {
			//TODO: rebellion?
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about collecting my pay check";
	}
}
