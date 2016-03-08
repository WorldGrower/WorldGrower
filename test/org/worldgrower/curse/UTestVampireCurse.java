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
package org.worldgrower.curse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class UTestVampireCurse {

	private VampireCurse curse = Curse.VAMPIRE_CURSE;
	
	@Test
	public void testGetCurseGoals() {
		List<Goal> normalGoals = Arrays.asList(Goals.FOOD_GOAL);
		List<Goal> curseGoals = curse.getCurseGoals(normalGoals);
		
		assertEquals(3, curseGoals.size());
		assertEquals(Goals.VAMPIRE_BLOOD_LEVEL_GOAL, curseGoals.get(0));
		assertEquals(Goals.LEGALIZE_VAMPIRISM_GOAL, curseGoals.get(1));
		assertEquals(Goals.FOOD_GOAL, curseGoals.get(2));
	}
}
