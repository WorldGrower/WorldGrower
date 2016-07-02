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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestLockUtils {

	@Test
	public void testPerformerHasKey() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createWorldObject(1, "Test2");
		world.addWorldObject(target);
		
		assertEquals(false, LockUtils.performerHasKey(performer, target));
		
		WorldObject key = Item.generateKey(target.getProperty(Constants.ID), world);
		performer.getProperty(Constants.INVENTORY).add(key);
		assertEquals(true, LockUtils.performerHasKey(performer, target));
		assertEquals("key to Test2", key.getProperty(Constants.NAME));
	}
	
	@Test
	public void testPerformerHasHouseKey() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createWorldObject(1, "Test2");
		
		for(int i=0; i<256; i++) { world.generateUniqueId(); }
		
		Actions.BUILD_HOUSE_ACTION.execute(performer, target, Args.EMPTY, world);
		int houseId = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).get(0);
		WorldObject house = world.findWorldObject(Constants.ID, houseId);

		assertEquals(true, LockUtils.performerHasKey(performer, house));
	}
	
	@Test
	public void testPerformerIsMagicLockCreator() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createWorldObject(1, "Test2");
		
		assertEquals(false, LockUtils.performerIsMagicLockCreator(performer, target));
		
		target.setProperty(Constants.MAGIC_LOCK_CREATOR_ID, 0);
		assertEquals(true, LockUtils.performerIsMagicLockCreator(performer, target));
	}
	
	@Test
	public void testDistance() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createWorldObject(1, "Test2");
		
		assertEquals(true, LockUtils.performerCanAccessContainer(performer, target));
		
		target.setProperty(Constants.LOCKED, Boolean.TRUE);
		assertEquals(false, LockUtils.performerCanAccessContainer(performer, target));
		
		target.setProperty(Constants.MAGIC_LOCK_CREATOR_ID, 0);
		assertEquals(true, LockUtils.performerCanAccessContainer(performer, target));
	}
}
