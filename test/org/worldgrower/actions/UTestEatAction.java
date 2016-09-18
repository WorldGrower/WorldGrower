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

public class UTestEatAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.FOOD, 800);
		
		WorldObject berryBush = createBerryBush(world);
		Actions.EAT_ACTION.execute(performer, berryBush, Args.EMPTY, world);
		
		assertEquals(975, performer.getProperty(Constants.FOOD).intValue());
		assertEquals(200, berryBush.getProperty(Constants.FOOD_SOURCE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject berryBush = createBerryBush(world);
		assertEquals(true, Actions.EAT_ACTION.isValidTarget(performer, berryBush, world));
		
		berryBush.setProperty(Constants.FOOD_SOURCE, 0);
		assertEquals(false, Actions.EAT_ACTION.isValidTarget(performer, berryBush, world));
	}

	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject berryBush = createBerryBush(world);
		assertEquals(true, Actions.EAT_ACTION.isActionPossible(performer, berryBush, Args.EMPTY, world));
	}
	
	private WorldObject createBerryBush(World world) {
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.setProperty(Constants.FOOD_SOURCE, 300);
		return berryBush;
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