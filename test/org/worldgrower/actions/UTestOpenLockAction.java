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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestOpenLockAction {

	private OpenLockAction action = Actions.OPEN_LOCK_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.LOCKPICK.generate(1f));
		WorldObject target = createUnownedHouse(performer, world);
		
		assertEquals(true, target.getProperty(Constants.LOCKED));
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(false, target.getProperty(Constants.LOCKED));
	}
	
	@Test
	public void testExecuteFailure() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		for (int i=0; i<250; i++) {
			world.nextTurn();
		}
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.LOCKPICK.generate(1f));
		WorldObject target = createUnownedHouse(performer, world);
		target.setProperty(Constants.LOCK_STRENGTH, 1000);
		
		assertEquals(true, target.getProperty(Constants.LOCKED));
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(true, target.getProperty(Constants.LOCKED));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createUnownedHouse(performer, world);
		
		assertEquals(false, action.isValidTarget(performer, performer, world));
		assertEquals(true, action.isValidTarget(performer, target, world));
	}

	private WorldObject createUnownedHouse(WorldObject performer, World world) {
		int id = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject target = world.findWorldObjectById(id);
		return target;
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createUnownedHouse(performer, world);
		
		assertEquals(true, action.isActionPossible(performer, target, Args.EMPTY, world));
	}

	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createUnownedHouse(performer, world);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.LOCKPICK.generate(1f));
		
		assertEquals(0, action.distance(performer, target, Args.EMPTY, world));
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