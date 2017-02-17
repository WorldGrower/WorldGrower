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
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.RelationshipPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;
import org.worldgrower.text.Text;

public class LearnSkillUsingOrganizationConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		WorldObject target = conversationContext.getTarget();
		
		final int replyId;
		if (SkillUtils.canTargetTeachPerformer(performer,  target)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		Profession profession = performer.getProperty(Constants.PROFESSION);
		WorldObject organization = GroupPropertyUtils.findProfessionOrganization(performer, world);
		
		return Arrays.asList(new Question(organization, Text.QUESTION_LEARN_SKILL, profession.getDescription(), organization.getProperty(Constants.NAME)));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject performer = conversationContext.getPerformer();
		Profession profession = performer.getProperty(Constants.PROFESSION);
		
		return Arrays.asList(
			new Response(YES, Text.ANSWER_LEARN_SKILL_YES, profession.getDescription()),
			new Response(NO, Text.ANSWER_LEARN_SKILL_NO)
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		WorldObject performerOrganization = GroupPropertyUtils.findProfessionOrganization(performer, world);
		WorldObject targetOrganization = GroupPropertyUtils.findProfessionOrganization(target, world);
		return SkillUtils.canTargetTeachPerformer(performer, target) && performerOrganization.equals(targetOrganization);
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
		if (replyIndex == YES) {
			WorldObject performer = conversationContext.getPerformer();
			
			Profession performerProfession = performer.getProperty(Constants.PROFESSION);
			SkillProperty skillProperty = performerProfession.getSkillProperty();
			
			World world = conversationContext.getWorld();
			SkillUtils.teachSkill(performer, skillProperty, world.getWorldStateChangedListeners());
		} else if (replyIndex == NO) {
			WorldObject performer = conversationContext.getPerformer();
			WorldObject target = conversationContext.getTarget();
			World world = conversationContext.getWorld();
			RelationshipPropertyUtils.changeRelationshipValue(performer, target, -10, Actions.TALK_ACTION, Conversations.createArgs(this), world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about improving my skills";
	}
}
