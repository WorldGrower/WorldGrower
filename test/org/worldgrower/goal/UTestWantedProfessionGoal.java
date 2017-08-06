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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WantedProfession;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.history.Turn;

public class UTestWantedProfessionGoal {

	private WantedProfessionGoal goal = Goals.WANTED_PROFESSION_GOAL;
	
	@Test
	public void testCalculateGoal() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(7);
		performer.setProperty(Constants.WANTED_PROFESSION, WantedProfession.TAX_COLLECTOR);
		WorldObject leader = createPerformer(8);
		world.addWorldObject(leader);
		createVillagersOrganization(world);
		GroupPropertyUtils.setVillageLeader(leader.getProperty(Constants.ID), world);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalPreviousResponseNegative() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(7);
		performer.setProperty(Constants.WANTED_PROFESSION, WantedProfession.TAX_COLLECTOR);
		WorldObject leader = createPerformer(8);
		world.addWorldObject(leader);
		createVillagersOrganization(world);
		GroupPropertyUtils.setVillageLeader(leader.getProperty(Constants.ID), world);
		
		world.getHistory().actionPerformed(new OperationInfo(performer, leader, Conversations.createArgs(Conversations.CAN_COLLECT_TAXES_CONVERSATION, null, 1), Actions.TALK_ACTION), Turn.valueOf(0));
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(7);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.WANTED_PROFESSION, WantedProfession.TAX_COLLECTOR);
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("wanting to get hired", DefaultConversationFormatter.FORMATTER.format(goal.getDescription()));
	}
	
	WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		return organization;
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}