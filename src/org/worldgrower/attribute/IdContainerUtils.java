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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class IdContainerUtils {

	public static void removeIdContainers(WorldObject worldObjectToRemove, World world) {
		int id = worldObjectToRemove.getProperty(Constants.ID);
		for(WorldObject worldObject : world.getWorldObjects()) {
			removeIdPropertiesFromWorldObject(id, worldObject);
		}
	}

	public static void removeIdPropertiesFromWorldObject(int id, WorldObject worldObject) {
		List<IdContainer> worldObjectIds = Constants.getIdProperties();
		for(IdContainer worldObjectId : worldObjectIds) {
			ManagedProperty<?> property = (ManagedProperty<?>) worldObjectId;
			if (worldObject.hasProperty(property) && (worldObject.getProperty(property) != null)) {
				worldObjectId.remove(worldObject, property, id);
			}
		}
	}
}
