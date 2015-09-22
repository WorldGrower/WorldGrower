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
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;

public class UTestSocializeGoal {

	@Test
	public void testIsSocializeTargetForPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		
		assertEquals(false, SocializeGoal.isSocializeTargetForPerformer(performer, target));
		
		performer.getProperty(Constants.GROUP).add(2);
		target.getProperty(Constants.GROUP).add(2);
		assertEquals(true, SocializeGoal.isSocializeTargetForPerformer(performer, target));
	}
	
	@Test
	public void testIsFirstTimeSocializeTargetForPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.getProperty(Constants.GROUP).add(6);
		target.getProperty(Constants.GROUP).add(6);
		
		assertEquals(true, SocializeGoal.isFirstTimeSocializeTargetForPerformer(performer, target));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 5);
		assertEquals(false, SocializeGoal.isFirstTimeSocializeTargetForPerformer(performer, target));
	}
}
