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
import org.worldgrower.goal.Goals;
import org.worldgrower.profession.Professions;

public class UTestDefaultGoalChangedListener {

	private final DefaultGoalChangedListener listener = new DefaultGoalChangedListener();
	
	@Test
	public void testGoalChangedNull() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);

		listener.goalChanged(performer, Goals.FOOD_GOAL, Goals.DRINK_WATER_GOAL);
		assertEquals(null, performer.getProperty(Constants.FACADE));
	}
}
