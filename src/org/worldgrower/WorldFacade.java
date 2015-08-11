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

import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.History;
import org.worldgrower.history.Turn;
import org.worldgrower.terrain.Terrain;

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
		}
	}

	private static boolean illusionIsBelievedBy(WorldObject personViewingWorld, WorldObject worldObject, World world) {
		final int insight;
		if (personViewingWorld.getProperty(Constants.INSIGHT_SKILL) == null) {
			insight = 0;
		} else {
			insight = personViewingWorld.getProperty(Constants.INSIGHT_SKILL).getLevel();
		}
				
		int illusionCreatorId = worldObject.getProperty(Constants.ILLUSION_CREATOR_ID);
		WorldObject illusionCreator = world.findWorldObject(Constants.ID, illusionCreatorId);
		int illusion = illusionCreator.getProperty(Constants.ILLUSION_SKILL).getLevel();
		
		return (illusion > insight);
	}
	
	@Override
	public List<WorldObject> findWorldObjects(WorldObjectCondition worldObjectCondition) {
		List<WorldObject> worldObjects = world.findWorldObjects(worldObjectCondition);
		filter(worldObjects);
		return worldObjects;
	}

	@Override
	public <T> WorldObject findWorldObject(ManagedProperty<T> propertyKey, T value) {
		WorldObject worldObject = world.findWorldObject(propertyKey, value);
		
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
}
