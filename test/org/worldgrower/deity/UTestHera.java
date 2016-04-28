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
package org.worldgrower.deity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class UTestHera {

	private Hera deity = Deity.HERA;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.INSIGHT_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.INSIGHT_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.INSIGHT_SKILL).getLevel(performer));
	}
	
	@Test
	public void testGetReasonIndex() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.CHILDREN, new IdList());
		
		assertEquals(-1, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(0, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.THIEF_PROFESSION);
		performer.setProperty(Constants.CHILDREN, new IdList().add(9));
		performer.setProperty(Constants.MATE_ID, null);
		assertEquals(1, deity.getReasonIndex(performer, world));
	}
	
	@Test
	public void testGetOrganizationGoalIndex() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.PERSONALITY, new Personality());
		
		assertEquals(-1, deity.getOrganizationGoalIndex(performer, world));
		
		performer.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.POWER_HUNGRY, 1000, "");
		assertEquals(1, deity.getOrganizationGoalIndex(performer, world));
	}
	
}
