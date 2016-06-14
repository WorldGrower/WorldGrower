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
package org.worldgrower.goal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ImmutableWorldObject;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.InvestigateAction;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.IllusionOnTurn;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;

public class IllusionPropertyUtils {

	public static int createIllusion(WorldObject performer, int sourceId, World world, int x, int y, int width, int height) {
		WorldObject sourceWorldObject = mapIdToWorldObject(performer, sourceId, width, height, world);
		int id = world.generateUniqueId();
		
		int turnsToLive = (int)(20 * SkillUtils.getSkillBonus(performer, Constants.ILLUSION_SKILL));
		ImmutableWorldObject illusionWorldObject = new ImmutableWorldObject(sourceWorldObject, Arrays.asList(Constants.TURNS_TO_LIVE), new IllusionOnTurn());
		illusionWorldObject.setPropertyInternal(Constants.ID, id);
		illusionWorldObject.setPropertyInternal(Constants.ILLUSION_CREATOR_ID, performer.getProperty(Constants.ID));
		illusionWorldObject.setPropertyInternal(Constants.X, x);
		illusionWorldObject.setPropertyInternal(Constants.Y, y);
		illusionWorldObject.setPropertyInternal(Constants.PASSABLE, Boolean.TRUE);
		illusionWorldObject.setPropertyInternal(Constants.TURNS_TO_LIVE, turnsToLive);
		
		if (sourceWorldObject.hasProperty(Constants.INVENTORY)) {
			illusionWorldObject.setPropertyInternal(Constants.INVENTORY, new WorldObjectContainer());
		}
		
		world.addWorldObject(illusionWorldObject);
		
		everyoneInVicinityKnowsOfIllusion(performer, world, illusionWorldObject);
		
		return id;
	}

	static void everyoneInVicinityKnowsOfIllusion(WorldObject performer, World world, WorldObject illusionWorldObject) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(
				performer, 
				illusionWorldObject, 
				w -> w.equals(performer) || !InvestigateAction.illusionIsBelievedBy(w, illusionWorldObject, world), 
				Constants.ILLUSION_CREATOR_ID, 
				performer.getProperty(Constants.ID), 
				world);
	}
	
	private static WorldObject mapIdToWorldObject(WorldObject performer, int id, int width, int height, World world) {
		if (world.exists(id)) {
			return world.findWorldObject(Constants.ID, id);
		} else {
			List<WorldObject> illusionSources = getIllusionSources(performer, width, height, world);
			for(WorldObject illusionSource : illusionSources) {
				if (illusionSource.getProperty(Constants.ID).intValue() == id) {
					return illusionSource;
				}
			}
			throw new IllegalStateException("Id " + id + " not found in illusionSources " + illusionSources);
		}
	}
	
	public static List<WorldObject> getIllusionSources(WorldObject performer, int width, int height, World world) {
		List<WorldObject> illusionSources = new ArrayList<>();
		addExistingWorldObjects(width, height, world, illusionSources);
		addPlants(width, height, world, illusionSources);
		addCreatures(width, height, world, illusionSources);
		addBuildings(width, height, illusionSources, performer);
		addTerrainResources(width, height, world, illusionSources);
		return illusionSources;
	}

	private static void addTerrainResources(int width, int height, World world, List<WorldObject> illusionSources) {
		List<WorldObject> terrainResources = TerrainGenerator.getTerrainResources(width, height, world);
		int terrainResourceId = -3000;
		addToIllusionSources(illusionSources, terrainResources, terrainResourceId);
		
	}

	private static void addToIllusionSources(List<WorldObject> illusionSources, List<WorldObject> possibleIllusionSources, int startId) {
		for(WorldObject terrainResource : possibleIllusionSources) {
			if (!isInList(terrainResource, illusionSources)) {
				terrainResource.setProperty(Constants.ID, startId--);
				illusionSources.add(terrainResource);
			}
		}
	}

	private static void addPlants(int width, int height, World world, List<WorldObject> illusionSources) {
		List<WorldObject> plants = PlantGenerator.getPlants(width, height, world);
		int plantId = -1000;
		addToIllusionSources(illusionSources, plants, plantId);
	}
	
	private static void addCreatures(int width, int height, World world, List<WorldObject> illusionSources) {
		CreatureGenerator creatureGenerator = new CreatureGenerator(GroupPropertyUtils.getVerminOrganization(world));
		List<WorldObject> creatures = creatureGenerator.getCreatures(width, height);
		int creatureId = -1;
		addToIllusionSources(illusionSources, creatures, creatureId);
	}
	
	private static void addBuildings(int width, int height, List<WorldObject> illusionSources, WorldObject performer) {
		List<WorldObject> buildings = BuildingGenerator.getBuildings(performer, width, height);
		int buildingId = -2000;
		addToIllusionSources(illusionSources, buildings, buildingId);
	}

	private static void addExistingWorldObjects(int width, int height, World world, List<WorldObject> illusionSources) {
		List<WorldObject> realWorldObjects = world.findWorldObjects(w -> w.getProperty(Constants.WIDTH) == width && w.getProperty(Constants.HEIGHT) == height);
		for(WorldObject realWorldObject : realWorldObjects) {
			if (!isInList(realWorldObject, illusionSources)) {
				illusionSources.add(realWorldObject);
			}
		}
	}

	private static boolean isInList(WorldObject creature, List<WorldObject> worldObjects) {
		for(WorldObject worldObject : worldObjects) {
			if (worldObject.getProperty(Constants.IMAGE_ID) == creature.getProperty(Constants.IMAGE_ID)) {
				return true;
			}
		}
		return false;
	}
}
