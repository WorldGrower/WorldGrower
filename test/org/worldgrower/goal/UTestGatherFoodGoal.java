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
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestGatherFoodGoal {

	private GatherFoodGoal goal = Goals.GATHER_FOOD_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalHarvestTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		int berryBushId = PlantGenerator.generateBerryBush(5, 5, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.getProperty(Constants.FOOD_SOURCE).increaseFoodAmount(500, berryBush, world);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.HARVEST_FOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalHarvestTargetFarAway() {
		World world = new WorldImpl(25, 25, null, null);
		WorldObject performer = createPerformer(2);
		
		int berryBushId = PlantGenerator.generateBerryBush(20, 20, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.getProperty(Constants.FOOD_SOURCE).increaseFoodAmount(500, berryBush, world);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.PLANT_BERRY_BUSH_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalButcherTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(TestUtils.createIntelligentWorldObject(1, "cow"));
		int cowId = creatureGenerator.generateCow(5, 5, world);
		WorldObject cow = world.findWorldObjectById(cowId);
		cow.setProperty(Constants.MEAT_SOURCE, 20);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.BUTCHER_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalButcherClaimedTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(TestUtils.createIntelligentWorldObject(1, "cow"));
		int cowId = creatureGenerator.generateCow(5, 5, world);
		WorldObject cow = world.findWorldObjectById(cowId);
		cow.setProperty(Constants.MEAT_SOURCE, 20);
		cow.setProperty(Constants.CATTLE_OWNER_ID, 7);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.PLANT_BERRY_BUSH_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalButcherTargetFarAway() {
		World world = new WorldImpl(25, 25, null, null);
		WorldObject performer = createPerformer(2);
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(TestUtils.createIntelligentWorldObject(1, "cow"));
		int cowId = creatureGenerator.generateCow(20, 20, world);
		WorldObject cow = world.findWorldObjectById(cowId);
		cow.setProperty(Constants.MEAT_SOURCE, 20);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.PLANT_BERRY_BUSH_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.FOOD, 0);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f), 50);
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
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