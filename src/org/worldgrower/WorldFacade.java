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
package org.worldgrower;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.History;
import org.worldgrower.history.Turn;
import org.worldgrower.terrain.Terrain;

/**
 * A WorldFacade is a filter over the original World.
 * Illusions that aren't believed are filtered out from the World. 
 */
public class WorldFacade implements World {

	private final WorldObject personViewingWorld;
	private final World world;
	
	public WorldFacade(WorldObject personViewingWorld, World world) {
		this.personViewingWorld = personViewingWorld;
		this.world = world;
	}
	
	@Override
	public void addWorldObject(WorldObject worldObject) {
		throw new IllegalStateException("WorldFacade is read-only, cannot add " + worldObject);

	}

	@Override
	public void removeWorldObject(WorldObject worldObject) {
		throw new IllegalStateException("WorldFacade is read-only, cannot add " + worldObject);
	}

	@Override
	public List<WorldObject> getWorldObjects() {
		List<WorldObject> worldObjects = new ArrayList<>(world.getWorldObjects());
		
		filter(worldObjects);
		
		return worldObjects;
	}

	private void filter(List<WorldObject> worldObjects) {
		Iterator<WorldObject> worldObjectIterator = worldObjects.iterator();
		while(worldObjectIterator.hasNext()) {
			WorldObject worldObject = worldObjectIterator.next();
			if (worldObject.hasProperty(Constants.ILLUSION_CREATOR_ID)) {
				if (!illusionIsBelievedBy(personViewingWorld, worldObject, world)) {
					worldObjectIterator.remove();
				}
			}
			if (isInvisible(worldObject)) {
				worldObjectIterator.remove();
			}
			if (isMaskedByIllusion(worldObject, world)) {
				worldObjectIterator.remove();
			}
		}
	}

