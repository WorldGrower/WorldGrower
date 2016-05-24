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
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestClaimBuildingAction {

	private ClaimBuildingAction action = Actions.CLAIM_BUILDING_ACTION;

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		WorldObject brewery = generateBrewery(world, performer);
		assertEquals(0, performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.BREWERY).size());
		
		action.execute(performer, brewery, Args.EMPTY, world);
		
		assertEquals(brewery.getProperty(Constants.ID).intValue(), performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.BREWERY).get(0).intValue());
	}
	
	@Test
	public void testChangeName() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.NAME, "performer");
		
		WorldObject brewery = generateBrewery(world, performer);
		assertEquals("performer's brewery", brewery.getProperty(Constants.NAME));
		
		performer.setProperty(Constants.NAME, "tester");
		action.changeName(performer, brewery, world);
		
		assertEquals("tester's brewery", brewery.getProperty(Constants.NAME));
	}
	
	@Test
	public void testChangeKeyNames() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.NAME, "performer");
		world.addWorldObject(performer);
		
		WorldObject brewery = generateBrewery(world, performer);
		assertEquals("performer's brewery", brewery.getProperty(Constants.NAME));
		
		performer.getProperty(Constants.INVENTORY).add(Item.generateKey(brewery.getProperty(Constants.ID), world));
		assertEquals("key to performer's brewery", performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.NAME));
		
		brewery.setProperty(Constants.NAME, "tester's brewery");
		action.changeKeyNames(brewery, world);
		
		assertEquals("key to tester's brewery", performer.getProperty(Constants.INVENTORY).get(0).getProperty(Constants.NAME));
	}
	
	@Test
	public void testAddKeyToInventory() {
		
	}

	private WorldObject generateBrewery(World world, WorldObject performer) {
		int breweryId = BuildingGenerator.generateBrewery(0, 0, world, performer);
		WorldObject brewery = world.findWorldObject(Constants.ID, breweryId);
		return brewery;
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject brewery = generateBrewery(world, performer);
		
		assertEquals(true, action.isValidTarget(performer, brewery, world));
		assertEquals(false, action.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject brewery = generateBrewery(world, performer);
		
		assertEquals(0, action.distance(performer, brewery, Args.EMPTY, world));
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