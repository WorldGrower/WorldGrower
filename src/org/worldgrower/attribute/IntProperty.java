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

import java.io.ObjectStreamException;
import java.util.List;


public class IntProperty implements ManagedProperty<Integer> {

	private final String name;
	private final Integer minValue;
	private final Integer maxValue;
	private final boolean nullable;
	private final int ordinal = OrdinalGenerator.getNextOrdinal();
	
	public IntProperty(String name, Integer minValue, Integer maxValue, boolean nullable, List<ManagedProperty<?>> allProperties) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.nullable = nullable;
		allProperties.add(this);
	}

	@Override
	public final void checkValue(Integer value) {
		if ((!nullable) && (value == null)) {
			throw new IllegalStateException("value " + value + " is null for " + name);
		}
		
		if ((minValue != null) && (value != null) && (value < minValue)) {
			throw new IllegalStateException("value " + value + " is lower than minValue " + minValue + " for " + name);
		}
		if ((maxValue != null) && (value != null) && (value > maxValue)) {
			throw new IllegalStateException("value " + value + " is higher than maxValue " + maxValue + " for " + name);
		}		
	}
	
	public int normalize(int currentValue) {
		if (minValue != null) {
			currentValue = Math.max(minValue, currentValue);
		}
		if (maxValue != null) {
			currentValue = Math.min(maxValue, currentValue);
		}
		return currentValue;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int getOrdinal() {
		return ordinal;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public Integer copy(Object value) {
		return (Integer) value;
	}
}