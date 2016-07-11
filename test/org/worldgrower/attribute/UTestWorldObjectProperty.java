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
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;

public class UTestWorldObjectProperty {

	private final WorldObjectProperty property = Constants.FACADE;
	
	@Test
	public void testRemove() {
		WorldObject facade = TestUtils.createIntelligentWorldObject(2, "facade");
		WorldObject person = TestUtils.createIntelligentWorldObject(3, Constants.FACADE, facade);
		
		property.remove(person, property, 2);
		assertEquals(null, person.getProperty(Constants.FACADE));
	}
}
