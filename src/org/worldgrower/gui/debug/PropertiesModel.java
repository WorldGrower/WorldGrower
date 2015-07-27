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
package org.worldgrower.gui.debug;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;

public class PropertiesModel extends AbstractTableModel {

	private WorldObject worldObject;
	
	public PropertiesModel(WorldObject worldObject) {
		super();
		this.worldObject = worldObject;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return worldObject.getPropertyKeys().size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		List<ManagedProperty<?>> propertyKeys = worldObject.getPropertyKeys();
		Collections.sort(propertyKeys, new PropertyComparator());
		if (column == 0) {
			return propertyKeys.get(row).getName();
		} else {
			return worldObject.getProperty(propertyKeys.get(row));
		}
	}
	
	public static class PropertyComparator implements Comparator<ManagedProperty<?>> {

		@Override
		public int compare(ManagedProperty<?> p1, ManagedProperty<?> p2) {
			return p1.getName().toUpperCase().compareTo(p2.getName().toUpperCase());
		}
	}
}