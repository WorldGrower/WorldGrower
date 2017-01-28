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
package org.worldgrower.goal;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;

public class UTestGenderPropertyUtils {

	@Test
	public void testChangeGender() {
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1);
		performer.setProperty(Constants.GENDER, "male");
		
		GenderPropertyUtils.changeGender(performer, new WorldStateChangedListeners());		
		assertTrue(GenderPropertyUtils.isFemale(performer));

		GenderPropertyUtils.changeGender(performer, new WorldStateChangedListeners());		
		assertTrue(GenderPropertyUtils.isMale(performer));
	}
}
