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
		Profession profession = Professions.getAllSortedProfessions().get(conversationContext.getAdditionalValue());
		boolean targetHasProfession = (target.getProperty(Constants.PROFESSION) == profession);
		boolean hasOtherPractitioners = getProfessionPractitioners(conversationContext).size() > 0;
		boolean replyYes = targetHasProfession || hasOtherPractitioners;
		
		final int replyId;
		if (relationshipValue >= 0 && replyYes) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		List<Question> questions = new ArrayList<>();
		for(Profession profession : Professions.getAllSortedProfessions()) {
			questions.add(new Question(null, "Do you know any people who are " + profession.getDescription() + "s?", Professions.getAllSortedProfessions().indexOf(profession)));
		}
		return questions;
	}
	
	private List<WorldObject> getProfessionPractitioners(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		Profession profession = Professions.getAllSortedProfessions().get(conversationContext.getAdditionalValue());
		return target.getProperty(Constants.KNOWLEDGE_MAP).findWorldObjects(Constants.PROFESSION, profession, world);
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		return Arrays.asList(
			new Response(YES, getProfessionPractitionersDescription(conversationContext)),
			new Response(NO, "No")
			);
	}

	private String getProfessionPractitionersDescription(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		List<WorldObject> professionPractitioners = getProfessionPractitioners(conversationContext);
		Profession profession = Professions.getAllSortedProfessions().get(conversationContext.getAdditionalValue());

		if (target.getProperty(Constants.PROFESSION) == profession) {
			return "I'm a " + profession.getDescription();
		} else if (professionPractitioners.size() == 1) {
			return "I know that " + professionPractitioners.get(0).getProperty(Constants.NAME) + " is a " + profession.getDescription();
		} else if (professionPractitioners.size() > 0) {
			StringBuilder followersDescription = new StringBuilder();
			for(int i=0; i<professionPractitioners.size(); i++) {
				WorldObject follower = professionPractitioners.get(i);
				followersDescription.append(follower.getProperty(Constants.NAME));
				if (i < professionPractitioners.size() - 1) {
					followersDescription.append(", ");
				}
			}
			return "I know that " + followersDescription.toString() + " are " + profession.getDescription() + "s";
		} else {
			return "I know no-one that is a " + profession.getDescription();
		}
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
		RelationshipPropertyUtils.changeRelationshipValue(performer, target, 10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about profession practitioners";
	}
}
