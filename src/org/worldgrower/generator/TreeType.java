package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

public enum TreeType {
	NORMAL(ImageIds.SMALL_TREE, ImageIds.TREE),
	BOREAL(ImageIds.SMALL_BOREAL_TREE, ImageIds.BOREAL_TREE),
	PALM(ImageIds.SMALL_PALM_TREE, ImageIds.PALM_TREE);
	
	private final ImageIds smallImageIds;
	private final ImageIds normalImageIds;
	
	
	private TreeType(ImageIds smallImageIds, ImageIds normalImageIds) {
		this.smallImageIds = smallImageIds;
		this.normalImageIds = normalImageIds;
	}
	
	private ImageIds calculateTreeImageId(WorldObject tree) {
		if (!tree.getProperty(Constants.WOOD_SOURCE).hasEnoughWood()) {
			return smallImageIds;
		} else {
			return normalImageIds;
		}
	}
	
	public static ImageIds getTreeImageId(WorldObject tree) {
		TreeType treeType = tree.getProperty(Constants.TREE_TYPE);
		return treeType.calculateTreeImageId(tree);
	}
}
