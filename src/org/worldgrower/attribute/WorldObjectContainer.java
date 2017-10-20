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

import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

/**
 * A WorldObjectContainer holds a list of WorldObjects and provides methods for manipulating them.
 */
public final class WorldObjectContainer implements Serializable {
	private final Int2ObjectOpenHashMap<WorldObject> worldObjects = new Int2ObjectOpenHashMap<WorldObject>();
	private int currentIndex = 0;
	
	public void add(WorldObject worldObject) {
		worldObjects.put(currentIndex++, worldObject);
	}
	
	public WorldObject remove(int index) {
		return worldObjects.remove(index);
	}
	
	public void iterate(ObjIntConsumer<WorldObject> consumer) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject object = entry.getValue();
			consumer.accept(object, entry.getIntKey());
		}
	}

	public WorldObject get(int index) {
		return worldObjects.get(index);
	}
	
	public void addQuantity(int index) {
		addQuantity(get(index));
	}
	
	public void addQuantity(WorldObject worldObject) {
		addQuantity(worldObject, 1);
	}
	
	public void addUniqueQuantity(WorldObject worldObject) {
		worldObject.setProperty(Constants.QUANTITY, 1);
		add(worldObject);
	}
	
	public void addQuantity(WorldObject worldObject, int quantity) {
		String name = worldObject.getProperty(Constants.NAME);
		boolean found = false;
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject object = entry.getValue();
			if (object.getProperty(Constants.NAME).equals(name)) {
				object.setProperty(Constants.QUANTITY, object.getProperty(Constants.QUANTITY) + quantity);
				found = true;
				return;
			}
		}
		
		if (!found) {
			worldObject.setProperty(Constants.QUANTITY, quantity);
			add(worldObject);
		}
	}
	
	public<T> void removeQuantity(ManagedProperty<T> propertyKey, int quantity) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject object = entry.getValue();
			if (object.hasProperty(propertyKey)) {
				object.increment(Constants.QUANTITY, -quantity);
				
				if (object.getProperty(Constants.QUANTITY) == 0) {
					worldObjects.remove(entry.getIntKey());
				}
				
				return;
			}
		}
	}
	
	public<T> int getQuantityFor(ManagedProperty<T> propertyKey) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey)) {
				return worldObject.getProperty(Constants.QUANTITY);
			}
		}
		return 0;
	}
	
	public int getQuantityFor(ManagedProperty<?> propertyKey1, ManagedProperty<?> propertyKey2, Function<WorldObject, Boolean> testFunction) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey1) && worldObject.hasProperty(propertyKey2)) {
				if (testFunction.apply(worldObject)) {
					return worldObject.getProperty(Constants.QUANTITY);
				}
			}
		}
		return 0;
	}
	
	public<T> int getQuantityFor(ManagedProperty<T> propertyKey1, ManagedProperty<T> propertyKey2) {
		return getQuantityFor(propertyKey1, propertyKey2, w -> true);
	}
	
	public<T> List<WorldObject> getWorldObjects(ManagedProperty<T> propertyKey, T value) {
		List<WorldObject> result = new ArrayList<>();
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey)) {
				if (worldObject.getProperty(propertyKey) == value) {
					result.add(worldObject);
				}
			}
		}
		return result;
	}
	
	public List<WorldObject> getWorldObjectsByFunction(ManagedProperty<?> propertyKey, Function<WorldObject, Boolean> testFunction) {
		List<WorldObject> result = new ArrayList<>();
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey)) {
				if (testFunction.apply(worldObject)) {
					result.add(worldObject);
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "[" + worldObjects + "]";
	}

	public<T> int getIndexFor(Function<WorldObject, Boolean> testFunction) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (testFunction.apply(worldObject)) {
				return entry.getIntKey();
			}
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey)) {
				return entry.getIntKey();
			}
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey, Function<WorldObject, Boolean> testFunction) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey) && testFunction.apply(worldObject)) {
				return entry.getIntKey();
			}
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey, T value) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey) && worldObject.getProperty(propertyKey) == value) {
				return entry.getIntKey();
			}
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey, T value, Function<WorldObject, Boolean> testFunction) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey) && (worldObject.getProperty(propertyKey) == value) && testFunction.apply(worldObject)) {
				return entry.getIntKey();
			}
		}
		return -1;
	}

	public<T> void removeAllQuantity(ManagedProperty<T> propertyKey) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(propertyKey)) {
				worldObjects.remove(entry.getIntKey());
				return;
			}
		}
	}

	public void addDemands(WorldObjectContainer demands) {
		demands.iterate((worldObject, index) -> this.addQuantity(worldObject));
	}
	
	public WorldObjectContainer copy() {
		WorldObjectContainer result = new WorldObjectContainer();
		result.currentIndex = currentIndex;
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			result.worldObjects.put(entry.getIntKey(), worldObject.deepCopy());
		}
		return result;
	}

	public <T> void setProperty(int index, ManagedProperty<T> propertyKey, T value) {
		worldObjects.get(index).setProperty(propertyKey, value);
	}

	public int getIndexFor(StringProperty property, String value, Function<WorldObject, Boolean> testFunction) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject.hasProperty(property) && worldObject.getProperty(property).equals(value)) {
				if (testFunction.apply(worldObject)) {
					return entry.getIntKey();
				}
			}
		}
		return -1;
	}
	
	public int getIndexFor(WorldObject worldObjectToFind) {
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			if (worldObject == worldObjectToFind) {
				return entry.getIntKey();
			}
		}
		return -1;
	}

	public void removeQuantity(int index, int quantity) {
		WorldObject object = worldObjects.get(index);
		object.increment(Constants.QUANTITY, -quantity);
			
		if (object.getProperty(Constants.QUANTITY) == 0) {
			worldObjects.remove(index);
		}
	}

	public void moveItemsFrom(WorldObjectContainer otherInventory) {
		ObjectIterator<Entry<WorldObject>> iterator = otherInventory.worldObjects.int2ObjectEntrySet().fastIterator();
		while(iterator.hasNext()) {
			Entry<WorldObject> otherEntry = iterator.next();
			WorldObject otherWorldObject = otherEntry.getValue();
		
			if (otherWorldObject.getProperty(Constants.QUANTITY) == null) {
				throw new IllegalStateException("otherWorldObject.getProperty(Constants.QUANTITY) is null: " + otherWorldObject);
			}
			addQuantity(otherWorldObject, otherWorldObject.getProperty(Constants.QUANTITY));
			iterator.remove();
		}
	}
	
	public int getUnmodifiedTotalWeight() {
		int totalWeight = 0;
		for(Entry<WorldObject> entry : worldObjects.int2ObjectEntrySet()) {
			WorldObject worldObject = entry.getValue();
			Integer weight = worldObject.getProperty(Constants.WEIGHT);
			if (weight != null) {
				int quantity = worldObject.getProperty(Constants.QUANTITY);
				totalWeight += (weight.intValue() * quantity);
			}
		}
		return totalWeight;
	}
}
