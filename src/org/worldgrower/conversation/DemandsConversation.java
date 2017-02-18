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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.TextId;

public class DemandsConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		final int replyId;
		PropertyCountMap<ManagedProperty<?>> demands = target.getProperty(Constants.DEMANDS);
		if (demands.size() > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(TextId.QUESTION_DEMANDS));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		PropertyCountMap<ManagedProperty<?>> demands = target.getProperty(Constants.DEMANDS);
		StringBuilder demandsBuilder = new StringBuilder();
		List<ManagedProperty<?>> demandsList = getDemandsList(demands);
		for(int i=0; i<demandsList.size(); i++) {
			demandsBuilder.append(demandsList.get(i).getName());
			if ((demands.size() > 1) && (i == demands.size() - 2)) {
				demandsBuilder.append(" and ");
			} else if (i < demands.size() - 1) {
				demandsBuilder.append(", ");
			}
		}
		return Arrays.asList(
			new Response(YES, TextId.ANSWER_DEMANDS_YES, demandsBuilder.toString()),
			new Response(NO,  TextId.ANSWER_DEMANDS_NO)
			);
	}

	private List<ManagedProperty<?>> getDemandsList(PropertyCountMap<ManagedProperty<?>> demands) {
		List<ManagedProperty<?>> demandsList = new ArrayList<>(demands.keySet());
		Collections.sort(demandsList, new DemandsComparator());
		return demandsList;
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
		return "asking me what I want to buy";
	}
	
	private static class DemandsComparator implements Comparator<ManagedProperty<?>> {

		@Override
		public int compare(ManagedProperty<?> o1, ManagedProperty<?> o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
}
