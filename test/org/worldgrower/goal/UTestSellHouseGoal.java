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
import org.worldgrower.attribute.IdList;
import org.worldgrower.generator.BuildingGenerator;

public class UTestSellHouseGoal {

	private SellHouseGoal goal = Goals.SELL_HOUSE_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	/*
	 //TODO: demand for houses doesn't work atm
	@Test
	public void testCalculateGoalSellHouse() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.PROFIT_PERCENTAGE, 0);
		
		WorldObject target = createPerformer();
		target.setProperty(Constants.GOLD, 1000);
		target.setProperty(Constants.DEMANDS, new PropertyCountMap<>());
		target.getProperty(Constants.DEMANDS).add(Constants.HOUSES, 1);
		target.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		
		int houseId = BuildingGenerator.generateHouse(5, 5, world, 1f);
		performer.getProperty(Constants.HOUSES).add(houseId);
		
		assertEquals(Actions.PLANT_COTTON_PLANT_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	*/
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		int houseId = BuildingGenerator.generateHouse(5, 5, world, 1f);
		performer.getProperty(Constants.HOUSES).add(houseId);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}