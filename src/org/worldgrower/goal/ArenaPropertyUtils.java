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
	
	public static List<WorldObject> getPeopleScheduledForArenaFight(WorldObject arenaOwner, World world) {
		return arenaOwner.getProperty(Constants.ARENA_FIGHTER_IDS).mapToWorldObjects(world, w -> personIsScheduledToFightInArena(w, arenaOwner));
	}
}
