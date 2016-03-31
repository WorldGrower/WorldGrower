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
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.gui.ImageIds;

public class TerrainGenerator {

	public static int generateStoneResource(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.STONE_RESOURCE);
		properties.put(Constants.STONE_SOURCE, 9000);
		properties.put(Constants.SOUL_GEM_SOURCE, 1000);
		properties.put(Constants.NAME, "stone resource");
		WorldObject tree = new WorldObjectImpl(properties);
		world.addWorldObject(tree);
		
		return id;
	}
	
	public static int generateOreResource(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.ORE_RESOURCE);
		properties.put(Constants.ORE_SOURCE, 9000);
		properties.put(Constants.NAME, "ore resource");
		WorldObject tree = new WorldObjectImpl(properties);
		world.addWorldObject(tree);
		
		return id;
	}
	
	public static int generateGoldResource(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.GOLD_RESOURCE);
		properties.put(Constants.GOLD_SOURCE, 9000);
		properties.put(Constants.NAME, "gold resource");
		WorldObject tree = new WorldObjectImpl(properties);
		world.addWorldObject(tree);
		
		return id;
	}
	
	public static int generateOilResource(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.OIL_RESOURCE);
		properties.put(Constants.OIL_SOURCE, 1000);
		properties.put(Constants.NAME, "oil resource");
		WorldObject oilResource = new WorldObjectImpl(properties);
		world.addWorldObject(oilResource);
		
		return id;
	}
	
	private static final int FIRE_TRAP_BASE_DAMAGE = 2 * Item.COMBAT_MULTIPLIER;
	
	public static int generateFireTrap(int x, int y, World world, double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.DAMAGE, (int)(FIRE_TRAP_BASE_DAMAGE * skillBonus));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.FIRE_TRAP);
		properties.put(Constants.PASSABLE, Boolean.TRUE);
		properties.put(Constants.NAME, "fire trap");
		WorldObject tree = new WorldObjectImpl(properties, new FireTrapOnTurn());
		world.addWorldObject(tree);
		
		return id;
	}
	
	public static int getFireTrapBaseDamage() {
		return FIRE_TRAP_BASE_DAMAGE;
	}
}
