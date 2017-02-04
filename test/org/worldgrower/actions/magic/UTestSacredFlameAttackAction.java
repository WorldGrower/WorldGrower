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
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.Item;

public class UTestSacredFlameAttackAction {

	private SacredFlameAttackAction action = Actions.SACRED_FLAME_ATTACK_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		target.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		target.setProperty(Constants.HIT_POINTS_MAX, 10 * Item.COMBAT_MULTIPLIER);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(2 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testExecuteWithHadesBoon() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		Conditions.addPermanent(performer, Deity.HADES.getBoon(), world);
		target.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		target.setProperty(Constants.HIT_POINTS_MAX, 10 * Item.COMBAT_MULTIPLIER);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(2 * Item.COMBAT_MULTIPLIER - (8 * Item.COMBAT_MULTIPLIER) / 10, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(action));
		target.setProperty(Constants.HIT_POINTS, 10);
		target.setProperty(Constants.ARMOR, 10);
		
		assertEquals(false, action.isValidTarget(performer, target, world));
		
		VampireUtils.vampirizePerson(target, new WorldStateChangedListeners());
		assertEquals(true, action.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, action.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(true, action.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(0, action.distance(performer, target, Args.EMPTY, world));
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