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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class IdMap implements Serializable {

	private final Map<Integer, Integer> idsToValue = new HashMap<>();
	
	public void incrementValue(int id, int value) {
		int currentValue = getValue(id);
		
		int newValue = currentValue + value;
		newValue = Constants.RELATIONSHIP_VALUE.normalize(newValue);
		idsToValue.put(id, newValue);
	}
	
	public int getValue(int id) {
		Integer value = idsToValue.get(id);
		return value != null ? value.intValue() : 0;
	}
	
	public int getValue(WorldObject worldObject) {
		return getValue(worldObject.getProperty(Constants.ID));
	}
	
	public int findBestId(Predicate<WorldObject> predicate, World world) {
		int bestId = -1;
		int bestRelationshipValue = Integer.MIN_VALUE;
		for(Entry<Integer, Integer> entry : idsToValue.entrySet()) {
			int id = entry.getKey();
			int relationshipValue = entry.getValue();
			
			WorldObject person = world.findWorldObject(Constants.ID, id);
			
			if (relationshipValue > bestRelationshipValue && predicate.test(person)) {
				bestRelationshipValue = relationshipValue;
				bestId = id;
			}
		}
		
		return bestId;
	}
	
	public List<Integer> getIds() {
		return new ArrayList<>(idsToValue.keySet());
	}
	
	public List<Integer> getIdsWithoutTarget(WorldObject target) {
		List<Integer> ids = getIds();
		ids.remove(target.getProperty(Constants.ID));
		return ids;
	}

	public boolean contains(WorldObject worldObject) {
		return idsToValue.containsKey(worldObject.getProperty(Constants.ID));
	}
	
	@Override
	public String toString() {
		return "[" + idsToValue + "]";
	}
	
	public IdMap copy() {
		IdMap idMap = new IdMap();
		idMap.idsToValue.putAll(this.idsToValue);
		return idMap;
	}

	public void incrementValue(WorldObject worldObject, int value) {
		incrementValue(worldObject.getProperty(Constants.ID), value);
	}
}
