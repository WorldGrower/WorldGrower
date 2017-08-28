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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class Demands implements Serializable, Iterable<IntProperty> {
	private PropertyCountMap<ManagedProperty<?>> properties = new PropertyCountMap<>();

	public void add(IntProperty propertyKey, int quantity) {
		properties.add(propertyKey, quantity);
	}

	public int count(IntProperty propertyKey) {
		return properties.count(propertyKey);
	}

	public void remove(IntProperty propertyKey) {
		properties.remove(propertyKey);
	}

	public void addAll(Demands otherDemands) {
		properties.addAll(otherDemands.properties);
		
	}

	public List<? extends IntProperty> propertyKeys() {
		return (List<? extends IntProperty>) properties.keySet();
	}

	public int size() {
		return properties.size();
	}

	@Override
	public Iterator<IntProperty> iterator() {
		return (Iterator<IntProperty>) propertyKeys().iterator();
	}
	
	public List<IntProperty> getSortedDemands() {
		List<IntProperty> demandsList = new ArrayList<>(propertyKeys());
		Collections.sort(demandsList, new DemandsComparator());
		return demandsList;
	}
	
	private static class DemandsComparator implements Comparator<IntProperty> {

		@Override
		public int compare(IntProperty o1, IntProperty o2) {
			return o1.getName().compareTo(o2.getName());
		}	
	}
}
