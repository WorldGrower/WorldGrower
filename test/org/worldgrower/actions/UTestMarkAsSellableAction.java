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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestMarkAsSellableAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		Actions.MARK_AS_SELLABLE_ACTION.execute(performer, target, new int[] {0}, world);
		
		assertEquals(true, target.getProperty(Constants.SELLABLE));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.HOUSES, new IdList());
		performer.setProperty(Constants.NAME, "performer");
		
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		performer.getProperty(Constants.HOUSES).add(target);
		assertEquals(true, Actions.MARK_AS_SELLABLE_ACTION.isValidTarget(performer, target, world));
		
		assertEquals(false, Actions.MARK_AS_SELLABLE_ACTION.isValidTarget(performer, performer, world));
		
		performer.getProperty(Constants.HOUSES).remove(target);
		assertEquals(false, Actions.MARK_AS_SELLABLE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.HOUSES, new IdList());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.NAME, "performer");
		
		int id = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		performer.getProperty(Constants.HOUSES).add(target);
		assertEquals(0, Actions.MARK_AS_SELLABLE_ACTION.distance(performer, target, new int[0], world));
	}
}