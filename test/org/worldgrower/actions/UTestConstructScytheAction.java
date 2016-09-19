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

public class UTestConstructScytheAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createWorkbench(world, performer);
		Actions.CONSTRUCT_SCYTHE_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SCYTHE_QUALITY));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		WorldObject workbench = createWorkbench(world, performer);
		
		assertEquals(true, Actions.CONSTRUCT_SCYTHE_ACTION.isValidTarget(performer, workbench, world));
		assertEquals(false, Actions.CONSTRUCT_SCYTHE_ACTION.isValidTarget(performer, performer, world));
	}

	private WorldObject createWorkbench(World world, WorldObject performer) {
		int workbenchId = BuildingGenerator.generateWorkbench(0, 0, world, performer);
		WorldObject workbench = world.findWorldObjectById(workbenchId);
		return workbench;
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		WorldObject workbench = createWorkbench(world, performer);
		
		assertEquals(true, Actions.CONSTRUCT_SCYTHE_ACTION.isActionPossible(performer, workbench, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject workbench = createWorkbench(world, performer);
		
		assertEquals(0, Actions.CONSTRUCT_SCYTHE_ACTION.distance(performer, workbench, Args.EMPTY, world));
	}
}