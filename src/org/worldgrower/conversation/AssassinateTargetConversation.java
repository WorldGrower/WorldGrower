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
import org.worldgrower.personality.PersonalityTrait;

public class AssassinateTargetConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		
		IdMap relationships = target.getProperty(Constants.RELATIONSHIPS);
		int relationshipValueSubject = relationships.getValue(subject.getProperty(Constants.ID));
		int relationshipValuePerformer = relationships.getValue(performer.getProperty(Constants.ID));
		boolean targetIsHonorable = target.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.HONORABLE) > 100;
		
		final int replyId;
		if (targetIsHonorable) {
			replyId = NO;
		} else if (relationshipValueSubject < -500 && relationshipValuePerformer > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(new Question(subject, "Would you like me to get rid of " + subject.getProperty(Constants.NAME) + "? If you agree, it'll cost you 100 gold."));
	}

	@Override
	public List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);
		
		List<WorldObject> subjects = new ArrayList<>();
		for(int subjectId : subjectIds) {
			if (subjectId != performer.getProperty(Constants.ID)) {
				WorldObject subject = world.findWorldObject(Constants.ID, subjectId);
				if (target.getProperty(Constants.RELATIONSHIPS).contains(subject)) {
					subjects.add(subject);
				}
			}
		}
		
		return subjects;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject subject = conversationContext.getSubject();
		
		return Arrays.asList(
			new Response(YES, subject, "Yes"),
			new Response(NO, subject, "No")
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
		return "talking about assassinating someone";
	}
}
