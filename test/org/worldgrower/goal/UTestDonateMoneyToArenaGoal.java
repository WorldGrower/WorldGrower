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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestDonateMoneyToArenaGoal {

	private DonateMoneyToArenaGoal goal = Goals.DONATE_MONEY_TO_ARENA_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalDonateMoney() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.GOLD, 100);
		world.addWorldObject(performer);
		
		WorldObject arenaOwner = createPerformer(3);
		arenaOwner.setProperty(Constants.ARENA_IDS, new IdList().add(7));
		arenaOwner.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList());
		world.addWorldObject(arenaOwner);
		
		assertEquals(Actions.DONATE_MONEY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalNoMoney() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.GOLD, 0);
		world.addWorldObject(performer);
		
		WorldObject arenaOwner = createPerformer(3);
		arenaOwner.setProperty(Constants.ARENA_IDS, new IdList().add(7));
		arenaOwner.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList());
		world.addWorldObject(arenaOwner);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.GOLD, 0);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.ARENA_DONATED_TURN, 300);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.ARENA_DONATED_TURN, 0);
		performer.setProperty(Constants.GOLD, 100);
		for(int i=0; i<600; i++) { world.nextTurn(); }
		assertEquals(false, goal.isGoalMet(performer, world));
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