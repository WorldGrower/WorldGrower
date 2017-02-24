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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestMarkHouseAsSellableGoal {

	private MarkBuildingAsSellableGoal goal = Goals.MARK_HOUSE_AS_SELLABLE_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		int houseId = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalMarkHouseAsSellable() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		int houseId = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		
		int houseId2 = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId2, BuildingType.HOUSE);
		
		assertEquals(Actions.MARK_AS_SELLABLE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		int houseId = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		
		int houseId2 = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId2, BuildingType.HOUSE);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		WorldObject house2 = world.findWorldObjectById(houseId2);
		Actions.MARK_AS_SELLABLE_ACTION.execute(performer, house2, Args.EMPTY, world);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("marking houses as sellable", DefaultConversationFormatter.FORMATTER.format(goal.getDescription()));
	}
	
	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		return performer;
	}
}