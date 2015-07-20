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
import org.worldgrower.attribute.Skill;
import org.worldgrower.history.HistoryItem;

public class IntimidateConversation implements Conversation {

	private static final int GET_LOST = -999;
	private static final int I_LL_COMPLY = 0;
	
	private final Conversation parentConversation;
	
	public IntimidateConversation(Conversation parentConversation) {
		this.parentConversation = parentConversation;
	}
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		int performerIntimidate = useIntimidateSkill(conversationContext);
		int targetInsight = useInsightSkill(conversationContext);
		
		if (performerIntimidate > targetInsight) {
			conversationContext.getTarget().getProperty(Constants.RELATIONSHIPS).incrementValue(conversationContext.getPerformer(), 1000);
		
			return parentConversation.getReplyPhrase(conversationContext);
		} else {
			return getReply(getReplyPhrases(conversationContext), GET_LOST);
		}
	}

	private int useInsightSkill(ConversationContext conversationContext) {
		Skill insightSkill = conversationContext.getTarget().getProperty(Constants.INSIGHT_SKILL);
		int level = insightSkill.getLevel();
		insightSkill.use();
		return level;
	}

	private int useIntimidateSkill(ConversationContext conversationContext) {
		Skill intimidateSkill = conversationContext.getPerformer().getProperty(Constants.INTIMIDATE_SKILL);
		int level = intimidateSkill.getLevel();
		intimidateSkill.use();
		return level;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		List<Question> parentQuestionPhrases = parentConversation.getQuestionPhrases(performer, target, questionHistoryItem, world);
		List<Question> result = new ArrayList<>();
		for(Question parentQuestionPhrase : parentQuestionPhrases) {
			WorldObject subject = parentQuestionPhrase.getSubjectId() != -1 ? world.findWorldObject(Constants.ID, parentQuestionPhrase.getSubjectId()) : null;
			result.add(new Question(subject, "I think you better help me or I'll slit your throat. " + parentQuestionPhrase.getQuestionPhrase()));
		}
		return result;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(GET_LOST, "Get lost"),
			new Response(I_LL_COMPLY, "I'll comply")
		);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		conversationContext.getTarget().getProperty(Constants.RELATIONSHIPS).incrementValue(conversationContext.getPerformer(), -1000);
	}
}
