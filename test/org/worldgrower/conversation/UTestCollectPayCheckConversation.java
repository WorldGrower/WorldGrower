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
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestCollectPayCheckConversation {

	private final CollectPayCheckConversation conversation = Conversations.COLLECT_PAY_CHECK_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, I'll pay your pay check", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, I won't pay your pay check", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());
		target.setProperty(Constants.ORGANIZATION_GOLD, 200);
		
		createDefaultVillagersOrganization(world, target);
				
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.ORGANIZATION_GOLD, 0);
		moveTurnsForword(world, 2000);
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
		assertEquals("I'm here to collect my pay check of 0 gold. Will you pay?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());

		createDefaultVillagersOrganization(world, target);
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.TAX_COLLECTOR_PROFESSION);
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));

		performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		target.setProperty(Constants.ORGANIZATION_GOLD, 0);
		moveTurnsForword(world, 2000);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());

		createDefaultVillagersOrganization(world, target);
		
		performer.setProperty(Constants.GOLD, 0);
		performer.setProperty(Constants.PROFESSION, Professions.TAX_COLLECTOR_PROFESSION);
		performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		target.setProperty(Constants.ORGANIZATION_GOLD, 1000);
		moveTurnsForword(world, 2000);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		conversation.handleResponse(0, context);
		
		assertEquals(20, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(980, target.getProperty(Constants.ORGANIZATION_GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.BUILDINGS, new BuildingList());

		createDefaultVillagersOrganization(world, target);
		
		performer.setProperty(Constants.GOLD, 0);
		performer.setProperty(Constants.PROFESSION, Professions.TAX_COLLECTOR_PROFESSION);
		performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		target.setProperty(Constants.ORGANIZATION_GOLD, 1000);
		moveTurnsForword(world, 2000);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		conversation.handleResponse(1, context);
		
		assertEquals(-150, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(0, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}

	private void createDefaultVillagersOrganization(World world, WorldObject target) {
		WorldObject organization = createVillagersOrganization(world);
		organization.getProperty(Constants.TAXES_PAID_TURN).incrementValue(target, 1);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		organization.setProperty(Constants.TAX_COLLECTOR_WAGE, 5);
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
}
