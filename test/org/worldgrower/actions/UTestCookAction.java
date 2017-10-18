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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestCookAction {

	private final CookAction action = Actions.COOK_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.ENERGY, 500);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.MEAT.generate(1f));
		
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject house = world.findWorldObjectById(houseId);
		action.execute(performer, house, new int[] { 0 }, world);
		
		assertEquals(2, performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.FOOD).intValue() );
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject house = world.findWorldObjectById(houseId);
		
		assertEquals(true, action.isValidTarget(performer, house, world));
		assertEquals(false, action.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject house = world.findWorldObjectById(houseId);
		
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(house, BuildingType.HOUSE));
		assertEquals(false, action.isActionPossible(performer, house, Args.EMPTY, world));
		
		house.getProperty(Constants.INVENTORY).add(Item.KITCHEN.generate(1f));
		assertEquals(true, action.isActionPossible(performer, house, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject house = world.findWorldObjectById(houseId);
		
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(house, BuildingType.HOUSE));
		
		assertEquals(0, action.distance(performer, house, Args.EMPTY, world));
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