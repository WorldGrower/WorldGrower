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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestCollectTaxesConversation {

	private final CollectTaxesConversation conversation = Conversations.COLLECT_TAXES_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		target.setProperty(Constants.BUILDINGS, new BuildingList());
		target.setProperty(Constants.GOLD, 1000);
		
		createDefaultVillagersOrganization(world, target);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, I'll pay my taxes", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, I won't pay my taxes", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());

		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		target.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		target.setProperty(Constants.GOLD, 200);
		
		createDefaultVillagersOrganization(world, target);
				
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		moveTurnsForword(world, 2000);
		target.setProperty(Constants.GOLD, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());

		createDefaultVillagersOrganization(world, target);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("I'm here to collect your taxes. The taxes are 0 gold. Will you pay your taxes?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		target.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		target.setProperty(Constants.GOLD, 200);
		
		createDefaultVillagersOrganization(world, target);
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);

		moveTurnsForword(world, 2000);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
	}

	private void createDefaultVillagersOrganization(World world, WorldObject target) {
		WorldObject organization = createVillagersOrganization(world);
		organization.getProperty(Constants.TAXES_PAID_TURN).incrementValue(target, 1);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
	}

	private void moveTurnsForword(World world, int count) {
		for(int i=0; i<count; i++) {
			world.nextTurn();
		}
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	@Test
	public void testSeizeAssets() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject leader = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());
		world.addWorldObject(leader);
		
		int shackId = BuildingGenerator.generateShack(0, 0, world, target);
		target.getProperty(Constants.BUILDINGS).add(shackId, BuildingType.SHACK);
		
		createDefaultVillagersOrganization(world, leader);
		GroupPropertyUtils.getVillagersOrganization(world).setProperty(Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		
		conversation.seizeAssets(target, world);
		
		assertEquals(false, target.getProperty(Constants.BUILDINGS).contains(shackId));
		assertEquals(true, leader.getProperty(Constants.BUILDINGS).contains(shackId));
	}
	
	//TODO: less setup for unit tests?
}
