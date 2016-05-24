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
package org.worldgrower.condition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

/**
 * This class holds all conditions of a WorldObjects.
 */
public class Conditions implements Serializable {

	private final Map<Condition, ConditionInfo> conditions = new LinkedHashMap<>();
	
	void addCondition(WorldObject worldObject, Condition condition, int turns, World world) {
		conditions.put(condition, new ConditionInfo(turns, world.getCurrentTurn().getValue()));
		conditionGained(worldObject, condition, world.getWorldStateChangedListeners());
	}
	
	private void removeConditionFromWorldObject(WorldObject worldObject, Condition condition, WorldStateChangedListeners worldStateChangedListeners, World world) {
		conditions.remove(condition);
		conditionEnds(worldObject, condition, worldStateChangedListeners, world);
	}
	
	private void removeConditionFromWorldObjectWhileIterating(WorldObject worldObject, Condition condition, Iterator<Entry<Condition, ConditionInfo>> conditionIterator, WorldStateChangedListeners worldStateChangedListeners, World world) {
		conditionIterator.remove();
		conditionEnds(worldObject, condition, worldStateChangedListeners, world);
	}

	private void conditionEnds(WorldObject worldObject, Condition condition, WorldStateChangedListeners worldStateChangedListeners, World world) {
		condition.conditionEnds(worldObject, world);
		conditionLost(worldObject, condition, worldStateChangedListeners);
	}
	
	public static void add(WorldObject worldObject, Condition condition, int turns, World world) {
		worldObject.getProperty(Constants.CONDITIONS).addCondition(worldObject, condition, turns, world);
	}
	
	public static void remove(WorldObject worldObject, Condition condition, World world) {
		worldObject.getProperty(Constants.CONDITIONS).removeConditionFromWorldObject(worldObject, condition, world.getWorldStateChangedListeners(), world);
	}
	
	public void removeAllDiseases(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners) {
		removeAll(worldObject, c -> c.isDisease(), worldStateChangedListeners);
	}
	
	public boolean canTakeAction() {
		for(Condition condition : conditions.keySet()) {
			if (!condition.canTakeAction()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean canMove() {
		for(Condition condition : conditions.keySet()) {
			if (!condition.canMove()) {
				return false;
			}
		}
		return true;
	}
	
	public List<String> getDescriptions() {
		List<String> descriptions = new ArrayList<>();
		for(Condition condition : conditions.keySet()) {
			descriptions.add(condition.getDescription());
		}
		return descriptions;
	}
	
	public List<ImageIds> getImageIds() {
		List<ImageIds> imageIds = new ArrayList<>();
		for(Condition condition : conditions.keySet()) {
			imageIds.add(condition.getImageIds());
		}
		return imageIds;
	}
	
	public List<String> getLongerDescriptions() {
		List<String> descriptions = new ArrayList<>();
		for(Condition condition : conditions.keySet()) {
			descriptions.add(condition.getLongerDescription());
		}
		return descriptions;
	}
	
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		Iterator<Entry<Condition, ConditionInfo>> conditionIterator = conditions.entrySet().iterator();
		while (conditionIterator.hasNext()) {
			Entry<Condition, ConditionInfo> entry = conditionIterator.next();
			int startTurns = entry.getValue().getStartTurn();
			entry.getKey().onTurn(worldObject, world, startTurns, creatureTypeChangedListeners);
			int turnsItWillLast = entry.getValue().getTurnsItWillLast();
			turnsItWillLast--;
			if (turnsItWillLast != 0) {
				entry.getValue().setTurnsItWillLast(turnsItWillLast);
			} else {
				removeConditionFromWorldObjectWhileIterating(worldObject, entry.getKey(), conditionIterator, world.getWorldStateChangedListeners(), world);
			}
		}
	}
	
	void setConditionToEndOnNextOnTurn(Condition condition) {
		conditions.get(condition).setTurnsItWillLast(1);
	}

	public boolean hasCondition(Condition condition) {
		return conditions.containsKey(condition);
	}
	
	private static class ConditionInfo implements Serializable {
		private int turnsItWillLast;
		private int startTurn;
		
		public ConditionInfo(int turnsItWillLast, int startTurn) {
			this.turnsItWillLast = turnsItWillLast;
			this.startTurn = startTurn;
		}

		public int getTurnsItWillLast() {
			return turnsItWillLast;
		}

		public void setTurnsItWillLast(int turnsItWillLast) {
			this.turnsItWillLast = turnsItWillLast;
		}

		public int getStartTurn() {
			return startTurn;
		}

		public void setStartTurn(int startTurn) {
			this.startTurn = startTurn;
		}
	}

	@Override
	public String toString() {
		return "Conditions [conditions=" + conditions + "]";
	}

	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
		Iterator<Entry<Condition, ConditionInfo>> conditionIterator = conditions.entrySet().iterator();
		while (conditionIterator.hasNext()) {
			Entry<Condition, ConditionInfo> conditionEntry = conditionIterator.next();
			conditionEntry.getKey().perform(performer, target, args, managedOperation, world);
		}
	}

	public boolean hasDiseaseCondition() {
		return getDiseaseConditions().size() > 0;
	}

	public List<Condition> getDiseaseConditions() {
		List<Condition> diseases = new ArrayList<>();
		for(Condition condition : conditions.keySet()) {
			if (condition.isDisease()) {
				diseases.add(condition);
			}
		}
		return diseases;
	}

	public void removeAllMagicEffects(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners) {
		removeAll(worldObject, c -> c.isMagicEffect(), worldStateChangedListeners);
	}
	
	private void removeAll(WorldObject worldObject, Function<Condition, Boolean> function, WorldStateChangedListeners worldStateChangedListeners) {
		Iterator<Entry<Condition, ConditionInfo>> conditionIterator = conditions.entrySet().iterator();
		while (conditionIterator.hasNext()) {
			Entry<Condition, ConditionInfo> conditionEntry = conditionIterator.next();
			if (function.apply(conditionEntry.getKey())) {
				conditionLost(worldObject, conditionEntry.getKey(), worldStateChangedListeners);
				conditionIterator.remove();
			}
		}
	}
	
	private void conditionGained(WorldObject worldObject, Condition condition, WorldStateChangedListeners worldStateChangedListeners) {
		worldStateChangedListeners.conditionGained(worldObject, condition);
	}
	
	private void conditionLost(WorldObject worldObject, Condition condition, WorldStateChangedListeners worldStateChangedListeners) {
		worldStateChangedListeners.conditionLost(worldObject, condition);
	}

	public List<Condition> getMagicConditions() {
		List<Condition> magicConditions = new ArrayList<>();
		for(Condition condition : conditions.keySet()) {
			if (condition.isMagicEffect()) {
				magicConditions.add(condition);
			}
		}
		return magicConditions;
	}

	public Conditions copy() {
		Conditions copy = new Conditions();
		copy.conditions.putAll(conditions);
		return copy;
	}
}
