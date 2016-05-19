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
import org.worldgrower.goal.BountyPropertyUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class CollectBountyFromThievesConversation implements Conversation {

	private static final int PAY_GOLD = 0;
	private static final int JAIL = 1;
	private static final int RESIST_ARREST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		int bounty = BountyPropertyUtils.getBounty(target, world);
		if (target.getProperty(Constants.GOLD).intValue() >= bounty) {
			replyId = PAY_GOLD;
		} else {
			replyId = JAIL;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		int bounty = BountyPropertyUtils.getBounty(target, world);
		return Arrays.asList(new Question(null, "I'm here to collect your bounty, " + bounty + " gold, what will you do?"));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		int bounty = BountyPropertyUtils.getBounty(target, world);
		List<Response> responses = new ArrayList<>();
		if (target.getProperty(Constants.GOLD).intValue() >= bounty) {
			responses.add(new Response(PAY_GOLD, "I will pay the bounty", bounty <= conversationContext.getTarget().getProperty(Constants.GOLD)));
		}
		responses.addAll(Arrays.asList(
				new Response(JAIL, "I will spend my time in jail"),
				new Response(RESIST_ARREST, "I resist arrest")));
		
		return responses;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == PAY_GOLD) {
			int bounty = BountyPropertyUtils.getBounty(target, world);
			target.increment(Constants.GOLD, -bounty);
			performer.increment(Constants.GOLD, bounty);
			
			removeBounty(target, world, bounty);
		} else if (replyIndex == JAIL) {
			//TODO: implement deteriorating skills
			int bounty = BountyPropertyUtils.getBounty(target, world);
			removeBounty(target, world, bounty);
		} else if (replyIndex == RESIST_ARREST) {
			target.getProperty(Constants.GROUP).remove(GroupPropertyUtils.getVillagersOrganization(world));
		}
	}

	private void removeBounty(WorldObject target, World world, int bounty) {
		GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY).incrementValue(target, -bounty);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return BountyPropertyUtils.getBounty(target, world) > 0 && BountyPropertyUtils.canForgiveBounty(performer);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "collecting the bounty for " + target.getProperty(Constants.NAME);
	}
}
