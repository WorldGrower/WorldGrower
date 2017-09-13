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
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;

public class UTestRebellionPropertyUtils {

	@Test
	public void testWantsToRebel() {
		WorldObject performer = createPerformer(2);
		WorldObject leader = createPerformer(3);
		
		assertEquals(false, RebellionPropertyUtils.wantsToRebel(performer, leader));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		assertEquals(true, RebellionPropertyUtils.wantsToRebel(performer, leader));
	}
	
	@Test
	public void testWantsToRebelNotForgiving() {
		WorldObject performer = createPerformer(2);
		WorldObject leader = createPerformer(3);
		assertEquals(false, RebellionPropertyUtils.wantsToRebel(performer, leader));
		
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.FORGIVING, -1000, "betrayal");
		performer.setProperty(Constants.PERSONALITY, personality);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -900);
		assertEquals(true, RebellionPropertyUtils.wantsToRebel(performer, leader));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -100);
		assertEquals(true, RebellionPropertyUtils.wantsToRebel(performer, leader));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.RELATIONSHIPS, new IdRelationshipMap());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}