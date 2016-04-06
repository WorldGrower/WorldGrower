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

public class WorldObjectProperties implements Serializable {

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
			return (T) worldObjectProperty.value;
		} else {
			return null;
		}
	}
	
	public<T> void put(ManagedProperty<T> propertyKey, T value) {
		WorldObjectProperty worldObjectProperty = this.properties[propertyKey.getOrdinal()];
		if (worldObjectProperty != null) {
			worldObjectProperty.value = value;
		} else {
			this.properties[propertyKey.getOrdinal()] = new WorldObjectProperty(propertyKey, value);
		}
	}
	
	public boolean containsKey(ManagedProperty<?> propertyKey) {
		return this.properties[propertyKey.getOrdinal()] != null;
	}

	public WorldObjectProperties shallowCopy() {
		WorldObjectProperty[] copyOfProperties = new WorldObjectProperty[properties.length];
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				copyOfProperties[i] = new WorldObjectProperty(properties[i].managedProperty, properties[i].value);
			}
		}
		
		return new WorldObjectProperties(copyOfProperties);
	}
	
	public WorldObjectProperties deepCopy() {
		WorldObjectProperty[] copyOfProperties = new WorldObjectProperty[properties.length];
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				ManagedProperty<?> managedProperty = properties[i].managedProperty;
				copyOfProperties[i] = new WorldObjectProperty(managedProperty, managedProperty.copy(properties[i].value));
			}
		}
		
		return new WorldObjectProperties(copyOfProperties);
	}

	public List<ManagedProperty<?>> keySet() {
		List<ManagedProperty<?>> keySet = new ArrayList<>();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				keySet.add(properties[i].managedProperty);
			}
		}
		return keySet;
	}

	public List<Entry<ManagedProperty<?>, Object>> entrySet() {
		List<Entry<ManagedProperty<?>, Object>> entrySet = new ArrayList<>();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				entrySet.add(new AbstractMap.SimpleEntry(properties[i].managedProperty, properties[i].value));
			}
		}
		return entrySet;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(int i=0; i<properties.length; i++) {
			if (properties[i] != null) {
				result.append(properties[i].managedProperty.getName()).append("=").append(properties[i].value).append(", ");
			}
		}
		return result.toString();
	}



	private static class WorldObjectProperty implements Serializable {
		public WorldObjectProperty(ManagedProperty<?> managedProperty, Object value) {
			this.managedProperty = managedProperty;
			this.value = value;
		}
		
		public ManagedProperty<?> managedProperty;
		public Object value;
	}



	public<T> void remove(ManagedProperty<T> propertyKey) {
		this.properties[propertyKey.getOrdinal()] = null;
	}
}
