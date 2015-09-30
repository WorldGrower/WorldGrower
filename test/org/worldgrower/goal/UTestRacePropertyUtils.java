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
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;

public class UTestRacePropertyUtils {

	@Test
	public void testHasSameRace() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		WorldObject ghoul = TestUtils.createIntelligentWorldObject(1, Constants.CREATURE_TYPE, CreatureType.GHOUL_CREATURE_TYPE);

		assertEquals(true, RacePropertyUtils.hasSameRace(performer, target));
		assertEquals(false, RacePropertyUtils.hasSameRace(performer, ghoul));
	}
	
	@Test
	public void testCanHaveOffspring() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		
		performer.setProperty(Constants.GENDER, "male");
		target.setProperty(Constants.GENDER, "female");
		
		assertEquals(true, RacePropertyUtils.canHaveOffspring(performer, target));
		
		target.setProperty(Constants.GENDER, "male");
		assertEquals(false, RacePropertyUtils.canHaveOffspring(performer, target));
		
		target.setProperty(Constants.GENDER, "female");
		target.setProperty(Constants.CREATURE_TYPE, CreatureType.GHOUL_CREATURE_TYPE);
		assertEquals(false, RacePropertyUtils.canHaveOffspring(performer, target));
	}
}
