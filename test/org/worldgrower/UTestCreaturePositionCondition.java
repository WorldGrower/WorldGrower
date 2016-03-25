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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.worldgrower.TestUtils.createWorldObject;

import org.junit.Test;

public class UTestCreaturePositionCondition {

	private CreaturePositionCondition creaturePositionCondition = new CreaturePositionCondition(4, 4);
	
	@Test
	public void testIsWorldObjectValidBasic() {
		WorldObject w1 = createWorldObject(4, 4, 1, 1);
		assertTrue(creaturePositionCondition.isWorldObjectValid(w1));
	}
	
	@Test
	public void testIsWorldObjectValid1x1() {
		WorldObject w1 = createWorldObject(4, 5, 1, 1);
		assertFalse(creaturePositionCondition.isWorldObjectValid(w1));
	}
	
	@Test
	public void testIsWorldObjectValid2x2False() {
		WorldObject w1 = createWorldObject(4, 5, 2, 2);
		assertFalse(creaturePositionCondition.isWorldObjectValid(w1));
	}
	
	@Test
	public void testIsWorldObjectValid2x2True() {
		WorldObject w1 = createWorldObject(4, 4, 2, 2);
		assertTrue(creaturePositionCondition.isWorldObjectValid(w1));
	}
	
	@Test
	public void testIsWorldObjectValid2x2TrueOverlap() {
		WorldObject w1 = createWorldObject(3, 3, 2, 2);
		assertTrue(creaturePositionCondition.isWorldObjectValid(w1));
	}
}
