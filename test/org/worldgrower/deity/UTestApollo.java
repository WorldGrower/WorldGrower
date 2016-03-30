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
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.profession.Professions;

public class UTestApollo {

	private Apollo deity = Deity.APOLLO;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.RESTORATION_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.RESTORATION_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.RESTORATION_SKILL).getLevel(performer));
	}
	
	@Test
	public void testGetReasonIndex() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(-1, Deity.APOLLO.getReasonIndex(performer, world));
		
		performer.getProperty(Constants.ARCHERY_SKILL).use(100, performer, Constants.ARCHERY_SKILL, new WorldStateChangedListeners());
		assertEquals(0, Deity.APOLLO.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.ARCHERY_SKILL, new Skill(10));
		performer.setProperty(Constants.PROFESSION, Professions.LUMBERJACK_PROFESSION);
		assertEquals(1, Deity.APOLLO.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(2, Deity.APOLLO.getReasonIndex(performer, world));
	}
}
