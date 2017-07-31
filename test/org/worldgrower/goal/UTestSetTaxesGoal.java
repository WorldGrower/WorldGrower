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
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestSetTaxesGoal {

	private SetTaxesGoal goal = Goals.SET_TAXES_GOAL;
	
	@Test
	public void testCalculateGoalSetTaxRate() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		createVillagersOrganization(world);
		
		assertEquals(Actions.SET_GOVERNANCE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateAndPerformGoalSetTaxRate() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.SHACK_TAX_RATE, 5);
		villagersOrganization.setProperty(Constants.HOUSE_TAX_RATE, 6);
		villagersOrganization.setProperty(Constants.SHERIFF_WAGE, 7);
		villagersOrganization.setProperty(Constants.TAX_COLLECTOR_WAGE, 8);
		
		OperationInfo operationInfo = goal.calculateGoal(performer, world);
		operationInfo.perform(world);
		assertEquals(0, villagersOrganization.getProperty(Constants.SHACK_TAX_RATE).intValue());
		assertEquals(0, villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE).intValue());
		assertEquals(10, villagersOrganization.getProperty(Constants.SHERIFF_WAGE).intValue());
		assertEquals(10, villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE).intValue());
	}

	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		world.addWorldObject(performer);
		
		WorldObject organization = createVillagersOrganization(world);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		assertEquals(true, goal.isGoalMet(performer, world));
		
		organization.setProperty(Constants.SHACK_TAX_RATE, 1);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		int shackId = BuildingGenerator.generateShack(0, 0, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(shackId, BuildingType.SHACK);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		return organization;
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(7, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}