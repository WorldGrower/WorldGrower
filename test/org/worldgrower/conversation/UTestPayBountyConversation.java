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
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestPayBountyConversation {

	private final PayBountyConversation conversation = Conversations.PAY_BOUNTY_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, your bounty is cleared", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, I won't clear your bounty", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.GROUP).add(villagersOrganization);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(performer, 40);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("I'm here to pay my bounty, 40 gold, what will you do?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.GOLD, 100);
		target.setProperty(Constants.GOLD, 100);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(performer, 40);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(60, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(140, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(performer, 40);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-20, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-20, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		performer.setProperty(Constants.GOLD, 1000);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(performer, 40);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		world.generateUniqueId();world.generateUniqueId();
		return villagersOrganization;
	}
}
