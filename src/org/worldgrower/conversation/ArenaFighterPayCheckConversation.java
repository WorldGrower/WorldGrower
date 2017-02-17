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
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.ArenaPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class ArenaFighterPayCheckConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		int payCheck = performer.getProperty(Constants.ARENA_PAY_CHECK_GOLD);
		int targetGold = target.getProperty(Constants.GOLD);
		if (targetGold >= payCheck) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(
			new Question(Text.QUESTION_ARENA_PAY_CHECK)
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		int payCheck = performer.getProperty(Constants.ARENA_PAY_CHECK_GOLD);
		return Arrays.asList(
			new Response(YES, Text.ANSWER_ARENA_PAY_CHECK_YES, payCheck, GOLD),
			new Response(NO, Text.ANSWER_ARENA_PAY_CHECK_NO));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == YES) {
			int payCheck = performer.getProperty(Constants.ARENA_PAY_CHECK_GOLD);
			
			performer.setProperty(Constants.ARENA_PAY_CHECK_GOLD, null);
			performer.increment(Constants.GOLD, payCheck);
			target.increment(Constants.GOLD, -payCheck);
		} else if (replyIndex == NO) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return ArenaPropertyUtils.worldObjectOwnsArena(target)
				&& ArenaPropertyUtils.personIsArenaFighter(performer, target)
				&& ArenaPropertyUtils.personCanCollectPayCheck(performer);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "looking for arena fight paycheck";
	}
}
