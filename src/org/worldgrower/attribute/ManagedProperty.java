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
import java.io.Serializable;
import java.util.List;

import org.worldgrower.Constants;

/**
 * A ManagedProperty manages a certain type T.
 * It is managed because it provides limits to possible values.
 * The checkValue method blocks invalid or out of range values.
 */
public interface ManagedProperty<T> extends Serializable {
	public String getName();
	public void checkValue(T value);
	public int getOrdinal();
	
	public default Object readResolveImpl() throws ObjectStreamException {
		int ordinal = getOrdinal();
		List<ManagedProperty<?>> allProperties = Constants.ALL_PROPERTIES;
		
		for(ManagedProperty<?> managedProperty : allProperties) {
			if (managedProperty.getOrdinal() == ordinal) {
				return managedProperty;
			}
		}
		throw new IllegalStateException("ManagedProperty with ordinal " + ordinal + " not found");
	}
	public T copy(Object value);
}
