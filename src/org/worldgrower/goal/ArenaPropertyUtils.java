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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class ArenaPropertyUtils {

	public static boolean worldObjectOwnsArena(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.ARENA_IDS) && worldObject.getProperty(Constants.ARENA_IDS).size() > 0;
	}
	
	public static boolean peopleAreScheduledToFightInArena(WorldObject arenaOwner, World world) {
		List<WorldObject> scheduledArenaFighters = arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).mapToWorldObjects(world, w -> w.getProperty(Constants.ARENA_OPPONENT_ID) != null);
		return scheduledArenaFighters.size() > 0;
	}

	public static boolean personIsScheduledToFightInArena(WorldObject worldObject, WorldObject arenaOwner) {
		return personIsArenaFighter(worldObject, arenaOwner) && (worldObject.getProperty(Constants.ARENA_OPPONENT_ID) != null);
	}

	public static boolean personIsArenaFighter(WorldObject worldObject, WorldObject arenaOwner) {
		return arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).contains(worldObject);
	}
	
	public static boolean personIsScheduledToFight(WorldObject worldObject) {
		return worldObject.getProperty(Constants.ARENA_OPPONENT_ID) != null;
	}
	
	public static boolean peopleAreFightingEachOther(WorldObject worldObject, WorldObject worldObject2) {
		Integer opponentId1 = worldObject.getProperty(Constants.ARENA_OPPONENT_ID);
		Integer opponentId2 = worldObject2.getProperty(Constants.ARENA_OPPONENT_ID);
		
		if (opponentId1 != null && opponentId2 != null) {
			int id1 = worldObject.getProperty(Constants.ID);
			int id2 = worldObject2.getProperty(Constants.ID);
			
			return (opponentId1.intValue() == id2) && (opponentId2.intValue() == id1);
		} else {
			return false;
		}
	}
	
	public static List<WorldObject> getPeopleScheduledForArenaFight(WorldObject arenaOwner, World world) {
		return arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).mapToWorldObjects(world, w -> personIsScheduledToFightInArena(w, arenaOwner));
	}
	
	public static void startArenaFight(WorldObject performer, WorldObject arenaOwner, World world, WorldObject opponent) {
		performer.setProperty(Constants.ARENA_OPPONENT_ID, opponent.getProperty(Constants.ID));
		opponent.setProperty(Constants.ARENA_OPPONENT_ID, performer.getProperty(Constants.ID));
		
		WorldObject topLeftArena = world.findWorldObject(Constants.ID, arenaOwner.getProperty(Constants.ARENA_IDS).getIds().get(0));
		int arenaX = topLeftArena.getProperty(Constants.X);
		int arenaY = topLeftArena.getProperty(Constants.Y);
		
		performer.setProperty(Constants.X, arenaX+1);
		performer.setProperty(Constants.Y, arenaY+1);
		
		opponent.setProperty(Constants.X, arenaX+10);
		opponent.setProperty(Constants.Y, arenaY+7);
	}

	public static boolean personCanCollectPayCheck(WorldObject performer) {
		Integer arenaPayCheckGold = performer.getProperty(Constants.ARENA_PAY_CHECK_GOLD);
		return ((arenaPayCheckGold != null) && (arenaPayCheckGold.intValue() > 0));
	}
	
	public static void addPayCheck(WorldObject worldObject) {
		if (!worldObject.hasProperty(Constants.ARENA_PAY_CHECK_GOLD)) {
			worldObject.setProperty(Constants.ARENA_PAY_CHECK_GOLD, 0);
		}
		
		worldObject.increment(Constants.ARENA_PAY_CHECK_GOLD, 5);
	}
	
	public static int getTurnsSinceLastDonation(WorldObject worldObject, World world) {
		int lastDonatedTurn = worldObject.getProperty(Constants.ARENA_DONATED_TURN).getValue(worldObject);
		int currentTurn = world.getCurrentTurn().getValue();
		
		return currentTurn - lastDonatedTurn;
	}
}
