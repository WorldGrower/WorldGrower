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
import org.worldgrower.generator.PlantGenerator;

public class UTestHarvestCottonAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createCottonPlant(world);
		
		target.setProperty(Constants.COTTON_SOURCE, 100);
		Actions.HARVEST_COTTON_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.COTTON));
		assertEquals(80, target.getProperty(Constants.COTTON_SOURCE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createCottonPlant(world);
		
		target.setProperty(Constants.COTTON_SOURCE, 100);
		assertEquals(true, Actions.HARVEST_COTTON_ACTION.isValidTarget(performer, target, world));
		
		target.setProperty(Constants.COTTON_SOURCE, 0);
		assertEquals(false, Actions.HARVEST_COTTON_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createCottonPlant(world);

		assertEquals(true, Actions.HARVEST_COTTON_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createCottonPlant(world);
		
		assertEquals(0, Actions.HARVEST_COTTON_ACTION.distance(performer, target, Args.EMPTY, world));
	}

	private WorldObject createCottonPlant(World world) {
		int id = PlantGenerator.generateCottonPlant(0, 0, world);
		WorldObject target = world.findWorldObjectById(id);
		return target;
	}
}