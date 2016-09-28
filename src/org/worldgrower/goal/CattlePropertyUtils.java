package org.worldgrower.goal;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class CattlePropertyUtils {

	public static List<WorldObject> getOwnedCattle(WorldObject performer, World world) {
		int id = performer.getProperty(Constants.ID);
		List<WorldObject> ownedCattle = world.findWorldObjectsByProperty(Constants.MEAT_SOURCE, w ->  w.getProperty(Constants.CATTLE_OWNER_ID) != null && w.getProperty(Constants.CATTLE_OWNER_ID).intValue() == id);
		return ownedCattle;
	}
}
