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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.conversation.Response;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestTalkAction {

	private WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
	private WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
	private WorldObject deity = TestUtils.createWorldObject(3, "Demeter");
	private DungeonMaster dungeonMaster = new DungeonMaster();
	
	@Test
	public void testAskName() {
		World world = createWorld();
		String reply = talk(world, Conversations.createArgs(Conversations.NAME_CONVERSATION));
		assertEquals("My name is target", reply);
	}

	@Test
	public void testAskNameRepeatedly() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.NAME_CONVERSATION));
		assertEquals("My name is target", reply);
		
		reply = talk(world, Conversations.createArgs(Conversations.NAME_CONVERSATION));
		assertEquals("I told you a while back, my name is target", reply);
	}
	
	@Test
	public void testAskGoal() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.GOAL_CONVERSATION));
		assertEquals("I don't have a goal for the moment", reply);
		
		reply = talk(world, Conversations.createArgs(Conversations.GOAL_CONVERSATION));
		assertEquals("I don't have a goal for the moment", reply);
	}
	
	@Test
	public void testAskRelationship() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.RELATIONSHIP_CONVERSATION, performer));
		assertEquals("I don't know performer", reply);
		
		reply = talk(world, Conversations.createArgs(Conversations.RELATIONSHIP_CONVERSATION, performer));
		assertEquals("I like performer", reply);
	}
	
	@Test
	public void testAskProfessionNull() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		assertEquals("I don't have a profession", reply);
		
		reply = talk(world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		assertEquals("It's still the same as the last time you asked, namely I don't have a profession", reply);
	}
	
	@Test
	public void testAskProfessionFarmer() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		String reply = talk(target, world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		assertEquals("I'm a farmer", reply);
		
		reply = talk(target, world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		assertEquals("It's still the same as the last time you asked, namely farmer", reply);
	}
	
	@Test
	public void testAskProfessionChanged() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		
		String reply = talk(target, world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);		
		reply = talk(target, world, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION));
		assertEquals("I'm a farmer now", reply);
	}

	@Test
	public void testAskDemands() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.DEMANDS_CONVERSATION));
		assertEquals("I'm not looking for anything to buy right now", reply);
	}
	
	@Test
	public void testAskDeityNull() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		assertEquals("I don't worship a deity", reply);
		
		reply = talk(world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		assertEquals("I still worship no one", reply);
	}
	
	@Test
	public void testAskDeityNotNull() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		target.setProperty(Constants.DEITY, Deity.HADES);
		
		String reply = talk(target, world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		assertEquals("I worship Hades", reply);
		
		reply = talk(target, world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		assertEquals("I still worship Hades", reply);
	}
	
	@Test
	public void testAskDeityChanged() {
		World world = createWorld();
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		
		String reply = talk(target, world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		
		target.setProperty(Constants.DEITY, Deity.HADES);
		reply = talk(target, world, Conversations.createArgs(Conversations.DEITY_CONVERSATION));
		assertEquals("I now worship Hades", reply);
	}
	
	@Test
	public void testAskDeityExplanation() {
		World world = createWorld();
		
		String reply = talk(world, Conversations.createArgs(Conversations.DEITY_EXPLANATION_CONVERSATION, deity));
		assertEquals("I don't know more about Aphrodite", reply);		
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, dungeonMaster, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		
		target.setProperty(Constants.X, 0);
		target.setProperty(Constants.Y, 0);
		
		assertEquals(true, Actions.TALK_ACTION.isActionPossible(performer, target, new int[] { 0, -1, 0, 0, 0 }, world));
	}
	
	@Test
	public void testIsActionPossibleWithSubject() {
		World world = new WorldImpl(1, 1, dungeonMaster, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), "performer");
		world.addWorldObject(performer);
		WorldObject target = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), "target");
		world.addWorldObject(target);
		WorldObject subject = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), "subject");
		world.addWorldObject(subject);
		
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		
		target.setProperty(Constants.X, 0);
		target.setProperty(Constants.Y, 0);
		
		int[] args = new int[] { 0, subject.getProperty(Constants.ID), 0, 0, 0 };
		assertEquals(true, Actions.TALK_ACTION.isActionPossible(performer, target, args, world));
	}
	
	@Test
	public void testDistance10() {
		World world = new WorldImpl(1, 1, dungeonMaster, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		
		assertEquals(200, Actions.TALK_ACTION.distance(performer, target, new int[] { 0, -1, 0, 0, 0 }, world));
	}
	
	private World createWorld() {
		World world = new WorldImpl(1, 1, dungeonMaster, null);
		performer.setProperty(Constants.ID, world.generateUniqueId());
		world.addWorldObject(performer);
		target.setProperty(Constants.ID, world.generateUniqueId());
		world.addWorldObject(target);
		deity.setProperty(Constants.ID, world.generateUniqueId());
		world.addWorldObject(deity);
		return world;
	}

	private String talk(World world, int[] args) {
		return talk(target, world, args);
	}

	private String talk(WorldObject target, World world, int[] args) {
		TalkAction talkAction = Actions.TALK_ACTION;
		WorldListener listener = new WorldListener();
		world.addListener(listener);
		dungeonMaster.executeAction(talkAction, performer, target, args, world);
		return listener.getMessage();
	}
	
	private static class WorldListener implements ManagedOperationListener {

		private String message;
		
		@Override
		public void actionPerformed(ManagedOperation managedOperation,
				WorldObject performer, WorldObject target, int[] args,
				Object message) {
			
			this.message = ((Response)message).getResponsePhrase(DefaultConversationFormatter.FORMATTER);
		}

		public String getMessage() {
			return message;
		}
	}
}
