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
import org.worldgrower.MockTerrain;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.terrain.TerrainType;

public class UTestMoveAction {

	@Test
	public void testMoveDirection() {
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.LOOK_DIRECTION, null);
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { -1, -1 });
		assertEquals(LookDirection.WEST, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { -1, 0 });
		assertEquals(LookDirection.WEST, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { -1, 1 });
		assertEquals(LookDirection.WEST, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 0, -1 });
		assertEquals(LookDirection.NORTH, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 0, 0 });
		assertEquals(LookDirection.NORTH, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 0, 1 });
		assertEquals(LookDirection.SOUTH, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 1, -1 });
		assertEquals(LookDirection.EAST, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 1, 0 });
		assertEquals(LookDirection.EAST, performer.getProperty(Constants.LOOK_DIRECTION));
		
		Actions.MOVE_ACTION.setLookDirection(performer, new int[] { 1, 1 });
		assertEquals(LookDirection.EAST, performer.getProperty(Constants.LOOK_DIRECTION));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(false, Actions.MOVE_ACTION.isValidTarget(performer, target, world));
		assertEquals(true, Actions.MOVE_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		
		assertEquals(0, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world));
	}
	
	@Test
	public void testDistanceMapEdge() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		
		assertEquals(true, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world) > 0);
	}
	
	@Test
	public void testDistanceWater() {
		World world = new MockWorld(new MockTerrain(TerrainType.WATER), new WorldImpl(10, 10, null, null));
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		
		assertEquals(true, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world) > 0);
	}
	
	@Test
	public void testDistanceWaterWalking() {
		World world = new MockWorld(new MockTerrain(TerrainType.WATER), new WorldImpl(10, 10, null, null));
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(performer, Condition.WATER_WALK_CONDITION, 8, world);
		
		assertEquals(0, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world));
	}
	
	@Test
	public void testDistanceWaterWalkingWithObstacle() {
		World world = new MockWorld(new MockTerrain(TerrainType.WATER), new WorldImpl(10, 10, null, null));
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(performer, Condition.WATER_WALK_CONDITION, 8, world);
		
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.X, 1);
		target.setProperty(Constants.Y, 1);
		world.addWorldObject(target);
		
		assertEquals(true, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world) > 0);
	}
	
	@Test
	public void testDistanceObstacle() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());

		WorldObject target = createPerformer(3);
		target.setProperty(Constants.X, 1);
		target.setProperty(Constants.Y, 1);
		world.addWorldObject(target);
		
		assertEquals(true, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world) > 0);
	}
	
	@Test
	public void testDistanceEnlargedMoveDownRight() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		world.addWorldObject(performer);

		Actions.ENLARGE_ACTION.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(0, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 1}, world));
	}
	
	@Test
	public void testDistanceEnlargedMoveRight() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		world.addWorldObject(performer);

		Actions.ENLARGE_ACTION.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(0, Actions.MOVE_ACTION.distance(performer, performer, new int[] {1, 0}, world));
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