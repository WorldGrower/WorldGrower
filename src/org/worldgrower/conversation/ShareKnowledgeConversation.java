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
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.goal.KnowledgePropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.Text;

public class ShareKnowledgeConversation implements Conversation {

	private KnowledgeToDescriptionMapper knowledgeToDescriptionMapper = new KnowledgeToDescriptionMapper();
	
	private final int THANKS = 0;
	private final int GET_LOST = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if (relationshipValue < 0) {
			replyId = GET_LOST;
		} else {
			replyId = THANKS;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		List<Knowledge> knowledgeList = getKnowledgeList(performer, target, world);
		for(int i=0; i<knowledgeList.size(); i++) {
			Knowledge knowledge = knowledgeList.get(i);
			WorldObject subject = world.findWorldObjectById(knowledge.getSubjectId());
			if (!subject.equals(target)) {
				String questionphrase = knowledgeToDescriptionMapper.getQuestionDescription(knowledge, world);
				questions.add(new Question(subject, Text.QUESTION_SHARE_KNOWLEDGE.get(questionphrase), knowledge.getId()));
			}
		}
		return questions;
	}

	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(THANKS, Text.ANSWER_SHARE_KNOWLEDGE_THANKS),
			new Response(GET_LOST, Text.ANSWER_SHARE_KNOWLEDGE_GETLOST)
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		KnowledgeMap performerOnlyKnowledge = KnowledgePropertyUtils.getPerformerOnlyKnowledge(performer, target);
		if (subject != null) {
			return performerOnlyKnowledge.hasKnowledge(subject.getProperty(Constants.ID));
		} else {
			return performerOnlyKnowledge.hasKnowledge();
		}
	}
	
	@Override
	public boolean additionalValuesValid(WorldObject performer, WorldObject target, int additionalValue, int additionalValue2, World world) {
		List<Knowledge> knowledgeList = getKnowledgeList(performer, target, world);
		int knowledgeId = additionalValue;
		return KnowledgePropertyUtils.exists(knowledgeList, knowledgeId);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		int knowledgeId = conversationContext.getAdditionalValue();
		World world = conversationContext.getWorld();
		
		List<Knowledge> knowledgeList = getKnowledgeList(performer, target, world);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, KnowledgePropertyUtils.find(knowledgeList, knowledgeId));

		if (replyIndex == THANKS) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		} else if (replyIndex == GET_LOST) {
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -50, -5, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	
		//TODO: if there are more return values, set return value Object on execute method, search for any other TODO like this
		world.getHistory().setNextAdditionalValue(replyIndex);
	}

	private List<Knowledge> getKnowledgeList(WorldObject performer, WorldObject target, World world) {
		KnowledgeMap performerOnlyKnowledge = KnowledgePropertyUtils.getPerformerOnlyKnowledge(performer, target);
		List<Knowledge> knowledgeList = performerOnlyKnowledge.getSortedKnowledge(performer, world);
		return knowledgeList;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "sharing knowledge";
	}
	
	public boolean previousAnswerWasGetLost(WorldObject performer, WorldObject target, World world) {
		return PreviousResponseIdUtils.previousResponseIdsContains(this, GET_LOST, performer, target, world);
	}
}
