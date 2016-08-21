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

import org.worldgrower.gui.ImageIds;

public enum BuildingType {
	SHACK("shack", ImageIds.SHACK, 10),
	HOUSE("house", ImageIds.HOUSE6, 50), 
	INN("inn", ImageIds.INN, 200),
	BREWERY("brewery", ImageIds.BREWERY, 50),
	SMITH("smith", ImageIds.SMITH, 50),
	WORKBENCH("workbench", ImageIds.WORKBENCH, 50),
	PAPERMILL("papermill", ImageIds.PAPER_MILL, 50),
	WEAVERY("weavery", ImageIds.WEAVERY, 50),
	APOTHECARY("apothecary", ImageIds.APOTHECARY, 50),
	CHEST("chest", ImageIds.CHEST, 5);
	
	private final String description;
	private final ImageIds imageId;
	private final int price;

	private BuildingType(String description, ImageIds imageId, int price) {
		this.description = description;
		this.imageId = imageId;
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}

	public ImageIds getImageId() {
		return imageId;
	}

	public int getPrice() {
		return price;
	}
}
