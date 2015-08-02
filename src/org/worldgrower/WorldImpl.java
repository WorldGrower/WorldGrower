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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.attribute.IdProperty;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.History;
import org.worldgrower.history.HistoryImpl;
import org.worldgrower.history.Turn;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainImpl;

public class WorldImpl implements World, Serializable {

	private final List<WorldObject> worldObjects = new ArrayList<>();
	private int nextId;
	private transient List<ManagedOperationListener> listeners = new ArrayList<>();
	private final Terrain terrain;
	private final DungeonMaster dungeonMaster;
	private final History history = new HistoryImpl();
	
	public WorldImpl(int width, int height, DungeonMaster dungeonMaster) {
		this(new TerrainImpl(width, height), dungeonMaster);
	}
	
	private WorldImpl(Terrain terrain, DungeonMaster dungeonMaster) {
		this.terrain = terrain;
		this.dungeonMaster = dungeonMaster;
	}

	@Override
	public void addWorldObject(WorldObject worldObject) {
		worldObjects.add(worldObject);
	}
	
	@Override
	public void removeWorldObject(WorldObject worldObjectToRemove) {
		worldObjects.remove(worldObjectToRemove);
		
		int id = worldObjectToRemove.getProperty(Constants.ID);
		for(WorldObject worldObject : worldObjects) {
			IdProperty[] worldObjectIds = getIdProperties();
			for(IdProperty worldObjectId : worldObjectIds) {
				if (worldObject.hasProperty(worldObjectId) && (worldObject.getProperty(worldObjectId) != null) && (worldObject.getProperty(worldObjectId).intValue() == id)) {
					worldObject.setProperty(worldObjectId, null);
				}
			}
		}
	}
	
	//TODO: add IdMap & IdList
	private IdProperty[] getIdProperties() {
		List<IdProperty> result = new ArrayList<>();
		for(ManagedProperty<?> property : Constants.ALL_PROPERTIES) {
			if (property instanceof IdProperty) {
				result.add((IdProperty)property);
			}
		}
		return result.toArray(new IdProperty[0]);
	}
	
	@Override
	public List<WorldObject> getWorldObjects() {
		return Collections.unmodifiableList(worldObjects);
	}

	@Override
	public List<WorldObject> findWorldObjects(WorldObjectCondition worldObjectCondition) {
		return worldObjects
			.stream()
			.filter(w -> worldObjectCondition.isWorldObjectValid(w))
			.collect(Collectors.toList());
	}
	
	public<T> WorldObject findWorldObject(ManagedProperty<T> propertyKey, T value) {
		List<WorldObject> result = 
				worldObjects
				.stream()
				.filter(w -> w.getProperty(propertyKey).equals(value))
				.collect(Collectors.toList());
		if (result.size() == 1) {
			return result.get(0);
		} else {
			throw new IllegalStateException("Problem finding worldObject with propertyKey " + propertyKey + " and value " + value);
		}
	}

	@Override
	public int generateUniqueId() {
		return nextId++;
	}

	@Override
	public void addListener(ManagedOperationListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(ManagedOperationListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public<T> void logAction(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, T value) {
		listeners.stream().forEach(l -> l.actionPerformed(managedOperation, performer, target, args, value));
	}

	@Override
	public int getWidth() {
		return terrain.getWidth();
	}

	@Override
	public int getHeight() {
		return terrain.getHeight();
	}

	@Override
	public Terrain getTerrain() {
		return terrain;
	}
	
	@Override
	public Goal getGoal(WorldObject worldObject) {
		return dungeonMaster.getGoal(worldObject);
	}
	
	@Override
	public OperationInfo getImmediateGoal(WorldObject worldObject, World world) {
		return dungeonMaster.getImmediateGoal(worldObject, world);
	}

	@Override
	public History getHistory() {
		return history;
	}
	
	@Override
	public void save(File fileToSave) {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileToSave))){

			objectOutputStream.writeObject ( Version.VERSION );
	    	objectOutputStream.writeObject ( this );
	    	
		} catch(IOException ex) {
			throw new RuntimeException("Problem saving file " + fileToSave, ex);
		}
	}
	
	public static World load(File fileToLoad) {
		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileToLoad))) {
			String versionFromFile = (String) objectInputStream.readObject();
			
			if (!versionFromFile.equals( Version.VERSION )) {
				throw new RuntimeException("Version in file " + fileToLoad + " doesn't match: " + versionFromFile + " isn't equal to " + Version.VERSION);
			}
			
			WorldImpl world = (WorldImpl) objectInputStream.readObject();
			world.listeners = new ArrayList<>();
			return world;
			
		} catch(IOException | ClassNotFoundException ex) {
			throw new RuntimeException("Problem loading file " + fileToLoad, ex);
		}
	}
	
	@Override
	public Turn getCurrentTurn() {
		return dungeonMaster.getCurrentTurn();
	}
}
