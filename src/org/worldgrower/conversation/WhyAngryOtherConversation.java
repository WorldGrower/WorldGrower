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
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.TextId;

public class WhyAngryOtherConversation implements Conversation {

	private static final int REAL_REASON = 0;
	private static final int GET_LOST = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		final int replyId = REAL_REASON;
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(new Question(subject, TextId.QUESTION_ANGRY_OTHER, subject.getProperty(Constants.NAME)));
	}
	
	@Override
	public List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);
		
		List<WorldObject> subjects = new ArrayList<>();
		for(int subjectId : subjectIds) {
			if (subjectId != performer.getProperty(Constants.ID)) {
				WorldObject subject = world.findWorldObjectById(subjectId);
				int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(subject);
				if (relationshipValue < 0) {
					subjects.add(subject);
				}
			}
		}
		
		return subjects;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		WorldObject subject = conversationContext.getSubject();
		
		String concatenatedAngryReasons = target.getProperty(Constants.BACKGROUND).getConcatenatedAngryReasons(false, target.getProperty(Constants.ID), subject, world);
		
		return Arrays.asList(
			new Response(REAL_REASON, TextId.ANSWER_ANGRY_OTHER_REASON, concatenatedAngryReasons),
			new Response(GET_LOST, TextId.ANSWER_ANGRY_OTHER_GETLOST)
		);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == REAL_REASON) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -20, -5, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about why I'm angry with someone";
	}
}
