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
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestChooseDeityGoal {

	private ChooseDeityGoal goal = Goals.CHOOSE_DEITY_GOAL;
	
	@Test
	public void testCalculateGoalChooseDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.NAME, "Test");
		performer.setProperty(Constants.CHILDREN, new IdList());
		
		assertEquals(Actions.CHOOSE_DEITY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalChooseFarmerDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.setProperty(Constants.NAME, "Test");
		performer.setProperty(Constants.CHILDREN, new IdList());
		
		assertEquals(Actions.CHOOSE_DEITY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(Deity.ALL_DEITIES.indexOf(Deity.DEMETER)));
	}
	
	@Test
	public void testCalculateGoalChoosePriestDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		
		assertEquals(Actions.CHOOSE_DEITY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		int deityIndex = goal.calculateGoal(performer, world).getArgs()[0];
		assertEquals(Deity.APHRODITE, Deity.ALL_DEITIES.get(deityIndex));
		
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.DEITY, Deity.HADES);
		world.addWorldObject(target);
		
		WorldObject target2 = createPerformer(4);
		target2.setProperty(Constants.DEITY, Deity.HADES);
		world.addWorldObject(target2);
		
		assertEquals(Actions.CHOOSE_DEITY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		deityIndex = goal.calculateGoal(performer, world).getArgs()[0];
		assertEquals(Deity.HADES, Deity.ALL_DEITIES.get(deityIndex));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.DEITY, Deity.ARES);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.CHILDREN, new IdList());
		return performer;
	}
}