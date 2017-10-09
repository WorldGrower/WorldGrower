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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.MockTerrain;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.terrain.TerrainType;

public class UTestCattleOnTurn {

	@Test
	public void testOnTurnOfCommonAttributes() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		createVillagersOrganization(world);
		
		WorldObject cow = createCow(world, organization);
		assertEquals(1, cow.getProperty(Constants.MEAT_SOURCE).intValue());
		
		cow.onTurn(world, new WorldStateChangedListeners());
		assertEquals(1, cow.getProperty(Constants.MEAT_SOURCE).intValue());
		
		for(int i=0; i<500; i++) { world.nextTurn(); }
		cow.onTurn(world, new WorldStateChangedListeners());
		assertEquals(2, cow.getProperty(Constants.MEAT_SOURCE).intValue());
	}
	
	@Test
	public void testGiveBirth() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());

		createVillagersOrganization(world);
		
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject cow = createCow(world, organization);
		cow.setProperty(Constants.PREGNANCY, 0);
		
		world = new MockWorld(new MockTerrain(TerrainType.GRASLAND), world);
		assertEquals(4, world.getWorldObjects().size());
		
		for(int i=0; i<700; i++) {
			world.nextTurn();
			cow.onTurn(world, new WorldStateChangedListeners());
		}
		
		assertEquals(5, world.getWorldObjects().size());
	}
	
	@Test
	public void testCheckLeash() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());

		createVillagersOrganization(world);
		
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject cow = createCow(world, organization);
		cow.setProperty(Constants.X, 0);
		cow.setProperty(Constants.Y, 0);
		
		WorldObject owner = TestUtils.createIntelligentWorldObject(7, "Test");
		owner.setProperty(Constants.X, 2);
		owner.setProperty(Constants.Y, 2);
		world.addWorldObject(owner);
		
		cow.setProperty(Constants.LEASH_ID, owner.getProperty(Constants.ID));
		cow.onTurn(world, new WorldStateChangedListeners());
		
		assertEquals(1, cow.getProperty(Constants.X).intValue());
		assertEquals(1, cow.getProperty(Constants.Y).intValue());
	}
	
	private WorldObject createCow(World world, WorldObject organization) {
		int cowId = new CreatureGenerator(organization).generateCow(0, 0, world);
		WorldObject cow = world.findWorldObjectById(cowId);
		return cow;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
	}

}