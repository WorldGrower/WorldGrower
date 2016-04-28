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

public class UTestVampireUtils {

	@Test
	public void testVampirizePerson() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		
		assertEquals(CreatureType.VAMPIRE_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
	
	@Test
	public void testVampireCount() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		world.addWorldObject(performer);
		
		assertEquals(0, VampireUtils.getVampireCount(world));
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());

		assertEquals(1, VampireUtils.getVampireCount(world));
	}
}
