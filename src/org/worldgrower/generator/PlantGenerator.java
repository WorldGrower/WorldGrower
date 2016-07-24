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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.DoNothingOnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.terrain.TerrainType;

public class PlantGenerator {

	private final WorldObject organization;
	
	public PlantGenerator(WorldObject organization) {
		this.organization = organization;
	}
	
	public static int generateBerryBush(int x, int y, World world) {
		int id = world.generateUniqueId();
		WorldObject berryBush = generateBerryBush(x, y, id);
		world.addWorldObject(berryBush);
		
		return id;
	}

	private static WorldObject generateBerryBush(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.BUSH);
		properties.put(Constants.FOOD_SOURCE, 1);
		properties.put(Constants.FOOD_PRODUCED, 1);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.NAME, "berry bush");
		WorldObject berryBush = new WorldObjectImpl(properties, new BerryBushOnTurn());
		return berryBush;
	}
	
	public static int generateTreeTrunk(int x, int y, World world) {
		int id = world.generateUniqueId();
		WorldObject treeStump = generateTreeTrunk(x, y, id);
		world.addWorldObject(treeStump);
		
		return id;
	}

	private static WorldObject generateTreeTrunk(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.WOOD_SOURCE, 30);
		properties.put(Constants.WOOD_PRODUCED, 400);
		properties.put(Constants.IMAGE_ID, ImageIds.TRUNK);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.NAME, "tree trunk");
		WorldObject treeStump = new WorldObjectImpl(properties);
		return treeStump;
	}
	
	public static int generateGrapeVine(int x, int y, World world) {
		int id = world.generateUniqueId();
		WorldObject berryBush = generateGrapeVine(x, y, id);
		world.addWorldObject(berryBush);
		
		return id;
	}

	private static WorldObject generateGrapeVine(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.GRAPE_VINE);
		properties.put(Constants.GRAPE_SOURCE, 1);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.NAME, "grape vine");
		WorldObject berryBush = new WorldObjectImpl(properties, new GrapeVineOnTurn());
		return berryBush;
	}
	
	public static int generateNightShade(int x, int y, World world) {
		int id = world.generateUniqueId();
		WorldObject nightshadePlant = generateNightShade(x, y, id);
		world.addWorldObject(nightshadePlant);
		
		return id;
	}

	private static WorldObject generateNightShade(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.NIGHT_SHADE_PLANT);
		properties.put(Constants.NIGHT_SHADE_SOURCE, 1);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.NAME, "nightshade");
		WorldObject nightshadePlant = new WorldObjectImpl(properties, new NightShadeOnTurn());
		return nightshadePlant;
	}
	
	public static int generateTree(int x, int y, World world) {
		return generateTree(x, y, world, 1f);
	}
	
	public static int generateTree(int x, int y, World world, double skillBonus) {
		int id = world.generateUniqueId();
		final ImageIds imageId = getTreeImageId(x, y, world);
		WorldObject tree = generateTree(x, y, id, imageId, skillBonus);
		tree.setProperty(Constants.IMAGE_ID, TreeImageCalculator.getTreeImageId(tree, world));
		world.addWorldObject(tree);
		
		return id;
	}

	private static WorldObject generateTree(int x, int y, int id, final ImageIds imageId, double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, imageId);
		properties.put(Constants.NAME, "tree");
		int woodSource = Constants.WOOD_SOURCE.normalize((int)(50 * skillBonus));
		properties.put(Constants.WOOD_SOURCE, woodSource);
		properties.put(Constants.WOOD_PRODUCED, woodSource);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		WorldObject tree = new WorldObjectImpl(properties, new TreeOnTurn());
		return tree;
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
	
	public int generateDemonTree(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.NAME, "DemonTree");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.TREE);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.CREATURE_TYPE, CreatureType.PLANT_CREATURE_TYPE);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		
		properties.put(Constants.ARMOR, 10);
		
		properties.put(Constants.STRENGTH, 6);
		properties.put(Constants.DEXTERITY, 12);
		properties.put(Constants.CONSTITUTION, 8);
		properties.put(Constants.INTELLIGENCE, 6);
		properties.put(Constants.WISDOM, 12);
		properties.put(Constants.CHARISMA, 6);
		
		properties.put(Constants.DAMAGE, 6 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject demonTree = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new DoNothingOnTurn(), new DemonTreeWorldEvaluationFunction());
		world.addWorldObject(demonTree);
		
		return id;
	}
	
	public static int generateCottonPlant(int x, int y, World world) {
		
		int id = world.generateUniqueId();
		WorldObject cottonPlant = generateCottonPlant(x, y, id);
		world.addWorldObject(cottonPlant);
		
		return id;
	}

	private static WorldObject generateCottonPlant(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_PLANT);
		properties.put(Constants.COTTON_SOURCE, 1);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.NAME, "cotton plant");
		WorldObject cottonPlant = new WorldObjectImpl(properties, new CottonPlantOnTurn());
		return cottonPlant;
	}

	public static List<WorldObject> getPlants(int width, int height, World world) {
		List<WorldObject> plants = new ArrayList<>();
		plants.add(generateBerryBush(0, 0, 0));
		plants.add(generateTreeTrunk(0, 0, 0));
		plants.add(generateGrapeVine(0, 0, 0));
		plants.add(generateNightShade(0, 0, 0));
		plants.add(generateTree(0, 0, 0, ImageIds.TREE, 1f));
		plants.add(generateTree(0, 0, 0, ImageIds.BOREAL_TREE, 1f));
		plants.add(generateCottonPlant(0, 0, 0));
		
		plants = plants.stream().filter(w -> w.getProperty(Constants.WIDTH) == width && w.getProperty(Constants.HEIGHT) == height).collect(Collectors.toList());
		
		return plants;
	}
}
