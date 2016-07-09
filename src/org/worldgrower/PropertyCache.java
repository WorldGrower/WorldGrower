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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.worldgrower.attribute.ManagedProperty;

/**
 * This class holds info about which WorldObjects have a certain ManagedProperty.
 */
class PropertyCache implements Serializable {

	private final Map<ManagedProperty<?>, List<Integer>> propertyToIdsMapping = new HashMap<>();
	
	public void idAdded(WorldObject worldObject) {
		// only add properties to already cached properties
		for(Entry<ManagedProperty<?>, List<Integer>> entry : propertyToIdsMapping.entrySet()) {
			ManagedProperty<?> property = entry.getKey();
			if (worldObject.hasProperty(property)) {
				List<Integer> ids = entry.getValue();
				ids.add(worldObject.getProperty(Constants.ID));
			}
		}
	}
	
	public void idRemoved(WorldObject worldObjectToRemove) {
		Integer id = worldObjectToRemove.getProperty(Constants.ID);
		for(Entry<ManagedProperty<?>, List<Integer>> entry : propertyToIdsMapping.entrySet()) {
			List<Integer> ids = entry.getValue();
			//make sure that id is an Integer, not an int
			ids.remove(id);
		}
	}
	
	public List<WorldObject> findWorldObjectsByProperty(ManagedProperty<?> managedProperty, WorldObjectCondition worldObjectCondition, World world) {
		List<Integer> ids = propertyToIdsMapping.get(managedProperty);
		if (ids == null) {
			ids = new ArrayList<>();
			for(WorldObject worldObject : world.getWorldObjects()) {
				if (worldObject.hasProperty(managedProperty)) {
					ids.add(worldObject.getProperty(Constants.ID));
				}
			}

			propertyToIdsMapping.put(managedProperty, ids);
		}
		return ids.stream().map(i -> world.findWorldObject(Constants.ID, i)).filter(w -> worldObjectCondition.isWorldObjectValid(w)).collect(Collectors.toList());
		
	}
}
