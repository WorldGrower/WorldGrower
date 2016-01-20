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
package org.worldgrower.personality;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestPersonality {

	@Test
	public void testChangeValue() {
		Personality personality = new Personality();
		
		personality.changeValue(PersonalityTrait.GREEDY, -1000, "reason");
		assertEquals(-1000, personality.getValue(PersonalityTrait.GREEDY));
	}
	
	@Test
	public void testCopy() {
		Personality personality = new Personality();
		
		personality.changeValue(PersonalityTrait.GREEDY, -1000, "reason");
		
		Personality copy = personality.copy();
		personality.changeValue(PersonalityTrait.GREEDY, 2000, "reason");
		
		assertEquals(1000, personality.getValue(PersonalityTrait.GREEDY));
		assertEquals(-1000, copy.getValue(PersonalityTrait.GREEDY));
	}
	
	@Test
	public void testGetMostExtremePersonalityTrait() {
		Personality personality = new Personality();
		for(PersonalityTrait personalityTrait : PersonalityTrait.ALL_TRAITS) {
			personality.changeValue(personalityTrait, -10000, "");
			personality.changeValue(personalityTrait, 1000, "");
		}
		personality.changeValue(PersonalityTrait.GREEDY, 10000, "");
		assertEquals(PersonalityTrait.GREEDY, personality.getMostExtremePersonalityTrait());
	}
}
