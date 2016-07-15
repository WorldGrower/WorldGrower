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
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestCatchFishAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.FISHING_POLE.generate(1f));
		WorldObject target = createFish(world);
		
		assertEquals(1, target.getProperty(Constants.FOOD_SOURCE).intValue());
		Actions.CATCH_FISH_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(8, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
		assertEquals(false, world.exists(target));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createFish(world);
		
		assertEquals(false, Actions.CATCH_FISH_ACTION.isValidTarget(performer, performer, world));
		assertEquals(true, Actions.CATCH_FISH_ACTION.isValidTarget(performer, target, world));
	}

	private WorldObject createFish(World world) {
		WorldObject organization = GroupPropertyUtils.create(null, "vermin", world);
		int id = new CreatureGenerator(organization).generateFish(0, 0, world);
		WorldObject target = world.findWorldObjectById(id);
		return target;
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createFish(world);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.FISHING_POLE.generate(1f));
		
		assertEquals(true, Actions.CATCH_FISH_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createFish(world);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.FISHING_POLE.generate(1f));
		
		assertEquals(0, Actions.CATCH_FISH_ACTION.distance(performer, target, Args.EMPTY, world));
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