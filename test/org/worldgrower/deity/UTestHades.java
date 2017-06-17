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
package org.worldgrower.deity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestHades {

	private Hades deity = Deity.HADES;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.EVOCATION_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.EVOCATION_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.EVOCATION_SKILL).getLevel(performer));
	}
	
	@Test
	public void testGetReasonIndex() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(-1, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(2, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.GRAVE_DIGGER_PROFESSION);
		assertEquals(3, deity.getReasonIndex(performer, world));
		
		WorldObject vampire = TestUtils.createSkilledWorldObject(3);
		VampireUtils.vampirizePerson(vampire, new WorldStateChangedListeners());
		world.addWorldObject(vampire);
		
		assertEquals(1, deity.getReasonIndex(performer, world));
	}
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		int stoneResourceId = TerrainGenerator.generateStoneResource(0, 0, world);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		for(int i=0; i<20; i++) { world.addWorldObject(TestUtils.createIntelligentWorldObject(i+10, Constants.DEITY, Deity.ARES)); }
		
		WorldObject stoneResource = world.findWorldObjectById(stoneResourceId);
		
		for(int i=0; i<7800; i++) {
			villagersOrganization.getProperty(Constants.DEITY_ATTRIBUTES).onTurn(world);
			world.nextTurn();
		}
		
		for(int i=0; i<400; i++) {
			deity.onTurn(world, new WorldStateChangedListeners());
			world.nextTurn(); 
		}
		assertEquals(0, stoneResource.getProperty(Constants.HIT_POINTS).intValue());
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
