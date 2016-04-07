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

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.history.HistoryItem;

/**
 * A conversation describes a dialogue between a performer and a target.
 * It describes possible questions, possible answers and the logic to select a correct answer for an npc.
 */
public interface Conversation extends Serializable {
	public Response getReplyPhrase(ConversationContext conversationContext);
	
	// number of replyphrases should remain the same: otherwise replyIndex doesn't work ==> response.isPossible flag
	public List<Response> getReplyPhrases(ConversationContext conversationContext);
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world);
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world);
	
	public default List<HistoryItem> findSameConversation(ConversationContext conversationContext) {
		World world = conversationContext.getWorld();
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		return world.getHistory().findHistoryItemsForAnyPerformer(performer, target, Conversations.createArgs(this, subject), Actions.TALK_ACTION);
	}
	
	public default boolean additionalValuesValid(WorldObject performer, WorldObject target, int additionalValue, int additionalValue2, World world) {
		return true;
	}
	
	public default Response getReply(List<Response> replyPhrases, int replyId) {
		for(Response response : replyPhrases) {
			if (response.getId() == replyId) {
				return response;
			}
		}
		throw new IllegalStateException("replyId " + replyId + " not found in responses: " + replyPhrases);
	}
	
	public default List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return null;
	}
	
	public void handleResponse(int replyIndex, ConversationContext conversationContext);

	public String getDescription(WorldObject performer, WorldObject target, World world);
	
	public default List<Integer> getPreviousResponseIds(WorldObject performer, WorldObject target, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItemsForAnyPerformer(performer, target, Conversations.createArgs(this), Actions.TALK_ACTION);
		return historyItems.stream().map(h -> (Integer)h.getAdditionalValue()).collect(Collectors.toList());
	}
	
	/*
	public default int getNumberOfTurnsSinceSameConversation(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = findSameConversation(conversationContext);
		if (historyItems.size() > 0) {
			return historyItems.get(historyItems.size() - 1).getTurn().getValue();
		} else {
			return Integer.MAX_VALUE;
		}
	}*/
}