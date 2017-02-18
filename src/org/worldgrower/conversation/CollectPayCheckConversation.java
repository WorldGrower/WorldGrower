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
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.text.TextId;

public class CollectPayCheckConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		if (targetCanPay(performer, target, world)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
		return Arrays.asList(new Question(TextId.QUESTION_COLLECT_PAYCHECK, amountToCollect));
	}
	
	private boolean targetCanPay(WorldObject performer, WorldObject target, World world) {
		int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
		return (target.getProperty(Constants.ORGANIZATION_GOLD) >= amountToCollect);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		List<Response> responses = new ArrayList<>();
		if (targetCanPay(performer, target, world)) {
			responses.add(new Response(YES, TextId.ANSWER_COLLECT_PAYCHECK_YES));
		}
		responses.add(new Response(NO, TextId.ANSWER_COLLECT_PAYCHECK_NO));
		return responses;
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		return ((performerProfession != null) && (performerProfession.isPaidByVillagerLeader()) && (GroupPropertyUtils.getPayCheckAmount(performer, world) > 0));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		WorldObject organization = GroupPropertyUtils.getVillagersOrganization(world);
		
		if (replyIndex == YES) {
			int amountToCollect = GroupPropertyUtils.getPayCheckAmount(performer, world);
			target.increment(Constants.ORGANIZATION_GOLD, -amountToCollect);
			performer.increment(Constants.GOLD, amountToCollect);
			
			IdMap payCheckPaidTurn = organization.getProperty(Constants.PAY_CHECK_PAID_TURN);
			payCheckPaidTurn.remove(performer);
			payCheckPaidTurn.incrementValue(performer, world.getCurrentTurn().getValue());
		} else if (replyIndex == NO) {
			//TODO: rebellion?
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -150, 0, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
		
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about collecting my pay check";
	}

	public boolean previousAnswerWasNegative(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, NO, performer, target, world);
	}
}
