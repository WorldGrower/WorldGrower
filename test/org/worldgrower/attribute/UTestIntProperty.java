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
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class UTestIntProperty {

	private final IntProperty intProperty = new IntProperty("testIntProperty", 0, 10, false, new ArrayList<>());
	
	@Test
	public void testCheckValue() {
		intProperty.checkValue(0);
		intProperty.checkValue(10);
	}
	
	@Test
	public void testCheckValueInvalid() {
		try {
			intProperty.checkValue(20);
			fail("method should fail");
		} catch(IllegalStateException ex) {
			assertEquals("value 20 is higher than maxValue 10 for testIntProperty", ex.getMessage());
		}
	}

	@Test
	public void testCheckValueNull() {
		try {
			intProperty.checkValue(null);
			fail("method should fail");
		} catch(IllegalStateException ex) {
			assertEquals("value null is null for testIntProperty", ex.getMessage());
		}
	}
	
	@Test
	public void testNormalize() {
		assertEquals(0, intProperty.normalize(-5));
		assertEquals(10, intProperty.normalize(20));
	}
}
