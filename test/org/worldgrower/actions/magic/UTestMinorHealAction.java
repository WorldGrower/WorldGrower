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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;

public class UTestMinorHealAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		target.setProperty(Constants.HIT_POINTS, 1 * Item.COMBAT_MULTIPLIER);
		target.setProperty(Constants.HIT_POINTS_MAX, 8 * Item.COMBAT_MULTIPLIER);
		
		Actions.MINOR_HEAL_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(6 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		
		Actions.MINOR_HEAL_ACTION.execute(performer, target, new int[0], world);
		assertEquals(8 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.MINOR_HEAL_ACTION));
		
		target.setProperty(Constants.ARMOR, 1);
		target.setProperty(Constants.HIT_POINTS, 1);
		target.setProperty(Constants.HIT_POINTS_MAX, 8);
		
		assertEquals(true, Actions.MINOR_HEAL_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		assertEquals(0, Actions.MINOR_HEAL_ACTION.distance(performer, target, new int[0], world));
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