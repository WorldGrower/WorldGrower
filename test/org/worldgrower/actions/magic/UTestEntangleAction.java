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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;

public class UTestEntangleAction {

	private EntangleAction action = Actions.ENTANGLE_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(performer);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENTANGLED_CONDITION));
	}
	
	@Test
	public void testExecuteMultipleTargets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		BuildingGenerator.generateShack(0, 0, world, 1f, performer);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.WIDTH, 4);
		target.setProperty(Constants.HEIGHT, 4);
		
		WorldObject target2 = createPerformer(3);
		world.addWorldObject(target2);
		
		action.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENTANGLED_CONDITION));
		assertEquals(false, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENTANGLED_CONDITION));
		assertEquals(true, target2.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENTANGLED_CONDITION));
	}

	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.ENTANGLE_ACTION));
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