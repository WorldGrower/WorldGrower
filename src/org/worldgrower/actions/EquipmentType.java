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
package org.worldgrower.actions;

import org.worldgrower.gui.ImageIds;

public enum EquipmentType {
	COTTON("Cotton", ImageIds.COTTON),
	LEATHER("Leather", ImageIds.LEATHER),
	IRON("Iron", ImageIds.IRON), 
	STEEL("Steel", ImageIds.STEEL), 
	TOOL("Tool", ImageIds.SCYTHE),
	FURNITURE("Furniture", ImageIds.KITCHEN_ICON);
	
	private final String description;
	private final ImageIds imageId;

	private EquipmentType(String description, ImageIds imageId) {
		this.description = description;
		this.imageId = imageId;
	}

	public String getDescription() {
		return description;
	}

	public ImageIds getImageId() {
		return imageId;
	}
}
