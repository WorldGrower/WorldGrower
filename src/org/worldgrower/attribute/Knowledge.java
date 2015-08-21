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

	public ManagedProperty<?> getManagedProperty() {
		return managedProperty;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return managedProperty.getName() + ":" + value;
	}
}