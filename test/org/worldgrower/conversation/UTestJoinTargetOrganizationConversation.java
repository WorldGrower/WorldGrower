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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestJoinTargetOrganizationConversation {

	private final JoinTargetOrganizationConversation conversation = Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createWorldObject(3, "TestOrg");
		
		ConversationContext context = new ConversationContext(performer, target,subject, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("Yes, you can join the TestOrg", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createWorldObject(3, "TestOrg");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(3));
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(target.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		organization.setProperty(Constants.ID, 3);
		world.addWorldObject(organization);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("Can I join the TestOrg ?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(3));
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(performer.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		organization.setProperty(Constants.ID, 3);
		world.addWorldObject(organization);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 50);
		assertEquals(Arrays.asList(), performer.getProperty(Constants.GROUP).getIds());
		
		conversation.handleResponse(0, context);
		assertEquals(Arrays.asList(3), performer.getProperty(Constants.GROUP).getIds());
	}
}
