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
package org.worldgrower.creaturetype;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;

public class UTestCreatureTypeUtils {

	@Test
	public void testIsUndead() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		assertEquals(false, CreatureTypeUtils.isUndead(performer));
		
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		assertEquals(true, CreatureTypeUtils.isUndead(performer));
	}
	
	@Test
	public void testIsHumanoid() {
		assertEquals(true, CreatureTypeUtils.isHumanoid(TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList())));
		assertEquals(false, CreatureTypeUtils.isHumanoid(TestUtils.createIntelligentWorldObject(2, Constants.CREATURE_TYPE, CreatureType.COW_CREATURE_TYPE)));
		assertEquals(false, CreatureTypeUtils.isHumanoid(TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.ID, 2)));
	}
}