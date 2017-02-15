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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class ComplimentConversation implements Conversation {

	private static final int THANKS = 0;
	private static final int STOP = 1;
	private static final int GET_LOST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (relationshipValue >= 0 && findSameConversation(conversationContext).size() == 0) {
			replyId = THANKS;
		} else if (relationshipValue >= 0) {
			replyId = STOP;
		} else {
			replyId = GET_LOST;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private double getDiplomacyBonus(ConversationContext conversationContext) {
		World world = conversationContext.getWorld();
		double diplomacyBonus = 1.0f;
		int performerDiplomacy = SkillUtils.useSkillLevel(conversationContext.getPerformer(), Constants.DIPLOMACY_SKILL, world.getWorldStateChangedListeners());
		int targetInsight = SkillUtils.useSkillLevel(conversationContext.getTarget(), Constants.INSIGHT_SKILL, world.getWorldStateChangedListeners());
		if (performerDiplomacy > targetInsight) {
			diplomacyBonus += (performerDiplomacy - targetInsight) / 100.0f;
		}
		return diplomacyBonus;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(
			new Question(null, Text.QUESTION_COMPLIMENT_STRONG.get()),
			new Question(null, Text.QUESTION_COMPLIMENT_HANDSOME.get())
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(THANKS, Text.ANSWER_COMPLIMENT_THANKS),
			new Response(STOP, Text.ANSWER_COMPLIMENT_STOP),
			new Response(GET_LOST, Text.ANSWER_COMPLIMENT_GETLOST));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		if (replyIndex == THANKS) {
			double diplomacyBonus = getDiplomacyBonus(conversationContext);
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, (int) (50 * diplomacyBonus), Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == STOP) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -5, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "complimenting " + target.getProperty(Constants.NAME);
	}
}
