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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class ProposeMateConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int ALREADY_ASKED_SAME = 2;
	private static final int ALREADY_ASKED_DIFFERENT = 3;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
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
		return relationshipValue >= 750;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(null, Text.QUESTION_PROPOSE_MATE.get()));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		boolean targetAccepts = targetAccepts(target, performer);
		
		return Arrays.asList(
			new Response(YES, Text.ANSWER_PROPOSE_MATE_YES.get()),
			new Response(NO, Text.ANSWER_PROPOSE_MATE_NO.get()),
			new Response(ALREADY_ASKED_SAME, Text.ANSWER_PROPOSE_MATE_SAME.get((targetAccepts ? "yes" : "no"))),
			new Response(ALREADY_ASKED_DIFFERENT, Text.ANSWER_PROPOSE_MATE_DIFFERENT.get((targetAccepts ? "yes" : "no")))
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		boolean alreadyAsked = (replyIndex == ALREADY_ASKED_SAME || replyIndex == ALREADY_ASKED_DIFFERENT); 
		boolean targetAccepts = (replyIndex == YES || (targetAccepts(target, performer) && alreadyAsked));
		boolean targetDeclines = (replyIndex == NO || (!targetAccepts(target, performer) && alreadyAsked));
		if (targetAccepts) {
			breakupWithPreviousMate(performer, target, world);
			makeMates(performer, target, world);
		} else if (targetDeclines) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}

	private void breakupWithPreviousMate(WorldObject performer, WorldObject target, World world) {
		Integer performerMateId = performer.getProperty(Constants.MATE_ID);
		
		if (performerMateId != null) {
			WorldObject performerMate = world.findWorldObjectById(performerMateId);
			Conversations.BREAKUP_WITH_MATE_CONVERSATION.breakup(performer, performerMate, world);
		}
		
		//targetMateId may have been set to null by breakup of performer with target
		Integer targetMateId = target.getProperty(Constants.MATE_ID);
		if (targetMateId != null) {
			WorldObject targetMate = world.findWorldObjectById(targetMateId);
			Conversations.BREAKUP_WITH_MATE_CONVERSATION.breakup(target, targetMate, world);
		}
	}

	private void makeMates(WorldObject performer, WorldObject target, World world) {
		performer.setProperty(Constants.MATE_ID, target.getProperty(Constants.ID));
		target.setProperty(Constants.MATE_ID, performer.getProperty(Constants.ID));
		performer.setProperty(Constants.MATE_TURN, world.getCurrentTurn().getValue());
		target.setProperty(Constants.MATE_TURN, world.getCurrentTurn().getValue());
		
		world.logAction(Actions.TALK_ACTION, performer, target, Args.EMPTY, performer.getProperty(Constants.NAME) + " and " + target.getProperty(Constants.NAME) + " are mates");
		// mate reason
		// performer ~ MateGoal
		// target ~ relationship > 750
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about becoming a mate for someone";
	}

	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, performer, target, world);
	}
}
