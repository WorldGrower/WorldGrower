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

public class UTestCutWoodAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createTree(world);
		
		assertEquals(true, target.getProperty(Constants.WOOD_SOURCE).hasEnoughWood());
		Actions.CUT_WOOD_ACTION.execute(performer, target, Args.EMPTY, world);
		Actions.CUT_WOOD_ACTION.execute(performer, target, Args.EMPTY, world);
		Actions.CUT_WOOD_ACTION.execute(performer, target, Args.EMPTY, world);
		Actions.CUT_WOOD_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(4, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD));
		assertEquals(false, target.getProperty(Constants.WOOD_SOURCE).hasEnoughWood());
	}
	
	@Test
	public void testExecuteHitPointsZero() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createTree(world);
		target.setProperty(Constants.WOOD_SOURCE, new TreeWoodSource(50));
		
		Actions.CUT_WOOD_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD));
		assertEquals(false, target.getProperty(Constants.WOOD_SOURCE).hasEnoughWood());
		assertEquals(0, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createTree(world);
		
		assertEquals(0, Actions.CUT_WOOD_ACTION.distance(performer, target, Args.EMPTY, world));
	}

	private WorldObject createTree(World world) {
		int treeId = PlantGenerator.generateOldTree(0, 0, world);
		WorldObject target = world.findWorldObjectById(treeId);
		return target;
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}