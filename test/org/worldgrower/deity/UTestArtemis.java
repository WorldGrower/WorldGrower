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
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestArtemis {

	private Artemis deity = Deity.ARTEMIS;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.ARCHERY_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.ARCHERY_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.ARCHERY_SKILL).getLevel(performer));
	}
	
	@Test
	public void testGetReasonIndex() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(-1, deity.getReasonIndex(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(0, deity.getReasonIndex(performer, world));
	}
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.DEITY, Deity.APHRODITE);
		performer.setProperty(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(performer);
		
		createVillagersOrganization(world);
		
		for(int i=0; i<20; i++) {
			WorldObject worshipper = TestUtils.createSkilledWorldObject(i + 10);
			worshipper.setProperty(Constants.DEITY, Deity.HADES);
			world.addWorldObject(worshipper);
		}
		
		for(int i=0; i<5000; i++) {
			world.nextTurn();
			deity.onTurn(world, new WorldStateChangedListeners());
		}
		
		assertEquals(CreatureType.WEREWOLF_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
	
	@Test
	public void testOnTurnNoWerewolfTarget() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.DEITY, null);
		performer.setProperty(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(performer);
		
		createVillagersOrganization(world);
		
		for(int i=0; i<20; i++) {
			WorldObject worshipper = TestUtils.createSkilledWorldObject(i + 10);
			worshipper.setProperty(Constants.DEITY, Deity.HADES);
			world.addWorldObject(worshipper);
		}
		
		for(int i=0; i<5000; i++) {
			world.nextTurn();
			deity.onTurn(world, new WorldStateChangedListeners());
		}
		
		assertEquals(CreatureType.HUMAN_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
