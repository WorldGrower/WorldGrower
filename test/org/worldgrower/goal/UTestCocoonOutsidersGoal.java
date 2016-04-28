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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestCocoonOutsidersGoal {

	private CocoonOutsidersGoal goal = Goals.COCOON_OUTSIDERS_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalCocoonOutsider() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		target.getProperty(Constants.GROUP).removeAll();
		Conditions.add(target, Condition.PARALYZED_CONDITION, 8, world);
		
		assertEquals(Actions.COCOON_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalAlreadyCocooned() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		target.getProperty(Constants.GROUP).removeAll();
		Conditions.add(target, Condition.COCOONED_CONDITION, 8, world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		target.getProperty(Constants.GROUP).removeAll();
		Conditions.add(target, Condition.PARALYZED_CONDITION, 8, world);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createIntelligentWorldObject(id, "person");
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ARMOR, 1);
		performer.setProperty(Constants.HIT_POINTS, 1);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		return performer;
	}
}