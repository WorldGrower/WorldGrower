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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;
import org.worldgrower.history.Turn;
import org.worldgrower.profession.Professions;

public class UTestKnowledgeMap {

	@Test
	public void testAddGet() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		
		assertEquals(true, knowledgeMap.hasKnowledge());
		assertEquals(true, knowledgeMap.hasProperty(1, Constants.FOOD));
	}
	
	@Test
	public void testSwitchProfession() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject subject = TestUtils.createWorldObject(1, "Test");
		world.addWorldObject(subject);
		
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(subject, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		PropertyKnowledge expectedKnowledge = new PropertyKnowledge(1, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		assertEquals(Arrays.asList(expectedKnowledge), knowledgeMap.getKnowledge(subject));
		
		
		knowledgeMap.addKnowledge(subject, Constants.PROFESSION, Professions.FISHER_PROFESSION);
		
		expectedKnowledge = new PropertyKnowledge(1, Constants.PROFESSION, Professions.FISHER_PROFESSION);
		assertEquals(Arrays.asList(expectedKnowledge), knowledgeMap.getKnowledge(subject));
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
	
	@Test
	public void testFindWorldObjectsForInt() {
		World world = new WorldImpl(0, 0, null, null);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500));
		
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		
		List<WorldObject> worldObjects = knowledgeMap.findWorldObjects(Constants.FOOD, 500, world);
		assertEquals(1, worldObjects.size());
		assertEquals(1, worldObjects.get(0).getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testRemoveForIdContainer() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.DEITY, Deity.APHRODITE);
		knowledgeMap.addKnowledge(2, Constants.CHILD_BIRTH_ID, 1);
		
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, knowledgeMap);
		
		knowledgeMap.remove(person, Constants.KNOWLEDGE_MAP, 1);
		assertEquals(false, knowledgeMap.hasKnowledge());
	}
	
	@Test
	public void addMultipleKnowledgeWithSameProperty() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		knowledgeMap.addKnowledge(1, Constants.FOOD, 700);
		
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, knowledgeMap);
		
		assertEquals(true, knowledgeMap.hasKnowledge());
		assertEquals(1, knowledgeMap.getKnowledge(person).size());
		assertEquals(700, ((PropertyKnowledge)knowledgeMap.getKnowledge(person).get(0)).getValue());
	}
	
	@Test
	public void testHasKnowledge() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		
		assertEquals(true, knowledgeMap.hasKnowledge(1));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500);
		assertEquals(false, knowledgeMap.hasKnowledge(target));
	}
	
	@Test
	public void testAdd() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		knowledgeMap.addKnowledge(1, Constants.FOOD, 500);
		
		KnowledgeMap knowledgeMap2 = new KnowledgeMap();
		knowledgeMap2.addKnowledge(1, Constants.WATER, 500);
		
		knowledgeMap.add(knowledgeMap2);
		
		assertEquals(true, knowledgeMap.hasKnowledge(1));
		List<PropertyKnowledge> expectedKnowledge = Arrays.asList(
				new PropertyKnowledge(1, Constants.FOOD, 500),
				new PropertyKnowledge(1, Constants.WATER, 500)
				);
		assertEquals(expectedKnowledge, knowledgeMap.getKnowledge(TestUtils.createIntelligentWorldObject(1, "")));
	}
	
	@Test
	public void testAddNoExistingKnowledge() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		
		KnowledgeMap knowledgeMap2 = new KnowledgeMap();
		knowledgeMap2.addKnowledge(1, Constants.WATER, 500);
		
		knowledgeMap.add(knowledgeMap2);
		
		assertEquals(true, knowledgeMap.hasKnowledge(1));
		List<PropertyKnowledge> expectedKnowledge = Arrays.asList(
				new PropertyKnowledge(1, Constants.WATER, 500)
				);
		assertEquals(expectedKnowledge, knowledgeMap.getKnowledge(TestUtils.createIntelligentWorldObject(1, "")));
	}
	
	@Test
	public void testHasKnowledgeByIds() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500);
		Knowledge knowledge = new PropertyKnowledge(performer.getProperty(Constants.ID), Constants.FOOD, 500);
		Knowledge otherKnowledge = new PropertyKnowledge(performer.getProperty(Constants.ID), Constants.WATER, 500);
		knowledgeMap.addKnowledge(performer, knowledge);
		
		assertEquals(true, knowledgeMap.hasKnowledge(performer.getProperty(Constants.ID), knowledge));
		assertEquals(false, knowledgeMap.hasKnowledge(performer.getProperty(Constants.ID)+1, knowledge));
		assertEquals(false, knowledgeMap.hasKnowledge(performer.getProperty(Constants.ID), otherKnowledge));
	}
	
	@Test
	public void testHasAllKnowledge() {
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		KnowledgeMap otherKnowledgeMap = new KnowledgeMap();
		assertEquals(true, knowledgeMap.hasAllKnowledge(otherKnowledgeMap));
		
		Knowledge knowledge = new PropertyKnowledge(1, Constants.FOOD, 500);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500);
		knowledgeMap.addKnowledge(performer, knowledge);
		otherKnowledgeMap.addKnowledge(performer, knowledge);
		assertEquals(true, knowledgeMap.hasAllKnowledge(otherKnowledgeMap));
		
		Knowledge otherKnowledge = new PropertyKnowledge(1, Constants.WATER, 500);
		otherKnowledgeMap.addKnowledge(performer, otherKnowledge);
		assertEquals(false, knowledgeMap.hasAllKnowledge(otherKnowledgeMap));
	}
	
	@Test
	public void testHasEvent() {
		World world = new WorldImpl(0, 0, null, null);
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		WorldObject worldObject = TestUtils.createSkilledWorldObject(1, Constants.FOOD, 500);
		
		assertEquals(false, knowledgeMap.hasEvent(worldObject, t -> true, w -> true, world, Actions.MELEE_ATTACK_ACTION));
		
		knowledgeMap.addKnowledge(worldObject, world);
		world.getHistory().actionPerformed(new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		
		assertEquals(true, knowledgeMap.hasEvent(worldObject, t -> true, w -> true, world, Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testHasEventTimeCondition() {
		World world = new WorldImpl(0, 0, null, null);
		KnowledgeMap knowledgeMap = new KnowledgeMap();
		WorldObject worldObject = TestUtils.createSkilledWorldObject(1, Constants.FOOD, 500);
	
		knowledgeMap.addKnowledge(worldObject, world);
		world.getHistory().actionPerformed(new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		
		assertEquals(false, knowledgeMap.hasEvent(worldObject, t -> t > 100, w -> true, world, Actions.MELEE_ATTACK_ACTION));
	}
}
