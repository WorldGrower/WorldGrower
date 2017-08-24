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
import static org.junit.Assert.fail;
import static org.worldgrower.TestUtils.createWorldObject;

import org.junit.Test;

public class UTestReach {

	@Test
	public void testEvaluateTargetNextToTarget() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);

		assertEquals(0, Reach.evaluateTarget(performer, createWorldObject(3, 3, 1, 1), 1));
		assertEquals(0, Reach.evaluateTarget(performer, createWorldObject(1, 1, 1, 1), 1));
		assertEquals(0, Reach.evaluateTarget(performer, createWorldObject(1, 3, 1, 1), 1));
		assertEquals(0, Reach.evaluateTarget(performer, createWorldObject(3, 1, 1, 1), 1));
	}
	
	@Test
	public void testEvaluateTargetNextToLargeTarget() {
		WorldObject target = createWorldObject(3, 3, 2, 2);
		// squares 3,3 3,4 4,3 and 4,4 contain target. All squares around it should have distance 0.
		assertEquals(0, Reach.evaluateTarget(createWorldObject(2, 2, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(2, 3, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(2, 4, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(2, 5, 1, 1), target, 1));
		
		assertEquals(0, Reach.evaluateTarget(createWorldObject(3, 5, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(4, 5, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(5, 5, 1, 1), target, 1));
		
		assertEquals(0, Reach.evaluateTarget(createWorldObject(5, 4, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(5, 3, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(5, 2, 1, 1), target, 1));
		
		assertEquals(0, Reach.evaluateTarget(createWorldObject(4, 2, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(3, 2, 1, 1), target, 1));
		assertEquals(0, Reach.evaluateTarget(createWorldObject(2, 2, 1, 1), target, 1));
		
		/*   2,2  3,2  4,2  5,2
		 *   2,3  3,3  4,3  5,3
		 *   2,4  3,4  4,4  5,4
		 *   2,5  3,5  4,5  5,5
		 */
	}
	
	@Test
	public void testDistance() {
		WorldObject performer = createWorldObject(2, 2, 1, 1);
		assertEquals(0, Reach.distance(performer, createWorldObject(2, 2, 1, 1)));
		assertEquals(1, Reach.distance(performer, createWorldObject(2, 3, 1, 1)));
		assertEquals(1, Reach.distance(performer, createWorldObject(3, 3, 1, 1)));
		assertEquals(2, Reach.distance(performer, createWorldObject(4, 4, 1, 1)));
	}
	
	@Test
	public void testEvaluateTargetPerformerNull() {
		try {
			assertEquals(0, Reach.evaluateTarget(null, createWorldObject(3, 3, 1, 1), 1));
			fail("method should fail");
		} catch(IllegalArgumentException e) {
			assertEquals("performer is null", e.getMessage());
		}
	}
	
	@Test
	public void testEvaluateTargetTargetNull() {
		try {
			assertEquals(0, Reach.evaluateTarget(createWorldObject(3, 3, 1, 1), null, 1));
			fail("method should fail");
		} catch(IllegalArgumentException e) {
			assertEquals("target is null", e.getMessage());
		}
	}
	
	@Test
	public void testDistancePerformerNull() {
		try {
			assertEquals(0, Reach.distance(null, createWorldObject(2, 2, 1, 1)));
			fail("method should fail");
		} catch(IllegalArgumentException e) {
			assertEquals("performer is null", e.getMessage());
		}
	}
	
	@Test
	public void testDistanceTargetNull() {
		try {
			assertEquals(0, Reach.distance(createWorldObject(2, 2, 1, 1), null));
			fail("method should fail");
		} catch(IllegalArgumentException e) {
			assertEquals("target is null", e.getMessage());
		}
	}
}
