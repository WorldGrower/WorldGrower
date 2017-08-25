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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.Item;

public class HousePropertyUtils {

	public static List<WorldObject> getHousingOfOwners(List<WorldObject> owners, World world) {
		List<WorldObject> result = new ArrayList<>();
		for(WorldObject owner : owners) {
			BuildingList buildings = owner.getProperty(Constants.BUILDINGS);
			if (buildings != null) {
				List<Integer> houseIds = buildings.getIds(BuildingType.SHACK, BuildingType.HOUSE);
				for(int houseId : houseIds) {
					result.add(world.findWorldObjectById(houseId));
				}
			}
		}
		
		return result;
	}

	public static WorldObject getBestHouse(WorldObject performer, World world) {
		int bestId = -1;
		int bestValue = Integer.MIN_VALUE;
		List<Integer> houseIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.SHACK, BuildingType.HOUSE);
		for(int houseId : houseIds) {
			WorldObject house = world.findWorldObjectById(houseId);
			int sleepComfort = house.getProperty(Constants.SLEEP_COMFORT);
			if (sleepComfort > bestValue) {
				bestId = houseId;
				bestValue = sleepComfort;
			}
		}
		
		if (bestId != -1) {
			return world.findWorldObjectById(bestId);
		} else {
			return null;
		}
	}
	
	public static boolean hasBuildings(WorldObject performer, BuildingType buildingType) {
		return performer.hasProperty(Constants.BUILDINGS) && performer.getProperty(Constants.BUILDINGS).count(buildingType) > 0;
	}

	public static boolean hasBuildingForSale(WorldObject target, BuildingType buildingType, World world) {
		WorldObject buildingForSale = getBuildingForSale(target, buildingType, world);
		return buildingForSale != null;
	}

	public static WorldObject getBuildingForSale(WorldObject target, BuildingType buildingType, World world) {
		if (target.hasProperty(Constants.BUILDINGS)) {
			for(int buildingId : target.getProperty(Constants.BUILDINGS).getIds(buildingType)) {
				WorldObject building = world.findWorldObjectById(buildingId);
				if (building.hasProperty(Constants.SELLABLE) && building.getProperty(Constants.SELLABLE)) {
					return building;
				}
			}
		}
		return null;
	}

	public static boolean allHousesButFirstSellable(WorldObject performer, World world) {
		boolean isFirstHouse = true;
		for(int houseId : performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE)) {
			WorldObject house = world.findWorldObjectById(houseId);
			
			if (!isFirstHouse) {
				if (!(house.hasProperty(Constants.SELLABLE) && house.getProperty(Constants.SELLABLE))) {
					return false;
				}
			}
			
			if (isFirstHouse) {
				isFirstHouse = false;
			}
		}
		
		return true;
	}

	public static boolean hasHouseWithBed(WorldObject performer, World world) {
		List<WorldObject> housesWithBed = performer.getProperty(Constants.BUILDINGS).mapToWorldObjects(world, BuildingType.HOUSE, w -> w.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.BED).size() > 0);
		return housesWithBed.size() > 0;
	}
	
	public static OperationInfo createBuyBuildingOperationInfo(WorldObject performer, BuildingType buildingType, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> hasBuildingForSale(w, buildingType, world), world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.getBuyBuildingConversation(buildingType)), Actions.TALK_ACTION);
		} else {
			return null;
		}
	}
	
	public static List<WorldObject> getUnmarkedBuildings(WorldObject performer, BuildingType buildingType, World world) {
		return performer.getProperty(Constants.BUILDINGS).mapToWorldObjects(world, buildingType, w -> !w.hasProperty(Constants.SELLABLE) || !w.getProperty(Constants.SELLABLE));
	}
	
	public static int getOwnedBuildingCount(BuildingType buildingType, World world) {
		int ownedBuildingCount = 0;
		for(WorldObject owner : world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.BUILDINGS))) {
			ownedBuildingCount += owner.getProperty(Constants.BUILDINGS).count(buildingType);
		}
		return ownedBuildingCount;
	}
	
	
	public static void removeShack(WorldObject performer, World world) {
		Integer currentHouseIdValue = performer.getProperty(Constants.BUILDINGS).getFirstIdOrNull(BuildingType.SHACK);
		if (currentHouseIdValue != null) {
			int currentHouseId = currentHouseIdValue.intValue();
			WorldObject shack = world.findWorldObjectById(currentHouseId);
			shack.setProperty(Constants.HIT_POINTS, 0);
		}
	}
	
	public static void transferProperty(int buildingId, WorldObject performer, WorldObject target, World world) {
		WorldObject building = world.findWorldObjectById(buildingId);
		BuildingType buildingType = building.getProperty(Constants.BUILDING_TYPE);
		
		performer.getProperty(Constants.BUILDINGS).remove(buildingId);
		
		target.getProperty(Constants.BUILDINGS).add(buildingId, buildingType);
		building.setProperty(Constants.SELLABLE, Boolean.FALSE);
	}
	
	public static WorldObject getBuildingOwner(WorldObject building, World world) {
		List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.BUILDINGS) && w.getProperty(Constants.BUILDINGS).contains(building));
		if (worldObjects.size() > 0) {
			return worldObjects.get(0);
		} else {
			return null;
		}
	}
}
