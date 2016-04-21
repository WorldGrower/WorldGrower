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
package org.worldgrower.attribute;

import java.io.Serializable;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.GoalCalculator;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class PropertyKnowledge implements Serializable, Knowledge {
	private final int id;
	private final int subjectId;
	private final ManagedProperty<?> managedProperty;
	private final Object value;
	
	public PropertyKnowledge(int subjectId, ManagedProperty<?> managedProperty, Object value) {
		super();
		this.id = KnowledgeIdFactory.getNextId();
		this.subjectId = subjectId;
		this.managedProperty = managedProperty;
		this.value = value;
	}

	public ManagedProperty<?> getManagedProperty() {
		return managedProperty;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public boolean refersToSameKnowledge(Knowledge knowledge) {
		if (knowledge instanceof PropertyKnowledge) {
			return ((PropertyKnowledge)knowledge).getManagedProperty() == managedProperty;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean knowledgeContainsId(int idToRemove) {
		return ((getManagedProperty() instanceof IdContainer) && (getValue().equals(idToRemove)));
	}
	
	@Override
	public Knowledge copy() {
		return new PropertyKnowledge(subjectId, managedProperty, value);
	}
	
	@Override
	public boolean hasProperty(ManagedProperty<?> managedProperty) {
		return this.managedProperty == managedProperty;
	}
	
	@Override
	public boolean hasPropertyValue(ManagedProperty<?> managedProperty, Object value) {
		return this.managedProperty == managedProperty && equals(this.value, value);
	}
	
	private static boolean equals(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((managedProperty == null) ? 0 : managedProperty.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyKnowledge other = (PropertyKnowledge) obj;
		if (managedProperty == null) {
			if (other.managedProperty != null)
				return false;
		} else if (!managedProperty.equals(other.managedProperty))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return managedProperty.getName() + ":" + value + ",id=" + id + ",subjectId=" + subjectId;
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		if (managedProperty == Constants.PROFESSION || managedProperty == Constants.DEITY) {
			return Integer.MIN_VALUE;
		} else if (performer.isControlledByAI()) {
			WorldObject copyOfPerformer = performer.deepCopy();
			copyOfPerformer.setProperty((ManagedProperty<Object>)managedProperty, value);
			GoalCalculator goalCalculator = new GoalCalculator();
			return goalCalculator.getIndexOfLastGoalMet(copyOfPerformer, world);
		} else {
			return managedProperty.getOrdinal();
		}
	}

	@Override
	public int getSubjectId() {
		return subjectId;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean hasEvent(Function<Integer, Boolean> turnFunction, Function<WorldObject, Boolean> targetFunction, World world, ManagedOperation... actions) {
		return false;
	}
}