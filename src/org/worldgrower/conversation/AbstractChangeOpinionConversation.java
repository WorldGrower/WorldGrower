/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.conversation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.history.HistoryItem;

public abstract class AbstractChangeOpinionConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;

	@Override
	public final Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();

		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (relationshipValue > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}

		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	public abstract Question createQuestion(WorldObject performer, WorldObject target, WorldObject subject);
	
	@Override
	public final List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);

		List<Question> questions = new ArrayList<>();
		for (int subjectId : subjectIds) {
			if (subjectId != performer.getProperty(Constants.ID)) {
				WorldObject subject = world.findWorldObject(Constants.ID, subjectId);
				int relationshipValuePerformer = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
				int relationshipValueSubject = target.getProperty(Constants.RELATIONSHIPS).getValue(subject);
				if (relationshipValueSubject < relationshipValuePerformer) {
					questions.add(createQuestion(performer, target, subject));
				}
			}
		}

		return questions;
	}

	@Override
	public final List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
				new Response(YES, "Yes"), 
				new Response(NO, "No"));
	}

	@Override
	public final void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();

		if (replyIndex == YES) {
			handleYesResponse(performer, target, subject);
		} else if (replyIndex == NO) {
			performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, -10);
			target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -10);
		}
	}
	
	public abstract void handleYesResponse(WorldObject performer, WorldObject target, WorldObject subject);

	@Override
	public final boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}

}
