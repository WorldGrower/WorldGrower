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
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.generator.BuildingGenerator;

public class UTestWaterPropertyUtils {

	@Test
	public void testEveryoneInVicinityKnowsOfPoisoning() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		int wellId = BuildingGenerator.buildWell(5, 5, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		WaterPropertyUtils.everyoneInVicinityKnowsOfPoisoning(performer, well, world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(wellId, Constants.POISON_DAMAGE));
		assertEquals(true, target.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(wellId, Constants.POISON_DAMAGE));
	}
}
