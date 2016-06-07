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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.WorldObjectProperties;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;

public class IllusionaryWorldObject implements WorldObject, Serializable {

	private final WorldObjectProperties properties;
	private int turnsToLive;
	
	public IllusionaryWorldObject(WorldObjectProperties properties, int turnsToLive) {
		super();
		this.properties = properties;
		this.turnsToLive = turnsToLive;
	}

	public IllusionaryWorldObject(WorldObject sourceWorldObject, int turnsToLive) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		for(ManagedProperty<?> key : sourceWorldObject.getPropertyKeys()) {
			properties.put(key, sourceWorldObject.getProperty(key));
		}
		
		this.properties = new WorldObjectProperties(properties);
		this.turnsToLive = turnsToLive;
	}

	@Override
	public <T> T getProperty(ManagedProperty<T> propertyKey) {
		return properties.get(propertyKey);
	}

	@Override
	public boolean hasProperty(ManagedProperty<?> propertyKey) {
		return properties.containsKey(propertyKey);
	}

	@Override
	public List<ManagedProperty<?>> getPropertyKeys() {
		return properties.keySet();
	}

	@Override
	public <T> void setProperty(ManagedProperty<T> propertyKey, T value) {
	}
	
	public <T> void setPropertyInternal(ManagedProperty<T> propertyKey, T value) {
		properties.put(propertyKey, value);
	}

	@Override
	public <T> void setPropertyUnchecked(ManagedProperty<T> propertyKey, T value) {
	}

	@Override
	public <T> void removeProperty(ManagedProperty<T> propertyKey) {
	}

	@Override
	public void increment(IntProperty propertyKey, int incrementValue) {
	}

	@Override
	public ManagedOperation getOperation(ManagedOperation operation) {
		throw new UnsupportedOperationException("Method getOperation is not supported");
	}

	@Override
	public List<ManagedOperation> getOperations() {
		throw new UnsupportedOperationException("Method getOperation is not supported");
	}

	@Override
	public void onTurn(World world, WorldStateChangedListeners worldStateChangedListeners) {
		turnsToLive--;
		
		if (turnsToLive == 0) {
			world.removeWorldObject(this);
		}
	}

	@Override
	public boolean hasIntelligence() {
		return false;
	}

	@Override
	public boolean isControlledByAI() {
		return false;
	}

	@Override
	public boolean canWorldObjectPerformAction(ManagedOperation operation) {
		return false;
	}

	@Override
	public List<Goal> getPriorities(World world) {
		return null;
	}

	@Override
	public <T> WorldObject shallowCopy() {
		return new IllusionaryWorldObject(properties, turnsToLive);
	}

	@Override
	public <T> WorldObject deepCopy() {
		return new IllusionaryWorldObject(properties, turnsToLive);
	}

	@Override
	public <T> WorldObject deepCopy(OnTurn onTurn) {
		return new IllusionaryWorldObject(properties, turnsToLive);
	}
}
