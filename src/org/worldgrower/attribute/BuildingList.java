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
package org.worldgrower.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class BuildingList implements Serializable, IdContainer {
	private List<Building> buildings = new ArrayList<>();
	
	public BuildingList add(int id, BuildingType buildingType) {
		buildings.add(new Building(id, buildingType));
		return this;
	}
	
	public BuildingList add(WorldObject worldObject, BuildingType buildingType) {
		add(worldObject.getProperty(Constants.ID), buildingType);
		return this;
	}
	
	public void remove(int id) {
		Iterator<Building> buildingsIterator = buildings.iterator();
		while(buildingsIterator.hasNext()) {
			Building building = buildingsIterator.next();
			if (building.getId() == id) {
				buildingsIterator.remove();
			}
		}
	}
	
	public void remove(WorldObject worldObject) {
		remove(worldObject.getProperty(Constants.ID));
	}
	
	public List<Integer> getIds(BuildingType buildingType) {
		List<Integer> idList = new ArrayList<>();
		for(Building building : buildings) {
			if (building.getBuildingType() == buildingType) {
				idList.add(building.getId());
			}
		}
		
		return idList;
	}
	
	public List<Integer> getIds(BuildingType buildingType1, BuildingType buildingType2) {
		List<Integer> idList = new ArrayList<>();
		for(Building building : buildings) {
			if (building.getBuildingType() == buildingType1 || building.getBuildingType() == buildingType2) {
				idList.add(building.getId());
			}
		}
		
		return idList;
	}
	
	private static class Building implements Serializable {
		private final int id;
		private final BuildingType buildingType;
		
		public Building(int id, BuildingType buildingType) {
			this.id = id;
			this.buildingType = buildingType;
		}

		public int getId() {
			return id;
		}

		public BuildingType getBuildingType() {
			return buildingType;
		}
	}

	@Override
	public void remove(WorldObject worldObject, ManagedProperty<?> property, int id) {
		BuildingsListProperty buildingProperty = (BuildingsListProperty) property;
		worldObject.getProperty(buildingProperty).remove(id);
		
	}

	public BuildingList copy() {
		BuildingList buildingList = new BuildingList();
		buildingList.buildings.addAll(buildings);
		return buildingList;
	}

	public boolean contains(WorldObject target) {
		return contains(target.getProperty(Constants.ID));
	}
	
	public List<WorldObject> mapToWorldObjects(World world, BuildingType buildingType, Function<WorldObject, Boolean> testFunction) {
		List<WorldObject> worldObjects = new ArrayList<>();
		for(Building building : buildings) {
			if (building.getBuildingType() == buildingType) {
				WorldObject worldObject = world.findWorldObjectById(building.getId());
				if (testFunction.apply(worldObject).booleanValue()) {
					worldObjects.add(worldObject);
				}
			}
		}
		
		return worldObjects;
	}

	public boolean contains(int targetId) {
		for(Building building : buildings) {
			if (building.getId() == targetId) {
				return true;
			}
		}
		return false;
	}

	public void removeAll() {
		buildings.clear();
	}
}
