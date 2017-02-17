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
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.BountyPropertyUtils;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class UTestCollectBountyFromThievesConversation {

	private final CollectBountyFromThievesConversation conversation = Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.GOLD, 1000);
		createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(3, replyPhrases.size());
		assertEquals("I will pay the bounty", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I will spend my time in jail", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I resist arrest", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		
		target.setProperty(Constants.GOLD, 1000);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 10000);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 40);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("I'm here to collect your bounty, 40 gold, what will you do?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.GOLD, 100);
		target.setProperty(Constants.GOLD, 100);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 40);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(140, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(60, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(10, 10, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.GOLD, 100);
		target.setProperty(Constants.GOLD, 100);
		
		BuildingGenerator.generateJail(0, 0, world, 1f);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 40);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(0, BountyPropertyUtils.getBounty(target, world));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 40);
		target.setProperty(Constants.GROUP, new IdList().add(villagersOrganization));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(0, target.getProperty(Constants.GROUP).size());
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		world.addWorldObject(target);
		performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 40);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
	}
	
	@Test
	public void testIsConversationAvailableForLookalike() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		world.addWorldObject(target);
		performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		WorldObject target2 = TestUtils.createIntelligentWorldObject(3, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target2.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		world.addWorldObject(target2);
		
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target2, 40);
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
