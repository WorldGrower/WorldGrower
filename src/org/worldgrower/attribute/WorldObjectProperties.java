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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class WorldObjectProperties implements Serializable {

	private final WorldObjectProperty[] properties;
	
	public WorldObjectProperties(Map<ManagedProperty<?>, Object> properties) {
		this.properties = new WorldObjectProperty[OrdinalGenerator.getNumberOfProperties()];
		for(Entry<ManagedProperty<?>, Object> entry : properties.entrySet()) {
			ManagedProperty managedProperty = entry.getKey();
			Object value = entry.getValue();
			this.properties[managedProperty.getOrdinal()] = new WorldObjectProperty(managedProperty, value);
		}
	}
	
	private WorldObjectProperties(WorldObjectProperty[] properties) {
		this.properties = properties;
	}
	
	public<T> T get(ManagedProperty<T> propertyKey) {
		WorldObjectProperty worldObjectProperty = this.properties[propertyKey.getOrdinal()];
		if (worldObjectProperty != null) {
			return (T) worldObjectProperty.getValue();
		} else {
			return null;
		}
	}
	
	public<T> void put(ManagedProperty<T> propertyKey, T value) {
		WorldObjectProperty worldObjectProperty = this.properties[propertyKey.getOrdinal()];
		if (worldObjectProperty != null) {
			worldObjectProperty.setValue(value);
		} else {
			this.properties[propertyKey.getOrdinal()] = new WorldObjectProperty(propertyKey, value);
		}
	}
	
	public boolean containsKey(ManagedProperty<?> propertyKey) {
		return this.properties[propertyKey.getOrdinal()] != null;
	}

	public WorldObjectProperties shallowCopy() {
		int propertiesLength = properties.length;
		WorldObjectProperty[] copyOfProperties = new WorldObjectProperty[propertiesLength];
		for(int i=0; i<propertiesLength; i++) {
			if (properties[i] != null) {
				copyOfProperties[i] = new WorldObjectProperty(properties[i]);
			}
		}
		
		return new WorldObjectProperties(copyOfProperties);
	}
	
	public WorldObjectProperties deepCopy() {
		int propertiesLength = properties.length;
		WorldObjectProperty[] copyOfProperties = new WorldObjectProperty[propertiesLength];
		for(int i=0; i<propertiesLength; i++) {
			if (properties[i] != null) {
				copyOfProperties[i] = properties[i].copy();
			}
		}
		
		return new WorldObjectProperties(copyOfProperties);
	}

	public List<ManagedProperty<?>> keySet() {
		List<ManagedProperty<?>> keySet = new ArrayList<>();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				keySet.add(properties[i].getManagedProperty());
			}
		}
		return keySet;
	}

	public List<Entry<ManagedProperty<?>, Object>> entrySet() {
		List<Entry<ManagedProperty<?>, Object>> entrySet = new ArrayList<>();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				entrySet.add(properties[i].getMapEntry());
			}
		}
		return entrySet;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				result.append(properties[i].toString()).append(", ");
			}
		}
		return result.toString();
	}

	public<T> void remove(ManagedProperty<T> propertyKey) {
		this.properties[propertyKey.getOrdinal()] = null;
	}

	private static class WorldObjectProperty implements Serializable {

		private ManagedProperty<?> managedProperty;
		private Object value;
		
		public WorldObjectProperty(WorldObjectProperty worldObjectProperty) {
			this.managedProperty = worldObjectProperty.managedProperty;
			this.value = worldObjectProperty.value;
		}
		
		public WorldObjectProperty(ManagedProperty<?> managedProperty, Object value) {
			this.managedProperty = managedProperty;
			this.value = value;
		}
		
		public WorldObjectProperty copy() {
			return new WorldObjectProperty(managedProperty, managedProperty.copy(value));
		}

		public ManagedProperty<?> getManagedProperty() {
			return managedProperty;
		}
		
		public Map.Entry<ManagedProperty<?>, Object> getMapEntry() {
			return new AbstractMap.SimpleEntry<ManagedProperty<?>, Object>(managedProperty, value);
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return managedProperty.getName() + "=" + value;
		}
	}
}
