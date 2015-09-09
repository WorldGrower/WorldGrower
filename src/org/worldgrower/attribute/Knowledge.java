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

public class Knowledge implements Serializable {
	private final ManagedProperty<?> managedProperty;
	private final Object value;
	
	public Knowledge(ManagedProperty<?> managedProperty, Object value) {
		super();
		this.managedProperty = managedProperty;
		this.value = value;
	}

	public Knowledge(Knowledge knowledge) {
		super();
		this.managedProperty = knowledge.managedProperty;
		this.value = knowledge.value;
	}

	public ManagedProperty<?> getManagedProperty() {
		return managedProperty;
	}

	public Object getValue() {
		return value;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((managedProperty == null) ? 0 : managedProperty.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Knowledge other = (Knowledge) obj;
		if (managedProperty == null) {
			if (other.managedProperty != null)
				return false;
		} else if (!managedProperty.equals(other.managedProperty))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return managedProperty.getName() + ":" + value;
	}
}