	public boolean isMaskedByIllusion(WorldObject worldObject, World world) {
		LocationWorldObjectsCache locationWorldObjectsCache = (LocationWorldObjectsCache) world.getWorldObjectsCache(Constants.X, Constants.Y);
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		int width = worldObject.getProperty(Constants.WIDTH);
		int height = worldObject.getProperty(Constants.HEIGHT);
		if (x >= 0 && y >= 0) {
			List<WorldObject> worldObjectsOnLocation = locationWorldObjectsCache.getWorldObjectsFor(x, y);
			for(WorldObject worldObjectOnLocation : worldObjectsOnLocation) {
				if (!worldObject.equals(worldObjectOnLocation) 
					&& worldObjectOnLocation.hasProperty(Constants.ILLUSION_CREATOR_ID)
					&& illusionIsBelievedBy(personViewingWorld, worldObjectOnLocation, world)
					&& worldObjectOnLocation.getProperty(Constants.WIDTH) == width
					&& worldObjectOnLocation.getProperty(Constants.HEIGHT) == height) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public WorldObject getWorldObjectMaskedByIllusion(WorldObject worldObject, World world) {
		if (worldObject.hasProperty(Constants.ILLUSION_CREATOR_ID) && illusionIsBelievedBy(personViewingWorld, worldObject, world)) {
			LocationWorldObjectsCache locationWorldObjectsCache = (LocationWorldObjectsCache) world.getWorldObjectsCache(Constants.X, Constants.Y);
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			int width = worldObject.getProperty(Constants.WIDTH);
			int height = worldObject.getProperty(Constants.HEIGHT);
			if (x >= 0 && y >= 0) {
				List<WorldObject> worldObjectsOnLocation = locationWorldObjectsCache.getWorldObjectsFor(x, y);
				for(WorldObject worldObjectOnLocation : worldObjectsOnLocation) {
					if (!worldObject.equals(worldObjectOnLocation) 
						&& !worldObjectOnLocation.hasProperty(Constants.ILLUSION_CREATOR_ID)
						&& worldObjectOnLocation.getProperty(Constants.WIDTH) == width
						&& worldObjectOnLocation.getProperty(Constants.HEIGHT) == height) {
						
						return worldObjectOnLocation;
					}
				}
			}
		}
		
		return null;
	}

	private static boolean illusionIsBelievedBy(WorldObject personViewingWorld, WorldObject worldObject, World world2) {
		if (personViewingWorld.hasProperty(Constants.KNOWLEDGE_MAP)) {
			return !personViewingWorld.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(worldObject, Constants.ILLUSION_CREATOR_ID);
		} else {
			return true;
		}
	}

	boolean isInvisible(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.CONDITIONS) && worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION);
	}

	@Override
	public List<WorldObject> findWorldObjects(WorldObjectCondition worldObjectCondition) {
		List<WorldObject> worldObjects = world.findWorldObjects(worldObjectCondition);
		filter(worldObjects);
		return worldObjects;
	}
	
	@Override
	public List<WorldObject> findWorldObjectsByProperty(ManagedProperty<?> managedProperty, WorldObjectCondition worldObjectCondition) {
		List<WorldObject> worldObjects = world.findWorldObjectsByProperty(managedProperty, worldObjectCondition);
		filter(worldObjects);
		return worldObjects;
	}

	@Override
	public WorldObject findWorldObjectById(int id) {
		WorldObject worldObject = world.findWorldObjectById(id);
		
		List<WorldObject> worldObjects = new ArrayList<>();
		worldObjects.add(worldObject);
		filter(worldObjects);
		
		if (worldObjects.size() == 1) {
			return worldObjects.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean exists(WorldObject worldObject) {
		throw new IllegalStateException("WorldFacade is read-only, exists should not be called");
	}
	
	@Override
	public boolean exists(int id) {
		if (world.exists(id)) {
			WorldObject worldObject = world.findWorldObjectById(id);
			
			List<WorldObject> worldObjects = new ArrayList<>();
			worldObjects.add(worldObject);
			filter(worldObjects);
			
			return worldObjects.size() > 0;
		} else {
			return false;
		}
	}

	@Override
	public int generateUniqueId() {
		throw new IllegalStateException("WorldFacade is read-only, cannot create unique id");
	}

	@Override
	public <T> void logAction(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, T value) {
	}

	@Override
	public void addListener(ManagedOperationListener listener) {
	}

	@Override
	public void removeListener(ManagedOperationListener listener) {
	}
	
	@Override
	public <T> T getListenerByClass(Class<T> clazz) {
		throw new IllegalStateException("WorldFacade is read-only, cannot return listener");
	}

	@Override
	public int getWidth() {
		return world.getWidth();
	}

	@Override
	public int getHeight() {
		return world.getHeight();
	}

	@Override
	public Terrain getTerrain() {
		return world.getTerrain();
	}

	@Override
	public Goal getGoal(WorldObject worldObject) {
		return world.getGoal(worldObject);
	}

	@Override
	public OperationInfo getImmediateGoal(WorldObject worldObject, World world) {
		return world.getImmediateGoal(worldObject, world);
	}

	@Override
	public History getHistory() {
		return world.getHistory();
	}

	@Override
	public void save(File fileToSave) {
	}

	@Override
	public Turn getCurrentTurn() {
		return world.getCurrentTurn();
	}

	@Override
	public void nextTurn() {
		throw new IllegalStateException("WorldFacade is read-only, cannot goto next turn");
	}

	@Override
	public WorldOnTurn getWorldOnTurn() {
		throw new IllegalStateException("WorldFacade is read-only, getWorldOnTurn should not be called");
	}

	@Override
	public WorldStateChangedListeners getWorldStateChangedListeners() {
		return world.getWorldStateChangedListeners();
	}

	@Override
	public void addWorldStateChangedListener(WorldStateChangedListener worldStateChangedListener) {
	}

	@Override
	public WorldObjectsCache getWorldObjectsCache(IntProperty intProperty1, IntProperty intProperty2) {
		return world.getWorldObjectsCache(intProperty1, intProperty2);
	}
	
	@Override
	public WorldObjectsCache getWorldObjectsCache() {
		return world.getWorldObjectsCache();
	}
}
