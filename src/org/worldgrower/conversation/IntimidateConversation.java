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
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.text.TextId;

public class IntimidateConversation implements Conversation {

	private static final int GET_LOST = -999;
	private static final int I_LL_COMPLY = 0;
	
	private final Conversation parentConversation;
	
	public IntimidateConversation(Conversation parentConversation) {
		this.parentConversation = parentConversation;
	}
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		World world = conversationContext.getWorld();
		WorldObject performer = conversationContext.getPerformer();
		int performerIntimidate = SkillUtils.useSkillLevel(performer, Constants.INTIMIDATE_SKILL, world.getWorldStateChangedListeners());
		WorldObject target = conversationContext.getTarget();
		int targetInsight = calculateTargetInsight(world, target);
		
		if (performerIntimidate > targetInsight) {
			return parentConversation.getReplyPhrase(conversationContext);
		} else {
			return getReply(getReplyPhrases(conversationContext), GET_LOST);
		}
	}

	private int calculateTargetInsight(World world, WorldObject target) {
		int targetInsight = SkillUtils.useSkillLevel(target, Constants.INSIGHT_SKILL, world.getWorldStateChangedListeners());
		
		if (target.hasProperty(Constants.PERSONALITY)) {
			boolean isCourageous = target.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.COURAGEOUS) > 100;
			boolean isCowardly = target.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.COURAGEOUS) < -100;
			if (isCourageous) {
				targetInsight++;
			}
			if (isCowardly) {
				targetInsight--;
			}
		}
		return targetInsight;
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> parentQuestionPhrases = parentConversation.getQuestionPhrases(performer, target, questionHistoryItem, subjectWorldObject, world);
		List<Question> result = new ArrayList<>();
		for(Question parentQuestionPhrase : parentQuestionPhrases) {
			WorldObject subject = parentQuestionPhrase.getSubjectId() != -1 ? world.findWorldObjectById(parentQuestionPhrase.getSubjectId()) : null;
			result.add(new Question(subject, TextId.QUESTION_INTIMIDATE, parentQuestionPhrase.getQuestionPhraseAsFormattableText()));
		}
		return result;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(GET_LOST, TextId.ANSWER_INTIMIDATE_GETLOST),
			new Response(I_LL_COMPLY, TextId.ANSWER_INTIMIDATE_COMPLY)
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
		
		if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, -50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == I_LL_COMPLY) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 1000, 50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "intimidating me";
	}
}
