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

import org.worldgrower.Constants;
import org.worldgrower.gui.ImageIds;

public enum BuildingType {
	SHACK("shack", ImageIds.SHACK, 10, Constants.SLEEP_COMFORT),
	HOUSE("house", ImageIds.HOUSE6, 50, Constants.SLEEP_COMFORT), 
	INN("inn", ImageIds.INN, 200, Constants.SLEEP_COMFORT),
	BREWERY("brewery", ImageIds.BREWERY, 50, Constants.BREWERY_QUALITY),
	SMITH("smith", ImageIds.SMITH, 50, Constants.SMITH_QUALITY),
	WORKBENCH("workbench", ImageIds.WORKBENCH, 50, Constants.WORKBENCH_QUALITY),
	PAPERMILL("papermill", ImageIds.PAPER_MILL, 50, Constants.PAPER_MILL_QUALITY),
	WEAVERY("weavery", ImageIds.WEAVERY, 50, Constants.WEAVERY_QUALITY),
	APOTHECARY("apothecary", ImageIds.APOTHECARY, 50, Constants.APOTHECARY_QUALITY),
	CHEST("chest", ImageIds.CHEST, 5, Constants.LOCK_STRENGTH);
	
	private final String description;
	private final ImageIds imageId;
	private final int price;
	private final ManagedProperty<?> qualityProperty;

	private BuildingType(String description, ImageIds imageId, int price, ManagedProperty<?> qualityProperty) {
		this.description = description;
		this.imageId = imageId;
		this.price = price;
		this.qualityProperty = qualityProperty;
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

	public ManagedProperty<?> getQualityProperty() {
		return qualityProperty;
	}
}
