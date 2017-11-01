package org.worldgrower.goal;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class CattlePropertyUtils {

	private static final int REPRODUCTION_MEAT_SOURCE_THRESHOLD = 5;
	
	public static List<WorldObject> getOwnedCattle(WorldObject performer, World world) {
		int id = performer.getProperty(Constants.ID);
		return world.findWorldObjectsByProperty(Constants.MEAT_SOURCE, w ->  w.getProperty(Constants.CATTLE_OWNER_ID) != null && w.getProperty(Constants.CATTLE_OWNER_ID).intValue() == id);
	}
	
	public static boolean isOwnedCattle(WorldObject performer, WorldObject w) {
		return w.getProperty(Constants.CATTLE_OWNER_ID) != null
				&& w.getProperty(Constants.CATTLE_OWNER_ID) == performer.getProperty(Constants.ID).intValue();
	}
	
	public static boolean isOwnedCattle(WorldObject w) {
		return w.getProperty(Constants.CATTLE_OWNER_ID) != null;
	}

	public static boolean isOldEnoughToReproduce(WorldObject w) {
		return w.getProperty(Constants.MEAT_SOURCE).intValue() > REPRODUCTION_MEAT_SOURCE_THRESHOLD;
	}
}
