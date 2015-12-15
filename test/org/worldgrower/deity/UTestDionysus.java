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

public class UTestDionysus {

	private Dionysus deity = Deity.DIONYSUS;
	
	@Test
	public void testWorship() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(0, performer.getProperty(Constants.ALCHEMY_SKILL).getLevel(performer));
		deity.worship(performer, target, 4, world);
		assertEquals(0, performer.getProperty(Constants.ALCHEMY_SKILL).getLevel(performer));
		deity.worship(performer, target, 5, world);
		
		assertEquals(2, performer.getProperty(Constants.ALCHEMY_SKILL).getLevel(performer));
	}
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		for(int i=0; i<1000; i++) { world.nextTurn(); }
		for(int i=0; i<10; i++) { world.addWorldObject(TestUtils.createIntelligentWorldObject(i+10, Constants.DEITY, Deity.ARES)); }
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		performer.setProperty(Constants.DEITY, Deity.DIONYSUS);
		world.addWorldObject(performer);
		
		deity.onTurn(world, new WorldStateChangedListeners());
		
		assertEquals(CreatureType.UNDEAD_CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
	}
}
