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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class PayBountyConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		if (target.getProperty(Constants.GROUP).contains(villagersOrganization)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		int bounty = getBounty(target, world);
		return Arrays.asList(new Question(null, "I'm here to pay my bounty, " + bounty + " gold, what will you do?"));
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {

		List<Response> responses = new ArrayList<>();
		responses.addAll(Arrays.asList(
				new Response(YES, "Yes, your bounty is cleared"),
				new Response(NO, "No, I won't clear your bounty")));
		
		return responses;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			int bounty = getBounty(target, world);
			performer.increment(Constants.GOLD, -bounty);
			target.increment(Constants.GOLD, bounty);
			
			removeBounty(performer, world, bounty);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	private void removeBounty(WorldObject target, World world, int bounty) {
		GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY).incrementValue(target, -bounty);
	}

	private int getBounty(WorldObject target, World world) {
		IdMap bountyMap = GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY);
		int bounty = bountyMap.getValue(target);
		return bounty;
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		IdMap bountyMap = GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY);
		int performerGold = performer.getProperty(Constants.GOLD).intValue();
		return bountyMap.getValue(performer) > 0 && performerGold >= getBounty(performer, world);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "paying a bounty";
	}
}
