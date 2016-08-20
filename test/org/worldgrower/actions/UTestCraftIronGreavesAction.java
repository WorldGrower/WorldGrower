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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestCraftIronGreavesAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createSmith(world, performer);
		Actions.CRAFT_IRON_GREAVES_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.ARMOR));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());

		WorldObject target = createSmith(world, performer);
		
		assertEquals(false, Actions.CRAFT_IRON_GREAVES_ACTION.isValidTarget(performer, performer, world));
		assertEquals(true, Actions.CRAFT_IRON_GREAVES_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		WorldObject target = createSmith(world, performer);
		
		assertEquals(true, Actions.CRAFT_IRON_GREAVES_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createSmith(world, performer);
		
		assertEquals(0, Actions.CRAFT_IRON_GREAVES_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createSmith(World world, WorldObject performer) {
		int smithId = BuildingGenerator.generateSmith(0, 0, world, performer);
		WorldObject target = world.findWorldObjectById(smithId);
		return target;
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