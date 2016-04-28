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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestClaimHouseAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.HOUSES, new IdList());
		
		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject house = world.findWorldObject(Constants.ID, houseId);
		Actions.CLAIM_HOUSE_ACTION.execute(performer, house, Args.EMPTY, world);
		
		assertEquals(true, performer.getProperty(Constants.HOUSES).contains(houseId));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject house = world.findWorldObject(Constants.ID, houseId);
		
		assertEquals(true, Actions.CLAIM_HOUSE_ACTION.isValidTarget(performer, house, world));
		assertEquals(false, Actions.CLAIM_HOUSE_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		WorldObject house = world.findWorldObject(Constants.ID, houseId);
		
		performer.setProperty(Constants.HOUSES, new IdList().add(house));
		
		assertEquals(0, Actions.CLAIM_HOUSE_ACTION.distance(performer, house, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}