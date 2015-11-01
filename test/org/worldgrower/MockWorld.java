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
import java.util.List;

import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.History;
import org.worldgrower.history.Turn;
import org.worldgrower.terrain.Terrain;

public class MockWorld implements World {

	private final Terrain terrain;
	private final World world;
	
	public MockWorld(Terrain terrain, World world) {
		super();
		this.terrain = terrain;
		this.world = world;
	}

	@Override
	public void addWorldObject(WorldObject worldObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeWorldObject(WorldObject worldObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<WorldObject> getWorldObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(WorldObject worldObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WorldObject> findWorldObjects(WorldObjectCondition worldObjectCondition) {
		return world.findWorldObjects(worldObjectCondition);
	}

	@Override
	public <T> WorldObject findWorldObject(ManagedProperty<T> propertyKey, T value) {
		return world.findWorldObject(propertyKey, value);
	}

	@Override
	public List<WorldObject> findWorldObjectsByProperty(ManagedProperty<?> managedProperty, WorldObjectCondition worldObjectCondition) {
		return world.findWorldObjectsByProperty(managedProperty, worldObjectCondition);
	}

	@Override
	public int generateUniqueId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> void logAction(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ManagedOperationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ManagedOperationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getListenerByClass(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationInfo getImmediateGoal(WorldObject worldObject, World world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Turn getCurrentTurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nextTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public History getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(File fileToSave) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WorldOnTurn getWorldOnTurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldStateChangedListeners getWorldStateChangedListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addWorldStateChangedListener(WorldStateChangedListener worldStateChangedListener) {
		// TODO Auto-generated method stub
		
	}

	
}
