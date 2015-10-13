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
package org.worldgrower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.worldgrower.actions.Actions;

public class UTestOperationInfo {
	
	@Test
	public void testConstructorPerformerNull() {
		try {
			new OperationInfo(null, null, null, null);
			fail("method should fail");
		} catch(IllegalArgumentException ex) {
			assertEquals("performer is null", ex.getMessage());
		}
	}
	
	@Test
	public void testSearchAnyPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		assertEquals(true, info.searchAnyPerformer(info));
	}
	
	@Test
	public void testSearchAnyPerformerWithFacade() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.FACADE, TestUtils.createWorldObject(3, "facade"));
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		assertEquals(false, info.searchAnyPerformer(info));
	}
	
	@Test
	public void testTargetMovedNotMoved() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(target, target);
		assertEquals(false, info.targetMoved(world));
	}
	
	@Test
	public void testTargetMovedNotMovedTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		assertEquals(false, info.targetMoved(world));
	}
	
	@Test
	public void testTargetMovedMoveAction() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		
		world.getHistory().actionPerformed(new OperationInfo(target, target, new int[0], Actions.MOVE_ACTION), null);
		assertEquals(true, info.targetMoved(world));
	}
	
	@Test
	public void testRemoveDeadWorldObjectsUnintelligentHitPoints() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.HIT_POINTS, 0);
		performer.setProperty(Constants.ID, 7);
		world.addWorldObject(performer);
		
		OperationInfo.removeDeadWorldObjects(world);
		assertEquals(0, world.getWorldObjects().size());
	}
	
	@Test
	public void testRemoveDeadWorldObjectsWood() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.WOOD_SOURCE, 0);
		performer.setProperty(Constants.ID, 7);
		world.addWorldObject(performer);
		
		OperationInfo.removeDeadWorldObjects(world);
		assertEquals(0, world.getWorldObjects().size());
	}
	
	@Test
	public void testRemoveDeadWorldObjectsIntelligentHitPoints() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "test");
		performer.setProperty(Constants.ID, 7);
		performer.setProperty(Constants.X, 7);
		performer.setProperty(Constants.Y, 7);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.HIT_POINTS, 0);
		performer.setProperty(Constants.GOLD, 0);
		performer.setProperty(Constants.DEATH_REASON, "death");
		world.addWorldObject(performer);
		
		OperationInfo.removeDeadWorldObjects(world);
		assertEquals(1, world.getWorldObjects().size());
		assertEquals("skeletal remains of test", world.getWorldObjects().get(0).getProperty(Constants.NAME));
	}
	
	@Test
	public void testIsPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		
		assertEquals(true, info.isPerformer(performer));
		assertEquals(false, info.isPerformer(target));
	}
	
	@Test
	public void testGetSecondPersonDescription() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		
		assertEquals("You were attacking target", info.getSecondPersonDescription(world));
	}
	
	@Test
	public void testGetThirdPersonDescription() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		OperationInfo info = createOperationInfo(performer, target);
		
		assertEquals("performer was attacking target", info.getThirdPersonDescription(world));
	}

	private OperationInfo createOperationInfo(WorldObject performer, WorldObject target) {
		
		OperationInfo info = new OperationInfo(performer, target, new int[0], Actions.MELEE_ATTACK_ACTION);
		return info;
	}
}
