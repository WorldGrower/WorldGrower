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

import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdToIntegerMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.gui.ImageIds;

public class BuildingGenerator {

	private static final String SHACK_NAME = "shack";
	private static final String HOUSE_NAME = "house";
	
	public static int generateVotingBox(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.VOTING_BOX);
		properties.put(Constants.NAME, "voting box");
		properties.put(Constants.TURN_COUNTER, 0);
		properties.put(Constants.CANDIDATES, new IdList());
		properties.put(Constants.VOTES, new IdToIntegerMap());
		WorldObject votingBox = new WorldObjectImpl(properties, new VotingBoxOnTurn());
		world.addWorldObject(votingBox);
		
		return id;
	}
	
	public static int generateShack(int x, int y, World world, double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.SLEEP_COMFORT, (int)(3 * skillBonus));
		properties.put(Constants.NAME, SHACK_NAME);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SHACK);
		properties.put(Constants.HIT_POINTS, 100);
		properties.put(Constants.HIT_POINTS_MAX, 100);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 10);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		
		WorldObject shack = new WorldObjectImpl(properties);
		world.addWorldObject(shack);
		
		return id;
	}
	
	public static int generateHouse(int x, int y, World world, double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.SLEEP_COMFORT, (int)(5 * skillBonus));
		properties.put(Constants.NAME, HOUSE_NAME);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.HOUSE);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, 200);
		properties.put(Constants.HIT_POINTS_MAX, 200);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		
		WorldObject house = new WorldObjectImpl(properties);
		world.addWorldObject(house);
		
		return id;
	}
	
	public static boolean isShack(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(SHACK_NAME);
	}
	
	public static boolean isHouse(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(HOUSE_NAME);
	}
	
	public static boolean isSellable(WorldObject worldObject) {
		return isShack(worldObject) || isHouse(worldObject);
	}
}