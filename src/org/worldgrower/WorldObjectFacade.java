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

import java.util.List;

import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.Goal;

/**
 * A WorldObjectFacade takes the original WorldObject and the facade.
 * If a property is set in the facade, it is returned.
 * If a property is not set in the facade, the value in the original WorldObject is returned.
 */
public class WorldObjectFacade implements WorldObject {

	private final WorldObject originalWorldObject;
	private final WorldObject facadeWorldObject;

	public WorldObjectFacade(WorldObject originalWorldObject, WorldObject facadeWorldObject) {
		this.originalWorldObject = originalWorldObject;
		this.facadeWorldObject = facadeWorldObject;
	}

	@Override
	public <T> T getProperty(ManagedProperty<T> propertyKey) {
		T originalValue = originalWorldObject.getProperty(propertyKey);
		T facadeValue = facadeWorldObject.getProperty(propertyKey);
		
		if (facadeValue != null) {
			return facadeValue;
		} else {
			return originalValue;
		}
	}

	@Override
	public boolean hasProperty(ManagedProperty<?> propertyKey) {
		return originalWorldObject.hasProperty(propertyKey);
	}

	@Override
	public List<ManagedProperty<?>> getPropertyKeys() {
		return originalWorldObject.getPropertyKeys();
	}

	@Override
	public <T> void setProperty(ManagedProperty<T> propertyKey, T value) {
		originalWorldObject.setProperty(propertyKey, value);
	}
	
	@Override
	public <T> void setPropertyUnchecked(ManagedProperty<T> propertyKey, T value) {
		originalWorldObject.setPropertyUnchecked(propertyKey, value);
	}
	
	@Override
	public <T> void removeProperty(ManagedProperty<T> propertyKey) {
		originalWorldObject.removeProperty(propertyKey);
	}

	@Override
	public void increment(IntProperty propertyKey, int incrementValue) {
		originalWorldObject.increment(propertyKey, incrementValue);
	}

	@Override
	public ManagedOperation getOperation(ManagedOperation operation) {
		return originalWorldObject.getOperation(operation);
	}

	@Override
	public List<ManagedOperation> getOperations() {
		return originalWorldObject.getOperations();
	}

	@Override
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		originalWorldObject.onTurn(world, creatureTypeChangedListeners);
	}

	@Override
	public boolean hasIntelligence() {
		return originalWorldObject.hasIntelligence();
	}

	@Override
	public boolean isControlledByAI() {
		return originalWorldObject.isControlledByAI();
	}
	
	@Override
	public boolean canWorldObjectPerformAction(ManagedOperation operation) {
		return originalWorldObject.canWorldObjectPerformAction(operation);
	}

	@Override
	public List<Goal> getPriorities(World world) {
		return originalWorldObject.getPriorities(world);
	}

	@Override
	public int hashCode() {
		return this.getProperty(Constants.ID).intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorldObject) {
			WorldObject other = (WorldObject) obj;
			return this.getProperty(Constants.ID).intValue() == other.getProperty(Constants.ID).intValue();
		} else {
			return false;
		}
	}

	@Override
	public <T> WorldObject shallowCopy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> WorldObject deepCopy() {
		return originalWorldObject.deepCopy();
	}

	@Override
	public WorldObject getActionWorldObject() {
		return originalWorldObject;
	}

	@Override
	public OnTurn getOnTurn() {
		return originalWorldObject.getOnTurn();
	}
}
