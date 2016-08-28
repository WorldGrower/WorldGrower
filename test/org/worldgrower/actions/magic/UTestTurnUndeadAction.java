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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.Item;

public class UTestTurnUndeadAction {

	private TurnUndeadAction action = Actions.TURN_UNDEAD_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject victim = createPerformer(3);
		victim.setProperty(Constants.X, 1);
		victim.setProperty(Constants.Y, 1);
		victim.setProperty(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		victim.setProperty(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		VampireUtils.vampirizePerson(victim, new WorldStateChangedListeners());
		world.addWorldObject(victim);
		
		action.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(7 * Item.COMBAT_MULTIPLIER, victim.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testGetAffectedTargets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		WorldObject target = createPerformer(3);
		VampireUtils.vampirizePerson(target, new WorldStateChangedListeners());
		world.addWorldObject(target);
		
		WorldObject target2 = createPerformer(4);
		world.addWorldObject(target2);
		
		assertEquals(Arrays.asList(target), action.getAffectedTargets(performer, world));
	}

	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(action));
		assertEquals(false, action.isValidTarget(performer, performer, world));
		assertEquals(true, action.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, action.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(0, action.distance(performer, performer, Args.EMPTY, world));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.FISHING_POLE.generate(1f));
		assertEquals(1, action.distance(performer, performer, Args.EMPTY, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(true, action.isActionPossible(performer, performer, Args.EMPTY, world));
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