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
package org.worldgrower.actions.magic;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestCurePoisonAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		Conditions.add(target, Condition.POISONED_CONDITION, 8, world);
		assertEquals(true, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
		
		Actions.CURE_POISON_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(false, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(false, Actions.CURE_POISON_ACTION.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.CURE_POISON_ACTION));
		target.setProperty(Constants.HIT_POINTS, 1);
		target.setProperty(Constants.ARMOR, 1);
		
		assertEquals(true, Actions.CURE_POISON_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(true, Actions.CURE_POISON_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, Actions.CURE_POISON_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testHasRequiredEnergy() {
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.ENERGY, 1000);
		assertEquals(true, Actions.CURE_POISON_ACTION.hasRequiredEnergy(performer));
		
		performer.setProperty(Constants.ENERGY, 0);
		assertEquals(false, Actions.CURE_POISON_ACTION.hasRequiredEnergy(performer));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}