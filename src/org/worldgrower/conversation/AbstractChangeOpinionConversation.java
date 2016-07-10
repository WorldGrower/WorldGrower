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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public abstract class AbstractChangeOpinionConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int ALREADY_ASKED_SAME = 2;
	private static final int ALREADY_ASKED_DIFFERENT = 3;

	@Override
	public final Response getReplyPhrase(ConversationContext conversationContext) {
		List<HistoryItem> historyItems = this.findSameConversation(conversationContext);
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();

		final int replyId;
		if (historyItems.size() == 0) {
			if (targetAccepts(target, performer)) {
				replyId = YES;
			} else {
				replyId = NO;
			}
		} else {
			HistoryItem lastHistoryItem = historyItems.get(historyItems.size() - 1);
			int lastResponse = (Integer) lastHistoryItem.getAdditionalValue();
			boolean targetAcceptedInPast = (lastResponse == YES);
			boolean targetAcceptsNow = targetAccepts(target, performer);
			if (targetAcceptedInPast == targetAcceptsNow) {
				replyId = ALREADY_ASKED_SAME;
			} else {
				replyId = ALREADY_ASKED_DIFFERENT;
			}
		}

		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private boolean targetAccepts(WorldObject target, WorldObject performer) {
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		return relationshipValue > 0;
	}
	
	public abstract Question createQuestion(WorldObject performer, WorldObject target, WorldObject subject);
	
	@Override
	public final List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subject, World world) {
		return Arrays.asList(createQuestion(performer, target, subject));
	}
	
	@Override
	public List<WorldObject> getPossibleSubjects(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		List<Integer> subjectIds = relationships.getIdsWithoutTarget(target);

		List<WorldObject> subjects = new ArrayList<>();
		for (int subjectId : subjectIds) {
			if (subjectId != performer.getProperty(Constants.ID)) {
				WorldObject subject = world.findWorldObjectById(subjectId);
				int relationshipValuePerformer = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
				int relationshipValueSubject = target.getProperty(Constants.RELATIONSHIPS).getValue(subject);
				if (relationshipValueSubject < relationshipValuePerformer) {
					subjects.add(subject);
				}
			}
		}

		return subjects;
	}

	@Override
	public final List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		boolean targetAccepts = targetAccepts(target, performer);
		
		return Arrays.asList(
				new Response(YES, "Yes"), 
				new Response(NO, "No"),
				new Response(ALREADY_ASKED_SAME, "My answer is still the same as the last time you asked, " + (targetAccepts ? "yes" : "no")),
				new Response(ALREADY_ASKED_DIFFERENT, "This time my answer is " + (targetAccepts ? "yes" : "no"))
				);
	}

	@Override
	public final void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		World world = conversationContext.getWorld();

		boolean alreadyAsked = (replyIndex == ALREADY_ASKED_SAME || replyIndex == ALREADY_ASKED_DIFFERENT); 
		boolean targetAccepts = (replyIndex == YES || (targetAccepts(target, performer) && alreadyAsked));
		boolean targetDeclines = (replyIndex == NO || (!targetAccepts(target, performer) && alreadyAsked));
		
		if (targetAccepts) {
			handleYesResponse(performer, target, subject, world);
		} else if (targetDeclines) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}
	
	public abstract void handleYesResponse(WorldObject performer, WorldObject target, WorldObject subject, World world);

	@Override
	public final boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}

}
