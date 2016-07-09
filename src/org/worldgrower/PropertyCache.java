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
package org.worldgrower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.attribute.ManagedProperty;

/**
 * This class holds info about which WorldObjects have a certain ManagedProperty.
 */
class PropertyCache implements Serializable {
	private static final int PROPERTY_COUNT = Constants.ALL_PROPERTIES.size();
	private static final ManagedProperty<?>[]  ALL_PROPERTIES = Constants.ALL_PROPERTIES.toArray(new ManagedProperty[0]);
	
	@SuppressWarnings("unchecked")
	private final List<Integer>[] propertyToIdsMapping = new ArrayList[PROPERTY_COUNT];
	
	public void idAdded(WorldObject worldObject, World world) {
		for(int i=0; i <PROPERTY_COUNT; i++) {
			ManagedProperty<?> property = ALL_PROPERTIES[i];
			if (worldObject.hasProperty(property)) {
				List<Integer> ids = propertyToIdsMapping[i];
				if (ids == null) {
					ids = initializeIdsList(property, world);
					propertyToIdsMapping[i] = ids;
				} else {
					ids.add(worldObject.getProperty(Constants.ID));
				}
			}
		}
	}
	
	public void idRemoved(WorldObject worldObjectToRemove) {
		Integer id = worldObjectToRemove.getProperty(Constants.ID);
		for(int i=0; i <PROPERTY_COUNT; i++) {
			List<Integer> ids = propertyToIdsMapping[i];
			if (ids != null) {
				ids.remove(id);
			}
		}
	}
	
	public List<WorldObject> findWorldObjectsByProperty(ManagedProperty<?> managedProperty, WorldObjectCondition worldObjectCondition, World world) {
		int index = managedProperty.getOrdinal();
		List<Integer> ids = propertyToIdsMapping[index];
		if (ids == null) {
			ids = initializeIdsList(managedProperty, world);
			propertyToIdsMapping[index] = ids;
		}
		return mapIdsToWorldObjects(ids, worldObjectCondition, world);
	}

	private List<WorldObject> mapIdsToWorldObjects(List<Integer> ids, WorldObjectCondition worldObjectCondition, World world) {
		List<WorldObject> result = new ArrayList<>(ids.size());
		for(Integer id : ids) {
			WorldObject worldObject = world.findWorldObject(Constants.ID, id);
			if (worldObjectCondition.isWorldObjectValid(worldObject)) {
				result.add(worldObject);
			}
		}
		return result;
	}

	private List<Integer> initializeIdsList(ManagedProperty<?> managedProperty, World world) {
		List<Integer> ids = new ArrayList<Integer>();
		for(WorldObject worldObject : world.getWorldObjects()) {
			if (worldObject.hasProperty(managedProperty)) {
				ids.add(worldObject.getProperty(Constants.ID));
			}
		}
		return ids;
	}
}
