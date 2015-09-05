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
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class ProfessionPractitionersConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
		
		final int replyId;
		if (relationshipValue >= 0 && getProfessionPractitioners(conversationContext).size() > 0) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		List<Question> questions = new ArrayList<>();
		for(Profession profession : Professions.getAllProfessions()) {
			questions.add(new Question(null, "Do you know any people who are " + profession.getDescription() + "s?", Professions.getAllProfessions().indexOf(profession)));
		}
		return questions;
	}
	
	private List<WorldObject> getProfessionPractitioners(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		Profession profession = Professions.getAllProfessions().get(conversationContext.getAdditionalValue());
		World world = conversationContext.getWorld();
		return target.getProperty(Constants.KNOWLEDGE_MAP).findWorldObjects(Constants.PROFESSION, profession, world);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		String followersDescription = getProfessionPractitionersDescription(conversationContext);
		
		Profession profession = Professions.getAllProfessions().get(conversationContext.getAdditionalValue());
		return Arrays.asList(
			new Response(YES, "I know that " + followersDescription + " are " + profession.getDescription() + "s"),
			new Response(NO, "No")
			);
	}

	private String getProfessionPractitionersDescription(ConversationContext conversationContext) {
		List<WorldObject> professionPractitioners = getProfessionPractitioners(conversationContext);
		
		StringBuilder followersDescription = new StringBuilder();
		for(WorldObject follower : professionPractitioners) {
			followersDescription.append(follower.getProperty(Constants.NAME)).append(", ");
		}
		return followersDescription.toString();
	}

	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
		return true;
	}

	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		RelationshipPropertyUtils.changeRelationshipValue(performer, target, 10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about profession practitioners";
	}
}
