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
import org.worldgrower.attribute.Skill;
import org.worldgrower.history.HistoryItem;

public class ComplimentConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		if (relationshipValue >= 0 && findSameConversation(conversationContext).size() == 0) {
			replyId = 0;
		} else if (relationshipValue >= 0) {
			replyId = 1;
		} else {
			replyId = 2;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	private double getDiplomacyBonus(ConversationContext conversationContext) {
		double diplomacyBonus = 1.0f;
		int performerDiplomacy = useDiplomacySkill(conversationContext);
		int targetInsight = useInsightSkill(conversationContext);
		if (performerDiplomacy > targetInsight) {
			diplomacyBonus += (performerDiplomacy - targetInsight) / 100.0f;
		}
		return diplomacyBonus;
	}

	private int useInsightSkill(ConversationContext conversationContext) {
		Skill insightSkill = conversationContext.getTarget().getProperty(Constants.INSIGHT_SKILL);
		int level = insightSkill.getLevel();
		insightSkill.use();
		return level;
	}

	private int useDiplomacySkill(ConversationContext conversationContext) {
		Skill diplomacySkill = conversationContext.getPerformer().getProperty(Constants.DIPLOMACY_SKILL);
		int level = diplomacySkill.getLevel();
		diplomacySkill.use();
		return level;
	}
	
	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		return Arrays.asList(
			new Question(null, "You look very strong"),
			new Question(null, "You are very handsome")
			);
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(0, "Thanks, you too"),
			new Response(1, "Stop doing that"),
			new Response(2, "Get lost"));
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		if (replyIndex == 0) {
			double diplomacyBonus = getDiplomacyBonus(conversationContext);
			performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, (int) (50 * diplomacyBonus));
			target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, (int) (50 * diplomacyBonus));
		} else if (replyIndex == 2) {
			performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, -50);
			target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -50);
		}
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
}
