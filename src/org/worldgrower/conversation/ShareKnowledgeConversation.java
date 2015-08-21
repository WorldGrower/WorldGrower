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
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;

public class ShareKnowledgeConversation implements Conversation {

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
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		List<Question> questions = new ArrayList<>();
		KnowledgeMap performerOnlyKnowledge = getPerformerOnlyKnowledge(performer, target);
		for(int id : performerOnlyKnowledge.getIds()) {
			WorldObject subject = world.findWorldObject(Constants.ID, id);
			//TODO: implement fully
			questions.add(new Question(subject, "Did you know the well is poisoned?"));
		}
		return questions;
	}

	private KnowledgeMap getPerformerOnlyKnowledge(WorldObject performer, WorldObject target) {
		KnowledgeMap performerKnowledge = performer.getProperty(Constants.KNOWLEDGE_MAP);
		KnowledgeMap targetKnowledge = target.getProperty(Constants.KNOWLEDGE_MAP);
		KnowledgeMap performerOnlyKnowledge = performerKnowledge.subtract(targetKnowledge);
		return performerOnlyKnowledge;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(0, "Thanks for the information"),
			new Response(1, "Get lost")
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		KnowledgeMap performerOnlyKnowledge = getPerformerOnlyKnowledge(performer, target);
		return performerOnlyKnowledge.hasKnowledge();
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		WorldObject subject = conversationContext.getSubject();
		World world = conversationContext.getWorld();
		
		KnowledgeMap performerKnowledge = performer.getProperty(Constants.KNOWLEDGE_MAP);
		Knowledge knowledge = performerKnowledge.getKnowledge(subject);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, knowledge);
		
		RelationshipPropertyUtils.changeRelationshipValue(performer, target, 50, Actions.TALK_ACTION, Conversations.createArgs(this), world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "sharing knowledge";
	}
}
