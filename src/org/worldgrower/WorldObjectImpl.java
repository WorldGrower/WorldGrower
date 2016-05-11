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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.WorldObjectProperties;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.MagicSpellUtils;

public class WorldObjectImpl implements WorldObject, Serializable {
	private final WorldObjectProperties properties;
	private final List<ManagedOperation> operations;
	private final OnTurn onTurn;
	private final WorldObjectPriorities worldObjectPriorities;

	public WorldObjectImpl(Map<ManagedProperty<?>, Object> properties, List<ManagedOperation> operations, OnTurn onTurn, WorldObjectPriorities worldObjectPriorities) {
		this(new WorldObjectProperties(properties), operations, onTurn, worldObjectPriorities, true);
	}
	
	private WorldObjectImpl(WorldObjectProperties properties, List<ManagedOperation> operations, OnTurn onTurn, WorldObjectPriorities worldObjectPriorities, boolean checkProperties) {
		this.properties = properties;
		this.operations = operations; 
		this.onTurn = onTurn;
		this.worldObjectPriorities = worldObjectPriorities;
		
		if (checkProperties) {
			checkProperties();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void checkProperties() {
		for(Entry<ManagedProperty<?>, Object> entry : properties.entrySet()) {
			ManagedProperty managedProperty = entry.getKey();
			Object value = entry.getValue();
			managedProperty.checkValue(value);
		}
	}

	public WorldObjectImpl(Map<ManagedProperty<?>, Object> properties, List<ManagedOperation> operations, WorldObjectPriorities worldObjectPriorities) {
		this(properties, operations, new DoNothingOnTurn(), worldObjectPriorities);
	}
	
	public WorldObjectImpl(Map<ManagedProperty<?>, Object> properties, WorldObjectPriorities worldObjectPriorities) {
		this(properties, new ArrayList<>(), new DoNothingOnTurn(), worldObjectPriorities);
	}

	public WorldObjectImpl(Map<ManagedProperty<?>, Object> properties) {
		this(properties, new ArrayList<>(), new DoNothingOnTurn(), null);
	}

	public WorldObjectImpl(Map<ManagedProperty<?>, Object> properties, OnTurn onTurn) {
		this(properties, new ArrayList<>(), onTurn, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public<T> T getProperty(ManagedProperty<T> propertyKey) {
		return (T) properties.get(propertyKey);
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
	public<T> void setProperty(ManagedProperty<T> propertyKey, T value) {
		propertyKey.checkValue(value);
		properties.put(propertyKey, value);
	}
	
	@Override
	public<T> void setPropertyUnchecked(ManagedProperty<T> propertyKey, T value) {
		properties.put(propertyKey, value);
	}
	
	@Override
	public<T> void removeProperty(ManagedProperty<T> propertyKey) {
		properties.remove(propertyKey);
	}
	
	@Override
	public void increment(IntProperty propertyKey, int incrementValue) {
		int currentValue = this.getProperty(propertyKey) + incrementValue;
		currentValue = propertyKey.normalize(currentValue);
		setProperty(propertyKey, currentValue);
	}
	
	@Override
	public ManagedOperation getOperation(ManagedOperation operation) {
		return operations.get(operations.indexOf(operation));
	}
	
	@Override
	public List<ManagedOperation> getOperations() {
		return operations;
	}
	
	@Override
	public void onTurn(World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		onTurn.onTurn(this, world, creatureTypeChangedListeners);
	}
	
	@Override
	public boolean hasIntelligence() {
		return ((operations.size() > 0) && !hasProperty(Constants.ILLUSION_CREATOR_ID));
	}
	
	@Override
	public boolean isControlledByAI() {
		return (worldObjectPriorities != null);
	}
	
	@Override
	public boolean canWorldObjectPerformAction(ManagedOperation operation) {
		if (operation == Actions.MOVE_ACTION) {
			Curse curse = this.getProperty(Constants.CURSE);
			Conditions conditions = this.getProperty(Constants.CONDITIONS);
			if (curse != null) {
				return curse.canMove();
			} else if (conditions != null && !conditions.canMove()) {
				return false;
			} else {
				CreatureType creatureType = this.getProperty(Constants.CREATURE_TYPE);
				return creatureType.canMove();
			}
		} else if (operation == Actions.TALK_ACTION) {
				Curse curse = this.getProperty(Constants.CURSE);
				if (curse != null) {
					return curse.canTalk();
				} else {
					return true;
				}
		} else if (operation == Actions.POISON_ATTACK_ACTION) {
			//TODO
			return this.getProperty(Constants.NAME).equals("Spider");
		} else if (operation == Actions.COCOON_ACTION) {
			//TODO
			return this.getProperty(Constants.NAME).equals("Spider");
		} else if (operation instanceof MagicSpell) {
			return MagicSpellUtils.canCast(this, (MagicSpell)operation);
		} else {
			Conditions conditions = this.getProperty(Constants.CONDITIONS);
			if (conditions == null || conditions.canTakeAction()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public int hashCode() {
		return this.getProperty(Constants.ID).intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorldObject) {
			WorldObject other = (WorldObject) obj;
			
			Integer thisId = this.getProperty(Constants.ID);
			Integer otherId = other.getProperty(Constants.ID);
			if ((thisId != null) && (otherId != null)) {
				return thisId.intValue() == otherId.intValue();
			}
		}
		
		return false;
	}
	
	@Override
	public List<Goal> getPriorities(World world) {
		return worldObjectPriorities.getPriorities(this, world);
	}
	
	@Override
	public<T> WorldObject shallowCopy() {
		WorldObjectProperties copiedProperties = properties.shallowCopy();
		WorldObjectImpl copyWorldObject = new WorldObjectImpl(copiedProperties, operations, onTurn, worldObjectPriorities, false);
		return copyWorldObject;
	}
	
	@Override
	public<T> WorldObject deepCopy() {
		return deepCopy(onTurn);
	}
	
	@Override
	public<T> WorldObject deepCopy(OnTurn onTurn) {
		WorldObjectProperties copiedProperties = properties.deepCopy();
		WorldObjectImpl copyWorldObject = new WorldObjectImpl(copiedProperties, operations, onTurn, worldObjectPriorities, false);
		return copyWorldObject;
	}
	
	@Override
	public String toString() {
		return properties.toString();
	}
}
