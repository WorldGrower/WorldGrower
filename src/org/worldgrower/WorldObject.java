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
import org.worldgrower.goal.Goal;

/**
 * A worldObject is a collections of properties and actions it may execute.
 * Both non-player-characters and lifeless objects are described by WorldObjects.
 */
public interface WorldObject {

	public<T> T getProperty(ManagedProperty<T> propertyKey);
	public boolean hasProperty(ManagedProperty<?> propertyKey);
	public List<ManagedProperty<?>> getPropertyKeys();
	public<T> void setProperty(ManagedProperty<T> propertyKey, T value);
	public<T> void setPropertyUnchecked(ManagedProperty<T> propertyKey, T value);
	
	public void increment(IntProperty propertyKey, int incrementValue);
	public ManagedOperation getOperation(ManagedOperation operation);
	public List<ManagedOperation> getOperations();
	
	public void onTurn(World world);
	public boolean hasIntelligence();
	public boolean isControlledByAI();
	public boolean canWorldObjectPerformAction(ManagedOperation operation);
	public List<Goal> getPriorities(World world);
	public<T> WorldObject shallowCopy();
	public<T> WorldObject deepCopy();
	
	public boolean equals(Object obj);
	
	public String toString();
}
