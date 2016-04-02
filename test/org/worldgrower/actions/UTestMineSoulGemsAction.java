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
import org.worldgrower.generator.TerrainGenerator;

public class UTestMineSoulGemsAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int stoneResourceId = TerrainGenerator.generateStoneResource(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, stoneResourceId);
		
		assertEquals(1000, target.getProperty(Constants.SOUL_GEM_SOURCE).intValue());
		Actions.MINE_SOUL_GEMS_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SOUL_GEM));
		assertEquals(999, target.getProperty(Constants.SOUL_GEM_SOURCE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int stoneResourceId = TerrainGenerator.generateStoneResource(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, stoneResourceId);
		
		assertEquals(true, Actions.MINE_SOUL_GEMS_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.MINE_SOUL_GEMS_ACTION.isValidTarget(performer, performer, world));
		
		target.setProperty(Constants.SOUL_GEM_SOURCE, 0);
		assertEquals(false, Actions.MINE_SOUL_GEMS_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int stoneResourceId = TerrainGenerator.generateStoneResource(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, stoneResourceId);
		
		assertEquals(0, Actions.MINE_SOUL_GEMS_ACTION.distance(performer, target, Args.EMPTY, world));
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