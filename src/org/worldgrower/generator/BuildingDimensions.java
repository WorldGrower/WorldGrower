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
package org.worldgrower.generator;

import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.attribute.ManagedProperty;

public enum BuildingDimensions {
	ARENA(11, 10),
	JAIL(3, 4),
	SHACK(2, 3),
	HOUSE(3, 3),
	WELL(1, 1),
	WORKBENCH(4, 3),
	SMITH(2, 2),
	WEAVERY(4, 3),
	BREWERY(4, 3),
	APOTHECARY(4, 3),
	CHEST(1, 1),
	TRAINING_DUMMY(1, 2),
	SHRINE(1, 2),
	SACRIFICIAL_ALTAR(1, 2),
	LIBRARY(2, 2),
	PAPERMILL(4, 3),
	SIGN_POST(1, 1),
	GRAVE(1, 1);
	
	private final int realWidth;
	private final int realHeight;
	
	private BuildingDimensions(int width, int height) {
		this.realWidth = width;
		this.realHeight = height;
	}

	public int getRealWidth() {
		return realWidth;
	}

	public int getRealHeight() {
		return realHeight;
	}
	
	public int getPlacementWidth() {
		return realWidth + 1;
	}
	
	public int getPlacementHeight() {
		return realHeight + 1;
	}
	
	public void addWidthHeight(Map<ManagedProperty<?>, Object> properties) {
		properties.put(Constants.WIDTH, getRealWidth());
		properties.put(Constants.HEIGHT, getRealHeight());
	}
}
