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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestEggOnTurn {

	@Test
	public void testGiveBirth() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());

		createVillagersOrganization(world);
		
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject egg = createEgg(world, organization);
		
		assertEquals(4, world.getWorldObjects().size());
		
		for(int i=0; i<700; i++) {
			world.nextTurn();
			egg.onTurn(world, new WorldStateChangedListeners());
		}
		
		assertEquals(0, egg.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals(5, world.getWorldObjects().size());
		assertEquals("chicken", world.getWorldObjects().get(4).getProperty(Constants.NAME));
	}
	
	private WorldObject createEgg(World world, WorldObject organization) {
		int eggId = new CreatureGenerator(organization).generateEgg(0, 0, world);
		return world.findWorldObjectById(eggId);
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
	}

}