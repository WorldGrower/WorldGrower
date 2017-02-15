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
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;
import org.worldgrower.text.Text;

public class DeityExplanationConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	private static final int GET_LOST = 2;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		Deity subjectDeity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		Deity targetDeity = target.getProperty(Constants.DEITY);
		Profession targetProfession = target.getProperty(Constants.PROFESSION);
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if (relationshipValue < 0) {
			replyId = GET_LOST;
		} else if ((targetDeity == subjectDeity) || (targetProfession == Professions.PRIEST_PROFESSION)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		for(Deity deity : Deity.ALL_DEITIES) {
			int indexOfDeity = Deity.ALL_DEITIES.indexOf(deity);
			questions.add(new Question(null, Text.QUESTION_DEITY_EXPLANATION.get(deity.getName()), indexOfDeity));
		}
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		Deity subjectDeity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		return Arrays.asList(
			new Response(YES, Text.ANSWER_DEITY_EXPLANATION_YES, subjectDeity.getExplanation()),
			new Response(NO, Text.ANSWER_DEITY_EXPLANATION_NO, subjectDeity.getName()),
			new Response(GET_LOST, Text.ANSWER_DEITY_EXPLANATION_GETLOST)
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
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -100, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about my deity";
	}
}
