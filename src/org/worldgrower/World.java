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

/** 
 * A world contains a collection of WorldObjects, as well as the history of performed actions and the world terrain. 
 * It contains all the state for this program.
 */
public interface World {

	public void addWorldObject(WorldObject worldObject);
	public void removeWorldObject(WorldObject worldObject);
	public List<WorldObject> getWorldObjects();
	public boolean exists(WorldObject worldObject);
	public boolean exists(int id);
	
	public List<WorldObject> findWorldObjects(WorldObjectCondition worldObjectCondition);
	public<T> WorldObject findWorldObject(ManagedProperty<T> propertyKey, T value);
	public List<WorldObject> findWorldObjectsByProperty(ManagedProperty<?> managedProperty, WorldObjectCondition worldObjectCondition);
	
	public int generateUniqueId();
	public<T> void logAction(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, T value);
	public void addListener(ManagedOperationListener listener);
	public void removeListener(ManagedOperationListener listener);
	public<T> T getListenerByClass(Class<T> clazz);
	
	public int getWidth();
	public int getHeight();
	public Terrain getTerrain();
	
	public Goal getGoal(WorldObject worldObject);
	public OperationInfo getImmediateGoal(WorldObject worldObject, World world);
	public Turn getCurrentTurn();
	public void nextTurn();
	
	public History getHistory();
	public void save(File fileToSave);
	public WorldOnTurn getWorldOnTurn();
	public WorldStateChangedListeners getWorldStateChangedListeners();
	public void addWorldStateChangedListener(WorldStateChangedListener worldStateChangedListener);
	
	
}
