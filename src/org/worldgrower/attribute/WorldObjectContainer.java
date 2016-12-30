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
import java.util.List;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

/**
 * A WorldObjectContainer holds a list of WorldObjects and provides methods for manipulating them.
 */
public class WorldObjectContainer implements Serializable {

	private final List<WorldObject> worldObjects = new ArrayList<>(); 
	
	public void add(WorldObject worldObject) {
		worldObjects.add(worldObject);
	}
	
	public WorldObject remove(int index) {
		return worldObjects.set(index, null);
	}
	
	public int size() {
		return worldObjects.size();
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
		for(WorldObject object : worldObjects) {
			if (object != null) {
				if (object.getProperty(Constants.NAME).equals(name)) {
					object.setProperty(Constants.QUANTITY, object.getProperty(Constants.QUANTITY) + quantity);
					found = true;
					return;
				}
			}
		}
		
		if (!found) {
			worldObject.setProperty(Constants.QUANTITY, quantity);
			add(worldObject);
		}
	}
	
	public<T> void removeQuantity(ManagedProperty<T> propertyKey, int quantity) {
		for(int i=0; i<worldObjects.size(); i++) {
			WorldObject object = worldObjects.get(i);
			if (object != null) {
				if (object.hasProperty(propertyKey)) {
					object.increment(Constants.QUANTITY, -quantity);
					
					if (object.getProperty(Constants.QUANTITY) == 0) {
						worldObjects.set(i, null);
					}
					
					return;
				}
			}
		}
	}
	
	public<T> int getQuantityFor(ManagedProperty<T> propertyKey) {
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey)) {
					return worldObject.getProperty(Constants.QUANTITY);
				}
			}
		}
		return 0;
	}
	
	public int getQuantityFor(ManagedProperty<?> propertyKey1, ManagedProperty<?> propertyKey2, Function<WorldObject, Boolean> testFunction) {
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey1) && worldObject.hasProperty(propertyKey2)) {
					if (testFunction.apply(worldObject)) {
						return worldObject.getProperty(Constants.QUANTITY);
					}
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
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey)) {
					if (worldObject.getProperty(propertyKey) == value) {
						result.add(worldObject);
					}
				}
			}
		}
		return result;
	}
	
	public List<WorldObject> getWorldObjectsByFunction(ManagedProperty<?> propertyKey, Function<WorldObject, Boolean> testFunction) {
		List<WorldObject> result = new ArrayList<>();
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey)) {
					if (testFunction.apply(worldObject)) {
						result.add(worldObject);
					}
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
		int index = 0; 
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (testFunction.apply(worldObject)) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey) {
		int index = 0; 
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey)) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey, T value) {
		int index = 0; 
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey) && worldObject.getProperty(propertyKey) == value) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}
	
	public<T> int getIndexFor(ManagedProperty<T> propertyKey, T value, Function<WorldObject, Boolean> testFunction) {
		int index = 0; 
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(propertyKey) && (worldObject.getProperty(propertyKey) == value) && testFunction.apply(worldObject)) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}

	public<T> void removeAllQuantity(ManagedProperty<T> propertyKey) {
		for(int i=0; i<worldObjects.size(); i++) {
			WorldObject object = worldObjects.get(i);
			if (object != null) {
				if (object.hasProperty(propertyKey)) {
					worldObjects.set(i, null);
					return;
				}
			}
		}
	}

	public void addDemands(WorldObjectContainer demands) {
		for(WorldObject worldObject : demands.worldObjects) {
			this.addQuantity(worldObject);
		}
	}
	
	public WorldObjectContainer copy() {
		WorldObjectContainer result = new WorldObjectContainer();
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				result.add(worldObject.deepCopy());
			}
		}
		return result;
	}

	public <T> void setProperty(int index, ManagedProperty<T> propertyKey, T value) {
		worldObjects.get(index).setProperty(propertyKey, value);
	}

	public int getIndexFor(StringProperty property, String value, Function<WorldObject, Boolean> testFunction) {
		int index = 0;
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject.hasProperty(property) && worldObject.getProperty(property).equals(value)) {
					if (testFunction.apply(worldObject)) {
						return index;
					}
				}
			}
			index++;
		}
		return -1;
	}
	
	public boolean contains(WorldObject worldObjectToFind) {
		for(WorldObject worldObject : worldObjects) {
			if (worldObject != null) {
				if (worldObject == worldObjectToFind) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeQuantity(int index, int quantity) {
		WorldObject object = worldObjects.get(index);
		object.increment(Constants.QUANTITY, -quantity);
			
		if (object.getProperty(Constants.QUANTITY) == 0) {
			worldObjects.set(index, null);
		}
	}

	public void moveItemsFrom(WorldObjectContainer otherInventory) {
		for(int index=0; index<otherInventory.size(); index++) {
			WorldObject otherWorldObject = otherInventory.get(index);
			if (otherWorldObject != null) {
				addQuantity(otherWorldObject, otherWorldObject.getProperty(Constants.QUANTITY));
				otherInventory.remove(index);
			}
		}
	}
}
