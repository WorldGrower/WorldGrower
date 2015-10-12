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
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.Condition;

public class UTestWorldFacade {

	//TODO: what if illusion creator dies?
	@Test
	public void testIllusionIsBelievedBy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		assertEquals(true, WorldFacade.illusionIsBelievedBy(personViewingWorld, worldObject, world));
	}	
	
	@Test
	public void testIllusionIsBelievedByNotBelieved() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(0));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		assertEquals(false, WorldFacade.illusionIsBelievedBy(personViewingWorld, worldObject, world));
	}
	
	@Test
	public void testFindWorldObject() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(personViewingWorld, worldFacade.findWorldObject(Constants.ID, 1));
		assertEquals(worldObject, worldFacade.findWorldObject(Constants.ID, 2));
		
		illusionCreator.setProperty(Constants.ILLUSION_SKILL, new Skill(0));
		assertEquals(null, worldFacade.findWorldObject(Constants.ID, 2));
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
		
		worldObject.getProperty(Constants.CONDITIONS).addCondition(Condition.INVISIBLE_CONDITION, 8, world);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(null, worldFacade.findWorldObject(Constants.ID, 2));
	}
	
	@Test
	public void testFindWorldObjectsByProperty() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject personViewingWorld = TestUtils.createIntelligentWorldObject(1, Constants.INSIGHT_SKILL, new Skill(10));
		WorldObject worldObject = TestUtils.createIntelligentWorldObject(2, Constants.ILLUSION_CREATOR_ID, 3);
		WorldObject illusionCreator = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_SKILL, new Skill(20));
		
		world.addWorldObject(personViewingWorld);
		world.addWorldObject(worldObject);
		world.addWorldObject(illusionCreator);
		
		WorldFacade worldFacade = new WorldFacade(personViewingWorld, world);
		assertEquals(Arrays.asList(personViewingWorld), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 1));
		assertEquals(Arrays.asList(worldObject), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 2));
		
		illusionCreator.setProperty(Constants.ILLUSION_SKILL, new Skill(0));
		assertEquals(Arrays.asList(), worldFacade.findWorldObjectsByProperty(Constants.ID, w -> w.getProperty(Constants.ID).intValue() == 2));
	}
}
