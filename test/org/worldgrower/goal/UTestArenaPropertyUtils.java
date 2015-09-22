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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldOnTurn;
import org.worldgrower.attribute.IdList;
import org.worldgrower.generator.BuildingGenerator;

public class UTestArenaPropertyUtils {

	@Test
	public void testWorldObjectOwnsArena() {
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		assertEquals(false, ArenaPropertyUtils.worldObjectOwnsArena(performer));
		
		performer.setProperty(Constants.ARENA_IDS, new IdList().add(6));
		assertEquals(true, ArenaPropertyUtils.worldObjectOwnsArena(performer));
	}
	
	@Test
	public void testPersonCanCollectPayCheck() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		assertEquals(false, ArenaPropertyUtils.personCanCollectPayCheck(performer));
		
		performer.setProperty(Constants.ARENA_PAY_CHECK_GOLD, 5);
		assertEquals(true, ArenaPropertyUtils.personCanCollectPayCheck(performer));
	}
	
	@Test
	public void testAddPayCheck() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		ArenaPropertyUtils.addPayCheck(performer);
		ArenaPropertyUtils.addPayCheck(performer);
		
		assertEquals(10, performer.getProperty(Constants.ARENA_PAY_CHECK_GOLD).intValue());
	}
	
	@Test
	public void testGetTurnsSinceLastDonation() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		assertEquals(0, ArenaPropertyUtils.getTurnsSinceLastDonation(performer, world));
		
		world.nextTurn();
		performer.setProperty(Constants.ARENA_DONATED_TURN, 1);
		world.nextTurn();
		world.nextTurn();
		
		assertEquals(2, ArenaPropertyUtils.getTurnsSinceLastDonation(performer, world));
	}
	
	@Test
	public void testPersonIsArenaFighter() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		WorldObject arenaOwner = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		
		assertEquals(false, ArenaPropertyUtils.personIsArenaFighter(performer, arenaOwner));
		
		arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).add(0);
		assertEquals(true, ArenaPropertyUtils.personIsArenaFighter(performer, arenaOwner));
	}
	
	@Test
	public void testPersonIsScheduledToFightInArena() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		WorldObject arenaOwner = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		
		assertEquals(false, ArenaPropertyUtils.personIsScheduledToFightInArena(performer, arenaOwner));
		
		arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).add(0);
		performer.setProperty(Constants.ARENA_OPPONENT_ID, 3);
		assertEquals(true, ArenaPropertyUtils.personIsScheduledToFightInArena(performer, arenaOwner));
	}
	
	@Test
	public void testStartArenaFight() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		WorldObject opponent = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject arenaOwner = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_IDS, new IdList());
		
		arenaOwner.getProperty(Constants.ARENA_IDS).addAll(BuildingGenerator.generateArena(1, 1, world, 1f));
		
		ArenaPropertyUtils.startArenaFight(performer, arenaOwner, world, opponent);
		assertEquals(1, performer.getProperty(Constants.ARENA_OPPONENT_ID).intValue());
		assertEquals(0, opponent.getProperty(Constants.ARENA_OPPONENT_ID).intValue());
	}
}
