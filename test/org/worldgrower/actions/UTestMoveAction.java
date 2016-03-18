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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.WorldObjectContainer;

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
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(false, Actions.MOVE_ACTION.isValidTarget(performer, target, world));
		assertEquals(true, Actions.MOVE_ACTION.isValidTarget(performer, performer, world));
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