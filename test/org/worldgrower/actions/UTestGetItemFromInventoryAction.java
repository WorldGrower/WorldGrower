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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestGetItemFromInventoryAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		target.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		Actions.GET_ITEM_FROM_INVENTORY_ACTION.execute(performer, target, new int[] { 0 }, world);
		
		assertEquals(0, target.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		target.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		assertEquals(true, Actions.GET_ITEM_FROM_INVENTORY_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		target.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		target.setProperty(Constants.LOCKED, Boolean.FALSE);
		
		assertEquals(0, Actions.GET_ITEM_FROM_INVENTORY_ACTION.distance(performer, target, new int[] { 0 }, world));
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