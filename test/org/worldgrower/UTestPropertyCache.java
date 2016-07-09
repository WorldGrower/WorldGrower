/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class UTestPropertyCache {

	@Test
	public void testIdAdded() {
		World world = createWorld();
		WorldObject person = TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500);
		world.addWorldObject(person);

		assertEquals(Arrays.asList(person), world.findWorldObjectsByProperty(Constants.FOOD, w -> true));
	}
	
	private WorldImpl createWorld() {
		return new WorldImpl(1, 1, null, null);
	}
}
