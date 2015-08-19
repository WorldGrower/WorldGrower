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

public class PropertyCountMap<T> implements Serializable {

	private final Map<T, Integer> propertyCounts = new HashMap<>(); 
	
	public void add(T propertyKey, int quantity) {
		if (propertyCounts.containsKey(propertyKey)) {
			propertyCounts.put(propertyKey, propertyCounts.get(propertyKey) + quantity);
		} else {
			propertyCounts.put(propertyKey, quantity);
		}
	}
	
	public int size() {
		return propertyCounts.size();
	}
	
	public List<T> keySet() {
		return new ArrayList<>(propertyCounts.keySet());
	}
	
	public int count(T propertyKey) {
		if (propertyCounts.containsKey(propertyKey)) {
			return propertyCounts.get(propertyKey);
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "[" + propertyCounts + "]";
	}

	public PropertyCountMap<T> copy() {
		PropertyCountMap<T> result = new PropertyCountMap<T>();
		result.propertyCounts.putAll(this.propertyCounts);
		return result;
	}

	public void addAll(PropertyCountMap<T> propertyMap) {
		for (T propertyKey : propertyMap.keySet()) {
			add(propertyKey, propertyMap.count(propertyKey));
		}
	}

	public void remove(T managedProperty) {
		propertyCounts.remove(managedProperty);
	}
}
