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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestFireTaxCollectorGoal {

	private FireTaxCollectorGoal goal = Goals.FIRE_TAX_COLLECTOR_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalFireTaxCollector() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer();
		createVillagersOrganization(world);
		
		GroupPropertyUtils.setVillageLeader(performer.getProperty(Constants.ID), world);
		addTaxCollector(7, 0, world);
		worldNextTurn(500, world);
		
		assertEquals(Actions.FIRE_PUBLIC_EMPLOYEE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer();
		createVillagersOrganization(world);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		GroupPropertyUtils.setVillageLeader(performer.getProperty(Constants.ID), world);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		addTaxCollector(7, 0, world);
		worldNextTurn(500, world);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("looking to fire tax collectors", DefaultConversationFormatter.FORMATTER.format(goal.getDescription()));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
	
	private void worldNextTurn(int amount, World world) {
		for(int i=0; i<amount; i++) {
			world.nextTurn();
		}
	}
	
	private WorldObject addTaxCollector(int id, int professionStartTurn, World world) {
		WorldObject taxCollector = TestUtils.createIntelligentWorldObject(id, Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		taxCollector.setProperty(Constants.PROFESSION_START_TURN, professionStartTurn);
		world.addWorldObject(taxCollector);
		return taxCollector;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId(); 
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}