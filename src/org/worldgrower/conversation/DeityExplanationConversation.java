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
import org.worldgrower.deity.Deity;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class DeityExplanationConversation implements Conversation {

	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Deity subjectDeity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		Deity targetDeity = target.getProperty(Constants.DEITY);
		Profession targetProfession = target.getProperty(Constants.PROFESSION);
		
		final int replyId;
		if ((targetDeity == subjectDeity) || (targetProfession == Professions.PRIEST_PROFESSION)) {
			replyId = 0;
		} else {
			replyId = 1;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		for(Deity deity : Deity.ALL_DEITIES) {
			int indexOfDeity = Deity.ALL_DEITIES.indexOf(deity);
			questions.add(new Question(null, "What can you tell me about " + deity.getName(), indexOfDeity));
		}
		return questions;
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		Deity subjectDeity = Deity.ALL_DEITIES.get(conversationContext.getAdditionalValue());
		return Arrays.asList(
			new Response(0, subjectDeity.getExplanation()),
			new Response(1, "I don't know more about " + subjectDeity.getName())
			);
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about my deity";
	}
}
