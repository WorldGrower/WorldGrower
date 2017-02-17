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
import org.worldgrower.text.Text;

public class DeityFollowersConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		Deity deity = Deity.getAllSortedDeities().get(conversationContext.getAdditionalValue());
		boolean targetHasDeity = (target.getProperty(Constants.DEITY) == deity);
		boolean hasOtherFollowers = getFollowersForDeity(conversationContext).size() > 0;
		boolean replyYes = targetHasDeity || hasOtherFollowers;
		
		final int replyId;
		if (relationshipValue >= 0 && replyYes) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		for(Deity deity : Deity.getAllSortedDeities()) {
			questions.add(new Question(Deity.getAllSortedDeities().indexOf(deity), Text.QUESTION_DEITY_FOLLOWERS, deity.getName()));
		}
		return questions;
	}
	
	private List<WorldObject> getFollowersForDeity(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity deity = Deity.getAllSortedDeities().get(conversationContext.getAdditionalValue());
		World world = conversationContext.getWorld();
		return target.getProperty(Constants.KNOWLEDGE_MAP).findWorldObjects(Constants.DEITY, deity, world);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, Text.ANSWER_DEITY_FOLLOWERS_YES, getFollowersDescription(conversationContext)),
			new Response(NO, Text.ANSWER_DEITY_FOLLOWERS_NO)
			);
	}

	private String getFollowersDescription(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		List<WorldObject> followersForDeity = getFollowersForDeity(conversationContext);
		Deity deity = Deity.getAllSortedDeities().get(conversationContext.getAdditionalValue());
		
		if (target.getProperty(Constants.DEITY) == deity) {
			return "I follow " + deity.getName();
		} else if (followersForDeity.size() == 1) {
			return "I know that " + followersForDeity.get(0).getProperty(Constants.NAME) + " is a worshipper of " + deity.getName();
		} else if (followersForDeity.size() > 0) {
			StringBuilder followersDescription = new StringBuilder();
			for(int i=0; i<followersForDeity.size(); i++) {
				WorldObject follower = followersForDeity.get(i);
				followersDescription.append(follower.getProperty(Constants.NAME));
				if (i < followersForDeity.size() - 1) {
					followersDescription.append(", ");
				}
			}
			return "I know that " + followersDescription.toString() + " are worshippers of " + deity.getName();
		} else {
			return "I know no-one that is a worshipper of " + deity.getName();
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
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
