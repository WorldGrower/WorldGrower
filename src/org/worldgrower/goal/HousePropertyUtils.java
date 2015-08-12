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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class HousePropertyUtils {

	public static List<WorldObject> getHousingOfOwners(List<WorldObject> owners, World world) {
		List<WorldObject> result = new ArrayList<>();
		for(WorldObject owner : owners) {
			if (owner.hasProperty(Constants.HOUSES) && owner.getProperty(Constants.HOUSES) != null) {
				List<Integer> houseIds = owner.getProperty(Constants.HOUSES).getIds();
				for(int houseId : houseIds) {
					result.add(world.findWorldObject(Constants.ID, houseId));
				}
			}
		}
		
		return result;
	}

	public static WorldObject getBestHouse(WorldObject performer, World world) {
		int bestId = -1;
		int bestValue = Integer.MIN_VALUE;
		List<Integer> houseIds = performer.getProperty(Constants.HOUSES).getIds();
		for(int houseId : houseIds) {
			WorldObject house = world.findWorldObject(Constants.ID, houseId);
			int sleepComfort = house.getProperty(Constants.SLEEP_COMFORT);
			if (sleepComfort > bestValue) {
				bestId = houseId;
				bestValue = sleepComfort;
			}
		}
		
		if (bestId != -1) {
			return world.findWorldObject(Constants.ID, bestId);
		} else {
			return null;
		}
	}
	
}
