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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestOrganizationConversation {

	private final OrganizationConversation conversation = Conversations.ORGANIZATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("I belong to no organization", replyPhrases.get(0).getResponsePhrase());
		assertEquals("I don't belong to any organizations", replyPhrases.get(1).getResponsePhrase());
		
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "Alliance of Farmers", Professions.FARMER_PROFESSION, world);
		target.getProperty(Constants.GROUP).add(organization);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I belong to the Alliance of Farmers", replyPhrases.get(0).getResponsePhrase());
		
		WorldObject organization2 = GroupPropertyUtils.createReligionOrganization(null, "Cult of Hades", Deity.HADES, Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL, world);
		target.getProperty(Constants.GROUP).add(organization2);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I belong to the Alliance of Farmers, the Cult of Hades", replyPhrases.get(0).getResponsePhrase());
	}


	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "Alliance of Farmers", Professions.FARMER_PROFESSION, world);
		target.getProperty(Constants.GROUP).add(organization);
		
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("What organizations do you belong to?", questions.get(0).getQuestionPhrase());
	}
}
