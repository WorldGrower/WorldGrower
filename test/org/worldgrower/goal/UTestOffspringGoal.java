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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestOffspringGoal {

	private OffspringGoal goal = Goals.OFFSPRING_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalSexAction() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.GENDER, "female");
		target.setProperty(Constants.GENDER, "male");
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		assertEquals(Actions.SEX_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.CHILDREN, new IdList());
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.CHILDREN).add(6).add(7).add(8).add(9);
		assertEquals(true, goal.isGoalMet(performer, world));
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