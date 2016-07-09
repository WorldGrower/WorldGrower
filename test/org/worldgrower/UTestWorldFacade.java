/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.IllusionPropertyUtils;

public class UTestWorldFacade {

	@Test
	public void testFindWorldObject() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		worldObject.setProperty(Constants.X, 5);
		worldObject.setProperty(Constants.Y, 5);
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(personViewingWorld, worldFacade.findWorldObjectById(1));
		assertEquals(worldObject, worldFacade.findWorldObjectById(2));
		
		personViewingWorld.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(worldObject, Constants.ILLUSION_CREATOR_ID, 3);
		assertEquals(null, worldFacade.findWorldObjectById(2));
	}
	
	@Test
	public void testFindWorldObjectInvisible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, "Test");
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		Conditions.add(worldObject, Condition.INVISIBLE_CONDITION, 8, world);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(null, worldFacade.findWorldObjectById(2));
	}
	
	@Test
	public void testFindWorldObjectsByProperty() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		worldObject.setProperty(Constants.X, 5);
		worldObject.setProperty(Constants.Y, 5);
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		personViewingWorld.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(Arrays.asList(personViewingWorld), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 1));
		assertEquals(Arrays.asList(worldObject), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 2));
		
		personViewingWorld.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(worldObject, Constants.ILLUSION_CREATOR_ID, 3);
		assertEquals(Arrays.asList(), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 2));
	}
	
	@Test
	public void testExists() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		worldObject.setProperty(Constants.X, 5);
		worldObject.setProperty(Constants.Y, 5);
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(false, worldFacade.exists(7));
		assertEquals(true, worldFacade.exists(1));
		assertEquals(true, worldFacade.exists(2));
		assertEquals(true, worldFacade.exists(3));
		
		personViewingWorld.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(worldObject, Constants.ILLUSION_CREATOR_ID, 3);
		assertEquals(true, worldFacade.exists(1));
		assertEquals(false, worldFacade.exists(2));
		assertEquals(true, worldFacade.exists(3));
	}
	
	@Test
	public void testGetWorldObjects() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		personViewingWorld.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(worldObject, Constants.ILLUSION_CREATOR_ID, 3);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(2, worldFacade.getWorldObjects().size());
		assertEquals(personViewingWorld, worldFacade.getWorldObjects().get(0));
		assertEquals(illusionCreator, worldFacade.getWorldObjects().get(1));
	}
	
	@Test
	public void testGetCurrentTurn() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(0, worldFacade.getCurrentTurn().getValue());
	}
	
	@Test
	public void testWorldFacadeIsMaskedByIllusion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		world.addWorldObject(personViewingWorld);
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		
		assertEquals(personViewingWorld, worldFacade.findWorldObjectById(personViewingWorld.getProperty(Constants.ID)));
	}
	
	@Test
	public void testIsMaskedByIllusionSelf() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		world.addWorldObject(personViewingWorld);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		
		assertEquals(false, worldFacade.isMaskedByIllusion(personViewingWorld, world));
	}
	
	@Test
	public void testIsMaskedByIllusionMaskingIllusion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		world.addWorldObject(personViewingWorld);
		
		int nightShadeId = PlantGenerator.generateNightShade(1, 1, world);
		WorldObject nightShade = world.findWorldObjectById(nightShadeId);
		IllusionPropertyUtils.createIllusion(personViewingWorld, personViewingWorld.getProperty(Constants.ID), world, 1, 1, 1, 1);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(false, worldFacade.isMaskedByIllusion(nightShade, world));
		
		personViewingWorld.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		assertEquals(true, worldFacade.isMaskedByIllusion(nightShade, world));
	}
	
	@Test
	public void testIsMaskedByIllusionOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(3, "person");
		world.addWorldObject(personViewingWorld);
		
		WorldObject organization = GroupPropertyUtils.create(null, "Test", world);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		
		assertEquals(false, worldFacade.isMaskedByIllusion(organization, world));
	}

	@Test
	public void testGetWorldObjectMaskedByIllusion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		world.addWorldObject(personViewingWorld);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		
		assertEquals(null, worldFacade.getWorldObjectMaskedByIllusion(personViewingWorld, world));
	}
	
	@Test
	public void testGetWorldObjectMaskedByIllusionMaskingIllusion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, "person");
		world.addWorldObject(personViewingWorld);
		
		int nightShadeId = PlantGenerator.generateNightShade(1, 1, world);
		WorldObject nightShade = world.findWorldObjectById(nightShadeId);
		int illusionId = IllusionPropertyUtils.createIllusion(personViewingWorld, personViewingWorld.getProperty(Constants.ID), world, 1, 1, 1, 1);
		WorldObject illusion = world.findWorldObjectById(illusionId);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(null, worldFacade.getWorldObjectMaskedByIllusion(illusion, world));
		
		personViewingWorld.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		assertEquals(nightShade, worldFacade.getWorldObjectMaskedByIllusion(illusion, world));
	}
}
