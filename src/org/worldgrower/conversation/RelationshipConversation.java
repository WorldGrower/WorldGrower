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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.history.HistoryItem;

public class RelationshipConversation implements Conversation {

	private static final int DONT_KNOW = 0;
	private static final int LIKE = 1;
	private static final int REALLY_LIKE = 2;
	private static final int DISLIKE = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		
		IdMap relationships = target.getProperty(Constants.RELATIONSHIPS);
		int relationshipValue = relationships.getValue(subject.getProperty(Constants.ID));

		final int replyId;
		if (relationshipValue == 0) {
			replyId = DONT_KNOW;
		} else if ((relationshipValue > 0) && (relationshipValue < 500)) {
			replyId = LIKE;
		} else if (relationshipValue >= 500) {
			replyId = REALLY_LIKE;
		} else {
			replyId = DISLIKE;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);
		
		List<Question> questions = new ArrayList<>();
		for(int subjectId : subjectIds) {
			if (subjectId != performer.getProperty(Constants.ID)) {
				WorldObject subject = world.findWorldObject(Constants.ID, subjectId);
				questions.add(new Question(subject, "What do you think about " + subject.getProperty(Constants.NAME)));
			}
		}
		
		return questions;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject subject = conversationContext.getSubject();
		
		return Arrays.asList(
			new Response(DONT_KNOW, subject, "I don't know " + subject.getProperty(Constants.NAME)),
			new Response(LIKE, subject, "I like " + subject.getProperty(Constants.NAME)),
			new Response(REALLY_LIKE, subject, "I really like " + subject.getProperty(Constants.NAME)),
			new Response(DISLIKE, subject, "I don't like " + subject.getProperty(Constants.NAME))
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about relationships with other people";
	}
}
