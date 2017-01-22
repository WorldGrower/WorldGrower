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
import org.worldgrower.text.Text;

public class LocationConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject subject = conversationContext.getSubject();
		Direction direction = getDirection(performer, subject);
		int replyId = direction.ordinal();
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	static Direction getDirection(WorldObject performer, WorldObject subject) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		int subjectX = subject.getProperty(Constants.X);
		int subjectY = subject.getProperty(Constants.Y);
		
		int diffX = performerX - subjectX;
		int diffY = performerY - subjectY;
		
		Direction replyId;
		if (diffY < 0) {
			replyId = Direction.SOUTH;
		} else if (diffY > 0) {
			replyId = Direction.NORTH;
		} else if (diffX > 0){
			replyId = Direction.WEST;
		} else if (diffX < 0){
			replyId = Direction.EAST;
		} else {
			replyId = null;
		}
		return replyId;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(new Question(subject, Text.QUESTION_LOCATION.get(subject.getProperty(Constants.NAME))));
	}
	
	@Override
	public List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);
		
		List<WorldObject> subjects = new ArrayList<>();
		for(int subjectId : subjectIds) {
			WorldObject subject = world.findWorldObjectById(subjectId);
			subjects.add(subject);
		}
		
		return subjects;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject subject = conversationContext.getSubject();
		List<Response> responses = new ArrayList<>();
		
		for (Direction direction : Direction.values()) {
			responses.add(new Response(direction.ordinal(), subject, Text.ANSWER_LOCATION.get(subject.getProperty(Constants.NAME), direction.getDescription())));
		}

		return responses;
	}
	
	public enum Direction {
		NORTH("north"),
		NORTH_EAST("northeast"),
		EAST("east"),
		SOUTH_EAST("southeast"),
		SOUTH("south"),
		SOUTH_WEST("southwest"),
		WEST("west"),
		NORTH_WEST("northwest");
		
		private final String description;

		Direction(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
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
		return "talking about the location of someone";
	}
}