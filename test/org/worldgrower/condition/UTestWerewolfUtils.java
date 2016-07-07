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
package org.worldgrower.condition;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestWerewolfUtils {

	@Test
	public void testMakePersonIntoWerewolf() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		createVillagersOrganization(world);
		WerewolfUtils.makePersonIntoWerewolf(performer, world);
		
		assertEquals(CreatureType.WEREWOLF_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
	
	@Test
	public void testGetWerewolfCount() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		createVillagersOrganization(world);
		world.addWorldObject(performer);
		
		assertEquals(0, WerewolfUtils.getWerewolfCount(world));
		WerewolfUtils.makePersonIntoWerewolf(performer, world);

		assertEquals(1, WerewolfUtils.getWerewolfCount(world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
