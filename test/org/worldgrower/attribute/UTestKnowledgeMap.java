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
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.deity.Deity;

public class UTestKnowledgeMap {

	@Test
	public void testAddGet() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		
		assertEquals(true, knowledgeMap.hasKnowledge());
		assertEquals(true, knowledgeMap.hasProperty(1, Constants.FOOD));
	}
	
	@Test
	public void testRemove() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		knowledgeMap.remove(1);
		
		assertEquals(false, knowledgeMap.hasKnowledge());
		assertEquals(false, knowledgeMap.hasProperty(1, Constants.FOOD));
	}
	
	@Test
	public void testSubtract() {
		KnowledgeMap knowledgeMap1 = new KnowledgeMap();
		knowledgeMap1.addKnowledge(1, Constants.FOOD, 500);
		knowledgeMap1.addKnowledge(1, Constants.WATER, 500);
		knowledgeMap1.addKnowledge(2, Constants.FOOD, 500);
		
		KnowledgeMap knowledgeMap2 = new KnowledgeMap();
		knowledgeMap2.addKnowledge(1, Constants.FOOD, 500);
		
		KnowledgeMap subtractedKnowledgeMap = knowledgeMap1.subtract(knowledgeMap2);
		assertEquals(true, subtractedKnowledgeMap.hasKnowledge());
		assertEquals(false, subtractedKnowledgeMap.hasProperty(1, Constants.FOOD));
		assertEquals(true, subtractedKnowledgeMap.hasProperty(1, Constants.WATER));
		assertEquals(true, subtractedKnowledgeMap.hasProperty(2, Constants.FOOD));
	}
	
	@Test
	public void testFindWorldObjects() {
		World world = new WorldImpl(0, 0, null, null);
		world.addWorldObject(TestUtils.createWorldObject(1, "Test"));
		
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.DEITY, Deity.APHRODITE);
		
		List<WorldObject> worldObjects = knowledgeMap.findWorldObjects(Constants.DEITY, Deity.APHRODITE, world);
		assertEquals(1, worldObjects.size());
		assertEquals(1, worldObjects.get(0).getProperty(Constants.ID).intValue());
	}
}
