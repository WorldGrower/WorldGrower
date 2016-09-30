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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public abstract class AbstractIdMap implements IdMap {

	private final boolean normalize;
	
	public AbstractIdMap(boolean normalize) {
		this.normalize = normalize;
	}

	private final Map<Integer, Integer> idsToValue = new HashMap<>();
	
	@Override
	public final void incrementValue(int id, int value) {
		int currentValue = getValue(id);
		
		int newValue = currentValue + value;
		if (normalize) {
			newValue = Constants.RELATIONSHIP_VALUE.normalize(newValue);
		}
		idsToValue.put(id, newValue);
	}
	
	@Override
	public final int getValue(int id) {
		Integer value = idsToValue.get(id);
		return value != null ? value.intValue() : 0;
	}
	
	@Override
	public final int getValue(WorldObject worldObject) {
		return getValue(worldObject.getProperty(Constants.ID));
	}
	
	@Override
	public final int getSumOfAllValues() {
		int sumOfAllValues = 0;
		for(Entry<Integer, Integer> entry : idsToValue.entrySet()) {
			sumOfAllValues += entry.getValue();
		}
		return sumOfAllValues;
	}
	
	@Override
	public final int findBestId(Predicate<WorldObject> predicate, World world) {
		int bestId = -1;
		int bestRelationshipValue = Integer.MIN_VALUE;
		for(Entry<Integer, Integer> entry : idsToValue.entrySet()) {
			int id = entry.getKey();
			int relationshipValue = entry.getValue();
			
			// id may not exist in world because it's filtered out by
			// WorldFacade, for example being invisible
			if (world.exists(id)) {
				WorldObject person = world.findWorldObjectById(id);
				
				if (relationshipValue > bestRelationshipValue && predicate.test(person)) {
					bestRelationshipValue = relationshipValue;
					bestId = id;
				}
			}
		}
		
		return bestId;
	}
	
	@Override
	public final int findWorstId(World world) {
		int worstId = -1;
		int worstRelationshipValue = Integer.MAX_VALUE;
		for(Entry<Integer, Integer> entry : idsToValue.entrySet()) {
			int id = entry.getKey();
			int relationshipValue = entry.getValue();
			
			if (relationshipValue < worstRelationshipValue) {
				worstRelationshipValue = relationshipValue;
				worstId = id;
			}
		}
		
		return worstId;
	}
	
	@Override
	public final int findBestId(Predicate<WorldObject> predicate, Comparator<WorldObject> comparator,  World world) {
		WorldObject bestPerson = null;
		for(Entry<Integer, Integer> entry : idsToValue.entrySet()) {
			int id = entry.getKey();
			// id may not exist in world because it's filtered out by
			// WorldFacade, for example being invisible
			if (world.exists(id)) {
				WorldObject person = world.findWorldObjectById(id);
				
				if (predicate.test(person)) {
					if (bestPerson == null || comparator.compare(bestPerson, person) < 0) {
						bestPerson = person;
					}
				}
			}
		}
		
		if (bestPerson != null) {
			return bestPerson.getProperty(Constants.ID);
		} else {
			return -1;
		}
	}
	
	@Override
	public final List<Integer> getIds() {
		return new ArrayList<>(idsToValue.keySet());
	}
	
	@Override
	public final List<Integer> getIdsWithoutTarget(WorldObject target) {
		List<Integer> ids = getIds();
		ids.remove(target.getProperty(Constants.ID));
		return ids;
	}

	@Override
	public final boolean contains(WorldObject worldObject) {
		return idsToValue.containsKey(worldObject.getProperty(Constants.ID));
	}
	
	@Override
	public final String toString() {
		return "[" + idsToValue + "]";
	}

	@Override
	public final void incrementValue(WorldObject worldObject, int value) {
		incrementValue(worldObject.getProperty(Constants.ID), value);
	}

	@Override
	public final void remove(int id) {
		idsToValue.remove(id);
	}
	
	@Override
	public final void remove(WorldObject worldObject) {
		idsToValue.remove(worldObject.getProperty(Constants.ID));
	}
	
	protected final void copyContent(AbstractIdMap idMap) {
		idMap.idsToValue.putAll(this.idsToValue);
	}
	
	@Override
	public final void remove(WorldObject worldObject, ManagedProperty<?> property, int id) {
		IdMapProperty idMapProperty = (IdMapProperty) property;
		worldObject.getProperty(idMapProperty).remove(id);
	}
}