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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdRelationshipMap;

public class UTestBecomeOrganizationMemberGoal {
	
	@Test
	public void testGetMostLikedLeaderId() {
		IdRelationshipMap idRelationshipMap = new IdRelationshipMap();
		idRelationshipMap.incrementValue(3, 100);
		idRelationshipMap.incrementValue(5, 200);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, idRelationshipMap);
		List<WorldObject> organizations = new ArrayList<>();

		organizations.add(TestUtils.createIntelligentWorldObject(2, Constants.ORGANIZATION_LEADER_ID, 3));
		assertEquals(3, Goals.BECOME_ORGANIZATION_MEMBER_GOAL.getMostLikedLeaderId(performer, organizations).intValue());
		
		organizations.add(TestUtils.createIntelligentWorldObject(4, Constants.ORGANIZATION_LEADER_ID, 5));
		assertEquals(5, Goals.BECOME_ORGANIZATION_MEMBER_GOAL.getMostLikedLeaderId(performer, organizations).intValue());
	}
	
	@Test
	public void testGetMostLikedLeaderIdForNoLeader() {
		IdRelationshipMap idRelationshipMap = new IdRelationshipMap();
		idRelationshipMap.incrementValue(3, -100);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, idRelationshipMap);
		List<WorldObject> organizations = new ArrayList<>();

		organizations.add(TestUtils.createIntelligentWorldObject(2, Constants.ORGANIZATION_LEADER_ID, 3));
		organizations.add(TestUtils.createIntelligentWorldObject(4, Constants.ORGANIZATION_LEADER_ID, null));
		assertEquals(null, Goals.BECOME_ORGANIZATION_MEMBER_GOAL.getMostLikedLeaderId(performer, organizations));
	}
}
