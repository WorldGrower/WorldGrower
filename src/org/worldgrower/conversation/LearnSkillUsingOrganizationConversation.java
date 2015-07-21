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
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.profession.Profession;

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
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, World world) {
		Profession profession = performer.getProperty(Constants.PROFESSION);
		WorldObject organization = GroupPropertyUtils.findProfessionOrganization(performer, world);
		
		return Arrays.asList(new Question(organization, "Can you teach me to improve my " + profession.getDescription() + " skills as a fellow member of the " + organization.getProperty(Constants.NAME) + "?"));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject organization = conversationContext.getSubject();
		
		return Arrays.asList(
			new Response(YES, "Yes, I'll join the " + organization.getProperty(Constants.NAME)),
			new Response(NO, "No")
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, World world) {
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
			
			SkillUtils.teachSkill(performer, skillProperty);
		}
	}
}
