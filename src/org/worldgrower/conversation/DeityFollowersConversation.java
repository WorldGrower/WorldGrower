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
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class DeityFollowersConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if (relationshipValue >= 0 && getFollowersForDeity(conversationContext).size() > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		List<Question> questions = new ArrayList<>();
		for(Deity deity : Deity.ALL_DEITIES) {
			questions.add(new Question(null, "Do you know any people who worship " + deity.getName() + "?", Deity.ALL_DEITIES.indexOf(deity)));
		}
		return questions;
	}
	
	private List<WorldObject> getFollowersForDeity(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity deity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		World world = conversationContext.getWorld();
		return target.getProperty(Constants.KNOWLEDGE_MAP).findWorldObjects(Constants.DEITY, deity, world);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		String followersDescription = getFollowersDescription(conversationContext);
		
		Deity deity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		return Arrays.asList(
			new Response(YES, "I know that " + followersDescription + " are worshippers of " + deity.getName()),
			new Response(NO, "No")
			);
	}

	private String getFollowersDescription(ConversationContext conversationContext) {
		List<WorldObject> followersForDeity = getFollowersForDeity(conversationContext);
		
		StringBuilder followersDescription = new StringBuilder();
		for(WorldObject follower : followersForDeity) {
			followersDescription.append(follower.getProperty(Constants.NAME)).append(", ");
		}
		return followersDescription.toString();
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		RelationshipPropertyUtils.changeRelationshipValue(performer, target, 10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about the followers of deities";
	}
}
