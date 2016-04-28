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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.TerrainGenerator;

public class UTestFillSoulGemGoal {

	private FillSoulGemGoal goal = Goals.FILL_SOUL_GEM_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalMineSoulGems() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		TerrainGenerator.generateStoneResource(0, 0, world);
		
		assertEquals(Actions.MINE_SOUL_GEMS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalSoulTrap() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		WorldObject target = createPerformer();
		performer.setProperty(Constants.ENERGY, 1000);
		target.setProperty(Constants.CONDITIONS, new Conditions());
		world.addWorldObject(target);
		Conditions.add(target, Condition.POISONED_CONDITION, 8, world);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.SOUL_TRAP_ACTION));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SOUL_GEM.generate(1f), 10);
		
		assertEquals(Actions.SOUL_TRAP_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		for(int i=0; i<10; i++) {
			WorldObject soulGem = Item.SOUL_GEM.generate(1f);
			soulGem.setProperty(Constants.SOUL_GEM_FILLED, Boolean.TRUE);
			performer.getProperty(Constants.INVENTORY).addQuantity(soulGem);
		}
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}