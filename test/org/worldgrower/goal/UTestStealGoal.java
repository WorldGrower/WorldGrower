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
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestStealGoal {

	private StealGoal goal = Goals.STEAL_GOAL;
	
	@Test
	public void testGetIndexOfWorldObjectToBeStolenNull() {
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(-1, StealGoal.getIndexOfWorldObjectToBeStolen(target));
	}
	
	@Test
	public void testGetIndexOfWorldObjectToBeStolenMostValuable() {
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		target.getProperty(Constants.INVENTORY).add(Item.generateSpellBook(Actions.MINOR_HEAL_ACTION));
		target.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		assertEquals(0, StealGoal.getIndexOfWorldObjectToBeStolen(target));
	}
	
	@Test
	public void testIsValidThievingTarget() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 200);
		target.getProperty(Constants.INVENTORY).add(Item.generateSpellBook(Actions.MINOR_HEAL_ACTION));
		target.setProperty(Constants.GOLD, 200);
		
		assertEquals(true, goal.isValidThievingTarget(performer, target));
		
		assertEquals(false, goal.isValidThievingTarget(performer, performer));
		
		target.setProperty(Constants.GOLD, 0);
		assertEquals(false, goal.isValidThievingTarget(performer, target));
	}
	
	@Test
	public void testCalculateGoalTwoTargets() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target1 = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target2 = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target1);
		world.addWorldObject(target2);
		
		target1.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		target2.getProperty(Constants.INVENTORY).add(Item.generateSpellBook(Actions.MINOR_HEAL_ACTION));
		target1.setProperty(Constants.GOLD, 200);
		target2.setProperty(Constants.GOLD, 200);
		
		assertEquals(Actions.STEAL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target2, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalStealMoney() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		target.setProperty(Constants.GOLD, 2000);
		
		assertEquals(Actions.STEAL_GOLD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 500);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.GOLD, 0);
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("in need of gold", DefaultConversationFormatter.FORMATTER.format(goal.getDescription()));
	}
}
