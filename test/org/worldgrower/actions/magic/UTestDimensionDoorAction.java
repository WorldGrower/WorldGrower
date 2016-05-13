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
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestDimensionDoorAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(2, 3, 1, 1);
		
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		
		Actions.DIMENSION_DOOR_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(2, performer.getProperty(Constants.X).intValue());
		assertEquals(3, performer.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testExecuteFailedTeleport() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(2, 3, 1, 1);
		
		PlantGenerator.generateTree(2, 3, world);
		
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		
		Actions.DIMENSION_DOOR_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(0, performer.getProperty(Constants.X).intValue());
		assertEquals(0, performer.getProperty(Constants.Y).intValue());
		assertEquals(5* Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.DIMENSION_DOOR_ACTION));
		assertEquals(true, Actions.DIMENSION_DOOR_ACTION.isValidTarget(performer, target, world));
		
		performer.setProperty(Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, Actions.DIMENSION_DOOR_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		
		assertEquals(0, Actions.DIMENSION_DOOR_ACTION.distance(performer, target, Args.EMPTY, world));
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