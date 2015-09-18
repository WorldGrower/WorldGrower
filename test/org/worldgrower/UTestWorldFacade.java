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

import org.junit.Test;
import org.worldgrower.attribute.Skill;

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
}
