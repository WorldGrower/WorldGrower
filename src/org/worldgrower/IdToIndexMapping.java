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
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;

/**
 * This mapping adds a mapping between Constants.ID and the index in the list of WorldObjects.
 * The list can have WorldObjects removed, which makes a one-on-one mapping harder.
 */
class IdToIndexMapping implements Serializable {

	private Int2IntArrayMap idToIndexMapping = new Int2IntArrayMap();

	private static final int NO_MAPPING_FOUND = Integer.MIN_VALUE;
	
	public IdToIndexMapping() {
		idToIndexMapping.defaultReturnValue(NO_MAPPING_FOUND);
	}
	
	public void idAdded(List<WorldObject> worldObjects) {
		int lastIndex = worldObjects.size() - 1;
		WorldObject lastWorldObject = worldObjects.get(lastIndex);
		idToIndexMapping.put(lastWorldObject.getProperty(Constants.ID).intValue(), lastIndex);
	}

	public void idRemoved(List<WorldObject> worldObjects) {
		idToIndexMapping.clear();
		for(int index = 0; index < worldObjects.size(); index++) {
			WorldObject worldObject = worldObjects.get(index);
			idToIndexMapping.put(worldObject.getProperty(Constants.ID).intValue(), index);
		}
	}

	public int getIndex(int id) {
		int index = idToIndexMapping.get(id);
		if (index == NO_MAPPING_FOUND) {
			throw new IllegalStateException("Id " + id + " not found in idToIndexMapping");
		} else {
			return index;
		}
	}

	public boolean idExists(int id) {
		return idToIndexMapping.containsKey(id);
	}
}
