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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdToIntegerMap;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.gui.ImageIds;

public class BuildingGenerator {

	private static final String WELL_NAME = "well";
	private static final String TRAINING_DUMMY_NAME = "Training dummy";
	private static final String GRAVE_NAME = "grave";
	private static final String JAIL_LEFT = "Jail left";
	private static final String JAIL_DOOR = "Jail door";
	
	public static int generateVotingBox(int x, int y, World world) {		
		int id = world.generateUniqueId();
		WorldObject votingBox = generateVotingBox(x, y, id);
		world.addWorldObject(votingBox);
		
		return id;
	}

	private static WorldObject generateVotingBox(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
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
		return votingBox;
	}
	
	public static int generateShack(int x, int y, World world, double skillBonus, WorldObject owner) {		
		int id = world.generateUniqueId();
		WorldObject shack = generateShack(x, y, skillBonus, owner, id);
		world.addWorldObject(shack);
		
		return id;
	}

	public static WorldObject generateShack(int x, int y, double skillBonus, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.SLEEP_COMFORT, (int)(3 * skillBonus));
		properties.put(Constants.NAME, createName("shack", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SHACK);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 10);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.LOCK_STRENGTH, 0);
		properties.put(Constants.LOCKED, Boolean.FALSE);
		properties.put(Constants.BUILDING_TYPE, BuildingType.SHACK);
		
		WorldObject shack = new WorldObjectImpl(properties);
		return shack;
	}
	
	public static int generateHouse(int x, int y, World world, double skillBonus, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject house = generateHouse(x, y, skillBonus, owner, id);
		world.addWorldObject(house);
		
		return id;
	}

	private static WorldObject generateHouse(int x, int y, double skillBonus, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 3);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.SLEEP_COMFORT, (int)(5 * skillBonus));
		properties.put(Constants.NAME, createName("house", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, generateHouseImageIds());
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.LOCK_STRENGTH, 2);
		properties.put(Constants.LOCKED, Boolean.TRUE);
		properties.put(Constants.BUILDING_TYPE, BuildingType.HOUSE);
		
		WorldObject house = new WorldObjectImpl(properties);
		return house;
	}
	
	public static String createName(BuildingType buildingType, WorldObject owner) {
		return createName(buildingType.getDescription(), owner);
	}
	
	public static String createName(String baseName, WorldObject owner) {
		return owner.getProperty(Constants.NAME) + "'s " + baseName;
	}
	
	public static int buildWell(int x, int y, World world, double skillBonus) {
		int id = world.generateUniqueId();
		WorldObject well = generateWell(x, y, id);
		world.addWorldObject(well);
		
		return id;
	}

	private static WorldObject generateWell(int x, int y, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.WATER_SOURCE, 2000);
		properties.put(Constants.NAME, WELL_NAME);
		
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.WELL);
		properties.put(Constants.HIT_POINTS, 75 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 75 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject well = new WorldObjectImpl(properties, new WellOnTurn());
		return well;
	}
	
	private static ImageIds generateHouseImageIds() {
		return ImageIds.HOUSE6;
	}

	public static int generateTrainingDummy(int x, int y, World world, double skillBonus) {
		int id = world.generateUniqueId();		
		WorldObject trainingDummy = generateTrainingDummy(x, y, skillBonus, id);
		world.addWorldObject(trainingDummy);
		
		return id;
	}

	private static WorldObject generateTrainingDummy(int x, int y, double skillBonus, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int hitPoints = (int)(50 * skillBonus);
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.NAME, TRAINING_DUMMY_NAME);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.TRAINING_DUMMY);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, hitPoints * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, hitPoints * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject trainingDummy = new WorldObjectImpl(properties);
		return trainingDummy;
	}	
	
	public static int generateGrave(int x, int y, World world, WorldObject deceasedWorldObject) {
		int id = world.generateUniqueId();
		WorldObject grave = generateGrave(x, y, deceasedWorldObject, id);
		world.addWorldObject(grave);
		
		return id;
	}

	private static WorldObject generateGrave(int x, int y, WorldObject deceasedWorldObject, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y); 
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, GRAVE_NAME);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.GRAVE);
		properties.put(Constants.TEXT, "Here are the " + deceasedWorldObject.getProperty(Constants.NAME) + " buried");
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject grave = new WorldObjectImpl(properties);
		return grave;
	}
	
	public static int generateShrine(int x, int y, World world, WorldObject performer) {		
		int id = world.generateUniqueId();
		Deity deity = performer.getProperty(Constants.DEITY);
		WorldObject shrine = generateShrine(x, y, id, deity);
		world.addWorldObject(shrine);
		
		return id;
	}

	private static WorldObject generateShrine(int x, int y, int id, Deity deity) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		
		properties.put(Constants.DEITY, deity);
		properties.put(Constants.NAME, "shrine to " + deity.getName());
		properties.put(Constants.TEXT, "shrine to " + deity.getName());
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, deity.getStatueImageId());
		properties.put(Constants.CAN_BE_WORSHIPPED, Boolean.TRUE);
		properties.put(Constants.HIT_POINTS, 150 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 150 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject shrine = new WorldObjectImpl(properties);
		return shrine;
	}
	
	public static boolean isShack(WorldObject worldObject) {
		return worldObject.getProperty(Constants.BUILDING_TYPE) == BuildingType.SHACK;
	}
	
	public static boolean isHouse(WorldObject worldObject) {
		return worldObject.getProperty(Constants.BUILDING_TYPE) == BuildingType.HOUSE;
	}
	
	public static boolean isGrave(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(GRAVE_NAME);
	}
	
	public static boolean isSellable(WorldObject worldObject) {
		return isShack(worldObject) || isHouse(worldObject);
	}
	
	public static boolean isTrainingDummy(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(TRAINING_DUMMY_NAME);
	}
	
	public static boolean isJailLeft(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(JAIL_LEFT);
	}
	
	public static boolean isJailDoor(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(JAIL_DOOR);
	}
	
	public static boolean isWell(WorldObject worldObject) {
		return worldObject.getProperty(Constants.NAME).equals(WELL_NAME);
	} 

	public static void generateJail(int x, int y, World world, double useSkill) {
		createJailLeft(x, y, world);
		createJailUp(x, y, world);
		createJailRight(x, y, world);
		createJailDoor(x, y, world);
	}

	private static void createJailLeft(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, JAIL_LEFT);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.JAIL_LEFT);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject jailLeft = new WorldObjectImpl(properties);
		world.addWorldObject(jailLeft);
	}
	
	private static void createJailUp(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		
		properties.put(Constants.X, x+1);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.NAME, "Jail up");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.JAIL_UP);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject jailLeft = new WorldObjectImpl(properties);
		world.addWorldObject(jailLeft);
	}
	
	private static void createJailRight(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		
		properties.put(Constants.X, x+2);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, "Jail right");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.JAIL_RIGHT);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject jailLeft = new WorldObjectImpl(properties);
		world.addWorldObject(jailLeft);
	}
	
	private static void createJailDoor(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		
		properties.put(Constants.X, x+1);
		properties.put(Constants.Y, y+2);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.NAME, JAIL_DOOR);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.JAIL_DOOR);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject jailLeft = new WorldObjectImpl(properties);
		world.addWorldObject(jailLeft);
	}
	
	public static void addJailDoorIfNotPresent(WorldObject jailLeft, World world) {
		int jailLeftX = jailLeft.getProperty(Constants.X);
		int jailLeftY = jailLeft.getProperty(Constants.Y);
		List<WorldObject> jailDoors = world.findWorldObjects(w -> isJailDoor(w) && w.getProperty(Constants.X) == jailLeftX+1 && w.getProperty(Constants.Y) == jailLeftY + 2);
		
		if (jailDoors.size() == 0) {
			createJailDoor(jailLeftX, jailLeftY, world);
		}
	}

	public static WorldObject findEmptyJail(World world) {
		List<WorldObject> jails = getJails(world);
		for(WorldObject jail : jails) {
			int jailX = jail.getProperty(Constants.X);
			int jailY = jail.getProperty(Constants.Y);
			List<WorldObject> prisoners = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isInJail(jailX, jailY, w));
			if (prisoners.size() == 0) {
				return jail;
			}
		}
		return null;
	}

	private static boolean isInJail(int jailX, int jailY, WorldObject worldObject) {
		return worldObject.getProperty(Constants.X) == jailX+1 && worldObject.getProperty(Constants.Y) == jailY+1;
	}
	
	public static boolean isPrisonerInJail(WorldObject worldObject, World world) {
		List<WorldObject> jails = getJails(world);
		for(WorldObject jail : jails) {
			int jailX = jail.getProperty(Constants.X);
			int jailY = jail.getProperty(Constants.Y);
			if (isInJail(jailX, jailY, worldObject)) {
				return true;
			}
		}
		return false;
	}

	private static List<WorldObject> getJails(World world) {
		return world.getWorldObjectsCache().getWorldObjectsFor(0, 0);
	}

	public static int generateSacrificialAltar(int x, int y, World world, WorldObject performer, Deity deity, double useSkill) {
		int id = world.generateUniqueId();
		int sacrificialAltarCreatorId = performer.getProperty(Constants.ID);		
		WorldObject altar = generateSacrificalAltar(x, y, deity, id, sacrificialAltarCreatorId);
		world.addWorldObject(altar);
		
		return id;
	}

	private static WorldObject generateSacrificalAltar(int x, int y, Deity deity, int id, int sacrificialAltarCreatorId) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.NAME, "sacrificial Altar");
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SACRIFIAL_ALTAR);
		
		properties.put(Constants.SACRIFICIAL_ALTAR_CREATOR_ID, sacrificialAltarCreatorId);
		properties.put(Constants.TEXT, "Sacrificial Altar to " + deity.getName());
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject altar = new WorldObjectImpl(properties);
		return altar;
	}
	
	public static IdList generateArena(int x, int y, World world, double useSkill) {
		IdList idList = new IdList();
		idList.add(createArenaVertical(x, y, world));
		idList.add(createArenaHorizontal(x+1, y, world));
		idList.add(createArenaHorizontal(x+6, y, world));
		idList.add(createArenaVertical(x+10, y, world));
		idList.add(createArenaHorizontal(x+1, y+7, world));
		idList.add(createArenaHorizontal(x+6, y+7, world));
		
		return idList;
	}
	
	private static int createArenaVertical(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 8);
		properties.put(Constants.NAME, "Arena vertical");
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.ARENA_VERTICAL);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject arenaVertical = new WorldObjectImpl(properties);
		world.addWorldObject(arenaVertical);
		
		return id;
	}
	
	private static int createArenaHorizontal(int x, int y, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, "Arena horizontal");
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.ARENA_HORIZONTAL);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject arenaHorizontal = new WorldObjectImpl(properties);
		world.addWorldObject(arenaHorizontal);
		
		return id;
	}
	
	public static int generateLibrary(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject library = generateLibrary(x, y, owner, id);
		world.addWorldObject(library);
		return id;
	}

	private static WorldObject generateLibrary(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.LIBRARY_QUALITY, 1);
		properties.put(Constants.NAME, createName("library", owner));
		
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.LIBRARY);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.LOCK_STRENGTH, 2);
		properties.put(Constants.LOCKED, Boolean.TRUE);
		
		WorldObject library = new WorldObjectImpl(properties);
		return library;
	}
	
	public static int generatePaperMill(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject paperMill = generatePaperMill(x, y, owner, id);
		world.addWorldObject(paperMill);
		
		return id;
	}

	private static WorldObject generatePaperMill(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.PAPER_MILL_QUALITY, 5);
		properties.put(Constants.NAME, createName("papermill", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.PAPER_MILL);
		properties.put(Constants.HIT_POINTS, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 50 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.PAPERMILL);
		
		WorldObject paperMill = new WorldObjectImpl(properties);
		return paperMill;
	}
	
	public static int generateSmith(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject smith = generateSmith(x, y, owner, id);
		world.addWorldObject(smith);
	
		return id;
	}

	private static WorldObject generateSmith(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 2);
		properties.put(Constants.SMITH_QUALITY, 5);
		properties.put(Constants.NAME, createName("smithy", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SMITH);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.SMITH);
		
		WorldObject smith = new WorldObjectImpl(properties);
		return smith;
	}
	
	public static int generateWorkbench(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject workbench = generateWorkBench(x, y, owner, id);
		world.addWorldObject(workbench);
	
		return id;
	}

	private static WorldObject generateWorkBench(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.WORKBENCH_QUALITY, 5);
		properties.put(Constants.NAME, createName("workbench", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.WORKBENCH);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.WORKBENCH);
		
		WorldObject workbench = new WorldObjectImpl(properties);
		return workbench;
	}

	public static int generateInn(int x, int y, World world, double skillBonus, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject house = generateInn(x, y, skillBonus, owner, id);
		world.addWorldObject(house);
		
		return id;
	}

	private static WorldObject generateInn(int x, int y, double skillBonus, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 6);
		properties.put(Constants.HEIGHT, 8);
		properties.put(Constants.SLEEP_COMFORT, (int)(5 * skillBonus));
		properties.put(Constants.NAME, createName("Inn", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.INN);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 200);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.LOCK_STRENGTH, 2);
		properties.put(Constants.LOCKED, Boolean.TRUE);
		properties.put(Constants.BUILDING_TYPE, BuildingType.INN);
		
		WorldObject house = new WorldObjectImpl(properties);
		return house;
	}
	
	public static int generateSignPost(int x, int y, World world, String text) {
		int id = world.generateUniqueId();
		WorldObject signPost = generateSignPost(x, y, text, id);
		world.addWorldObject(signPost);
		
		return id;
	}

	private static WorldObject generateSignPost(int x, int y, String text, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, "sign post");
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SIGN_POST);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.TEXT, text);
		properties.put(Constants.HIT_POINTS, 20 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 20 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject signPost = new WorldObjectImpl(properties);
		return signPost;
	}
	
	public static int generateWeavery(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject weavery = generateWeavery(x, y, owner, id);
		world.addWorldObject(weavery);
	
		return id;
	}

	private static WorldObject generateWeavery(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.WEAVERY_QUALITY, 5);
		properties.put(Constants.NAME, createName("weavery", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.WEAVERY);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.WEAVERY);
		
		WorldObject weavery = new WorldObjectImpl(properties);
		return weavery;
	}
	
	public static int generateBrewery(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject brewery = generateBrewery(x, y, owner, id);
		world.addWorldObject(brewery);
	
		return id;
	}

	private static WorldObject generateBrewery(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.BREWERY_QUALITY, 5);
		properties.put(Constants.NAME, createName("brewery", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.BREWERY);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.BREWERY);
		
		WorldObject brewery = new WorldObjectImpl(properties);
		return brewery;
	}

	public static int generateApothecary(int x, int y, World world, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject brewery = generateApothecary(x, y, owner, id);
		world.addWorldObject(brewery);
	
		return id;
	}

	private static WorldObject generateApothecary(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 3);
		properties.put(Constants.APOTHECARY_QUALITY, 5);
		properties.put(Constants.NAME, createName("apothecary", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.APOTHECARY);
		properties.put(Constants.HIT_POINTS, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 100 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.BUILDING_TYPE, BuildingType.APOTHECARY);
		
		WorldObject brewery = new WorldObjectImpl(properties);
		return brewery;
	}
	
	public static int generateChest(int x, int y, World world, double skillBonus, WorldObject owner) {
		int id = world.generateUniqueId();
		WorldObject house = generateChest(x, y, owner, id);
		world.addWorldObject(house);
		
		return id;
	}

	private static WorldObject generateChest(int x, int y, WorldObject owner, int id) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, createName("chest", owner));
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.CHEST);
		properties.put(Constants.FLAMMABLE, Boolean.TRUE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.HIT_POINTS, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 200 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.LOCK_STRENGTH, 2);
		properties.put(Constants.LOCKED, Boolean.FALSE);
		properties.put(Constants.BUILDING_TYPE, BuildingType.CHEST);
		
		WorldObject house = new WorldObjectImpl(properties);
		return house;
	}

	
	public static boolean isBrewery(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.BREWERY_QUALITY);
	}
	
	public static Integer getSmithId(WorldObject performer) {
		List<Integer> smithIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.SMITH);
		if (smithIds.size() > 0) {
			return smithIds.get(0);
		} else {
			return null;
		}
	}
	
	public static Integer getBreweryId(WorldObject performer) {
		List<Integer> breweryIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.BREWERY);
		if (breweryIds.size() > 0) {
			return breweryIds.get(0);
		} else {
			return null;
		}
	}
	
	public static Integer getWorkbenchId(WorldObject performer) {
		List<Integer> workbenchIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.WORKBENCH);
		if (workbenchIds.size() > 0) {
			return workbenchIds.get(0);
		} else {
			return null;
		}
	}
	
	public static Integer getPapermillId(WorldObject performer) {
		List<Integer> papermillIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.PAPERMILL);
		if (papermillIds.size() > 0) {
			return papermillIds.get(0);
		} else {
			return null;
		}
	}
	
	public static Integer getWeaveryId(WorldObject performer) {
		List<Integer> weaveryIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.WEAVERY);
		if (weaveryIds.size() > 0) {
			return weaveryIds.get(0);
		} else {
			return null;
		}
	}
	
	public static Integer getApothecaryId(WorldObject performer) {
		List<Integer> apothecaryIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.APOTHECARY);
		if (apothecaryIds.size() > 0) {
			return apothecaryIds.get(0);
		} else {
			return null;
		}
	}
	
	public static List<WorldObject> findUnownedBuildingsForClaiming(WorldObject performer, IntProperty property, Function<WorldObject, Boolean> testFunction, World world) {
		return GoalUtils.findNearestTargetsByProperty(
				performer, 
				Actions.CLAIM_BUILDING_ACTION, 
				property, 
				w -> testFunction.apply(w) && Actions.CLAIM_BUILDING_ACTION.distance(performer, w, Args.EMPTY, world) == 0, 
				world);
	}

	public static boolean isSmithy(WorldObject w) {
		return w.hasProperty(Constants.SMITH_QUALITY);
	}

	public static boolean isWorkbench(WorldObject w) {
		return w.hasProperty(Constants.WORKBENCH_QUALITY);
	}

	public static boolean isPapermill(WorldObject w) {
		return w.hasProperty(Constants.PAPER_MILL_QUALITY);
	}

	public static boolean isWeavery(WorldObject w) {
		return w.hasProperty(Constants.WEAVERY_QUALITY);
	}
	
	public static boolean isApothecary(WorldObject w) {
		return w.hasProperty(Constants.APOTHECARY_QUALITY);
	}
	
	public static boolean isNormalChest(WorldObject w) {
		return isChest(w) && !w.hasProperty(Constants.SECRET_CHEST);
	}

	public static boolean isSecretChest(WorldObject w) {
		return isChest(w) && w.hasProperty(Constants.SECRET_CHEST);
	}
	
	private static boolean isChest(WorldObject w) {
		return (w.hasProperty(Constants.BUILDING_TYPE)) && w.getProperty(Constants.BUILDING_TYPE) == BuildingType.CHEST;
	}
	
	public static List<WorldObject> getBuildings(WorldObject owner, int width, int height) {
		List<WorldObject> buildings = new ArrayList<>();
		buildings.add(generateVotingBox(0, 0, 0));
		buildings.add(generateWell(0, 0, 0));
		buildings.add(generateTrainingDummy(0, 0, 1f, 0));
		for(Deity deity : Deity.ALL_DEITIES) {
			buildings.add(generateShrine(0, 0, 0, deity));
			buildings.add(generateSacrificalAltar(0, 0, deity, 0, 0));
		}
		buildings.add(generateChest(0, 0, owner, 0));
		buildings.add(generateLibrary(0, 0, owner, 0));
		buildings.add(generatePaperMill(0, 0, owner, 0));
		buildings.add(generateSmith(0, 0, owner, 0));
		buildings.add(generateWorkBench(0, 0, owner, 0));
		buildings.add(generateInn(0, 0, 1f, owner, 0));
		buildings.add(generateWeavery(0, 0, owner, 0));
		buildings.add(generateBrewery(0, 0, owner, 0));
		buildings.add(generateApothecary(0, 0, owner, 0));
		
		buildings = buildings.stream().filter(w -> w.getProperty(Constants.WIDTH) <= width && w.getProperty(Constants.HEIGHT) == height).collect(Collectors.toList());
		
		return buildings;
	}
}