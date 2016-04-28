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
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestHandoverTaxesGoal {

	private HandoverTaxesGoal goal = Goals.HANDOVER_TAXES_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalPerformerIsLeader() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		WorldObject organization = createVillagersOrganization(world);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalHandoverTaxes() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		WorldObject organization = createVillagersOrganization(world);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		assertEquals(Actions.HANDOVER_TAXES_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.ORGANIZATION_GOLD, 0);
		WorldObject target = createPerformer(3);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		WorldObject organization = createVillagersOrganization(world);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.ORGANIZATION_GOLD, 500);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
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