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
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestCatchThievesGoal {

	private CatchThievesGoal goal = Goals.CATCH_THIEVES_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalTalkWithThief() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		WorldObject performer = createCommoner(world, villagersOrganization);
		WorldObject target = createCommoner(world, villagersOrganization);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 1000);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.COLLECT_BOUNTY_FROM_THIEVES_CONVERSATION);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		WorldObject target = createCommoner(world, organization);
		villagersOrganization.getProperty(Constants.BOUNTY).incrementValue(target, 1000);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		world.generateUniqueId();world.generateUniqueId();
		return villagersOrganization;
	}
}