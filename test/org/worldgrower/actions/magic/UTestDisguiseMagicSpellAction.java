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

public class UTestDisguiseMagicSpellAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		Actions.DISGUISE_MAGIC_SPELL_ACTION.execute(performer, performer, new int[] { 3}, world);
		
		assertEquals(3, performer.getProperty(Constants.FACADE).getProperty(Constants.ID).intValue());
	}

	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		assertEquals(true, Actions.DISGUISE_MAGIC_SPELL_ACTION.isValidTarget(performer, performer, world));
		assertEquals(false, Actions.DISGUISE_MAGIC_SPELL_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(true, Actions.DISGUISE_MAGIC_SPELL_ACTION.isActionPossible(performer, performer, Args.EMPTY, world));
	}
	
	@Test
	public void testGetDisguiseTargets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(new ArrayList<>(), Actions.DISGUISE_MAGIC_SPELL_ACTION.getDisguiseTargets(performer, world));
		
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		WorldObject target2 = createPerformer(3);
		target2.setProperty(Constants.WIDTH, 2);
		target2.setProperty(Constants.HEIGHT, 2);
		world.addWorldObject(target2);
		assertEquals(Arrays.asList(target), Actions.DISGUISE_MAGIC_SPELL_ACTION.getDisguiseTargets(performer, world));
	}
	
	@Test
	public void testHasRequiredEnergy() {
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.ENERGY, 1000);
		assertEquals(true, Actions.DISGUISE_MAGIC_SPELL_ACTION.hasRequiredEnergy(performer));
		
		performer.setProperty(Constants.ENERGY, 0);
		assertEquals(false, Actions.DISGUISE_MAGIC_SPELL_ACTION.hasRequiredEnergy(performer));
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