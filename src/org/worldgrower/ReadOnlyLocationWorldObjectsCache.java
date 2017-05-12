package org.worldgrower;

import java.util.List;

public final class ReadOnlyLocationWorldObjectsCache {

	private final LocationWorldObjectsCache locationWorldObjectsCache;
	
	public ReadOnlyLocationWorldObjectsCache(World world) {
		this.locationWorldObjectsCache = (LocationWorldObjectsCache) world.getWorldObjectsCache(Constants.X, Constants.Y);
	}

	public List<WorldObject> getworldObjects(int x, int y, World world) {
		return locationWorldObjectsCache.getWorldObjectsFor(x, y);
	}
}
