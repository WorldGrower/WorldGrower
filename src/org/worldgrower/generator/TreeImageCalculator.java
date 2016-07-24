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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.terrain.TerrainType;

public class TreeImageCalculator {

	public static ImageIds getTreeImageId(WorldObject tree, World world) {
		ImageIds treeImageId = getTreeImageId(tree.getProperty(Constants.X), tree.getProperty(Constants.Y), world);
		int woodSource = tree.getProperty(Constants.WOOD_SOURCE);
		if (woodSource < 10) {
			if (treeImageId == ImageIds.TREE) {
				treeImageId = ImageIds.SMALL_TREE;
			} else if (treeImageId == ImageIds.BOREAL_TREE) {
				treeImageId = ImageIds.SMALL_BOREAL_TREE;
			}
		}
		return treeImageId;
	}
	
	private static ImageIds getTreeImageId(int x, int y, World world) {
		final ImageIds imageId;
		TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
		if (terrainType == TerrainType.HILL || terrainType == TerrainType.MOUNTAIN) {
			imageId = ImageIds.BOREAL_TREE;
		} else {
			imageId = ImageIds.TREE;
		}
		return imageId;
	}
}
