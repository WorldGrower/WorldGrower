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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestPutItemInInventoryAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createHouse(world, performer);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		Actions.PUT_ITEM_INTO_INVENTORY_ACTION.execute(performer, target, new int[] { 0 }, world);
		
		assertEquals(1, target.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createHouse(world, performer);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		assertEquals(true, Actions.PUT_ITEM_INTO_INVENTORY_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createHouse(world, performer);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		target.setProperty(Constants.LOCKED, Boolean.FALSE);
		
		assertEquals(true, Actions.PUT_ITEM_INTO_INVENTORY_ACTION.isActionPossible(performer, target, new int[] { 0 }, world));
	}

	private WorldObject createHouse(World world, WorldObject performer) {
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		return target;
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createHouse(world, performer);
		
		assertEquals(0, Actions.PUT_ITEM_INTO_INVENTORY_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testAccessToHouseContainer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		Actions.BUILD_HOUSE_ACTION.execute(performer, target, Args.EMPTY, world);
		int houseId = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).get(0);
		WorldObject house = world.findWorldObject(Constants.ID, houseId);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		assertEquals(true, Actions.PUT_ITEM_INTO_INVENTORY_ACTION.isActionPossible(performer, house, new int[] { 0 }, world));
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