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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestThieveryPropertyUtils {

	@Test
	public void testGetThieverySuccessPercentage() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		WorldObject target = TestUtils.createSkilledWorldObject(3);
		
		assertEquals(91, ThieveryPropertyUtils.getThieverySuccessPercentage(performer, target, 1, 0));
		assertEquals(46, ThieveryPropertyUtils.getThieverySuccessPercentage(performer, target, 10, 0));
		assertEquals(29, ThieveryPropertyUtils.getThieverySuccessPercentage(performer, target, 100, 0));
		assertEquals(74, ThieveryPropertyUtils.getThieverySuccessPercentage(performer, target, 1, 1));
	
		Conditions.add(performer, Condition.INVISIBLE_CONDITION, 8, world);
		assertEquals(96, ThieveryPropertyUtils.getThieverySuccessPercentage(performer, target, 1, 0));
	}
	
	@Test
	public void testGetOpenLockSuccessPercentage() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		WorldObject target = TestUtils.createSkilledWorldObject(3);
		target.setProperty(Constants.LOCK_STRENGTH, 5);
		
		assertEquals(34, ThieveryPropertyUtils.getOpenLockSuccessPercentage(performer, target));
	
		Conditions.add(performer, Condition.INVISIBLE_CONDITION, 8, world);
		assertEquals(41, ThieveryPropertyUtils.getOpenLockSuccessPercentage(performer, target));
	}
}
