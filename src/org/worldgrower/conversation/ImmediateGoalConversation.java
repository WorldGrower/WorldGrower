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

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.HistoryItem;

public class ImmediateGoalConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		Goal goal = world.getGoal(target);
		OperationInfo operationInfo = world.getImmediateGoal(target, world);

		if ((goal != null) && (operationInfo != null)) {
			replyId = 0;
		} else {
			replyId = 1;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(new Question(null, "What are you doing?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		Goal goal = world.getGoal(target);
		OperationInfo operationInfo = world.getImmediateGoal(target, world);
		String immediateGoalDescription = (operationInfo != null ? operationInfo.getDescription(world) : "");
		String goalDescription = (goal != null ? goal.getDescription() : "");
		return Arrays.asList(
			new Response(0, "I'm " + immediateGoalDescription + " because I'm " + goalDescription),
			new Response(1, "I'm not doing anything")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about what I'm doing";
	}
}
