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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestWorldObjectFacade {

	@Test
	public void testEquals() {
		WorldObject person1 = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500));
		WorldObject person2 = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500));
		
		assertEquals(false, person1.equals(person2));
		assertEquals(true, person1.equals(person1));
		assertEquals(false, person1.equals(new Object()));
	}
}
