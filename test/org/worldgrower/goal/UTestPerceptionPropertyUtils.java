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
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.terrain.TerrainImpl;
import org.worldgrower.terrain.TerrainMapper;

public class UTestPerceptionPropertyUtils {

	@Test
	public void testCalculateRadius() {
		MockWorld world = new MockWorld(new TerrainImpl(0, 0, new TerrainMapper()), new  WorldImpl(0, 0, null, null));
		WorldObject performer = TestUtils.createSkilledWorldObject(0);
		performer.setProperty(Constants.PERCEPTION_SKILL, new Skill(10));
		
		assertEquals(13, PerceptionPropertyUtils.calculateRadius(performer, world));
		
		world.setCurrentTurn(50);
		assertEquals(18, PerceptionPropertyUtils.calculateRadius(performer, world));
		
		performer.setProperty(Constants.PERCEPTION_SKILL, new Skill(20));
		assertEquals(20, PerceptionPropertyUtils.calculateRadius(performer, world));
	}
	
	@Test
	public void testCalculateRadiusWithDarkVision() {
		MockWorld world = new MockWorld(new TerrainImpl(0, 0, new TerrainMapper()), new  WorldImpl(0, 0, null, null));
		WorldObject performer = TestUtils.createSkilledWorldObject(0);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.PERCEPTION_SKILL, new Skill(10));
		Conditions.add(performer, Condition.DARK_VISION_CONDITION, 8, world);
		
		assertEquals(18, PerceptionPropertyUtils.calculateRadius(performer, world));
	}
}
