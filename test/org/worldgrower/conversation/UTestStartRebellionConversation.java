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
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestStartRebellionConversation {

	private final StartRebellionConversation conversation = Conversations.START_REBELLION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(3, replyPhrases.size());
		assertEquals("Yes, once a majority is in favor, we'll depose the current leader", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganizations = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(3, Constants.GROUP, new IdList());
		WorldObject leader = TestUtils.createIntelligentWorldObject(4, Constants.GROUP, new IdList());
		world.addWorldObject(leader);
		villagersOrganizations.setProperty(Constants.ORGANIZATION_LEADER_ID, 4);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(3, Constants.DEITY, Deity.HADES);
		WorldObject villagersOrganizations = createVillagersOrganization(world);
		
		WorldObject subject = villagersOrganizations;
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, subject, null);
		assertEquals(1, questions.size());
		assertEquals("Do you want to start a rebellion against the current leader of the villagers?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());

		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		WorldObject villagersOrganizations = createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, villagersOrganizations, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(2, villagersOrganizations.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());
		assertEquals(true, villagersOrganizations.getProperty(Constants.ORGANIZATION_REBEL_IDS).contains(7));
		assertEquals(true, villagersOrganizations.getProperty(Constants.ORGANIZATION_REBEL_IDS).contains(8));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(-100, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-100, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		world.generateUniqueId();world.generateUniqueId();
		return villagersOrganization;
	}
}
