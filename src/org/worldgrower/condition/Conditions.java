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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class Conditions implements Serializable {

	private final Map<Condition, ConditionInfo> conditions = new HashMap<>();
	
	public void addCondition(Condition condition, int turns, World world) {
		conditions.put(condition, new ConditionInfo(turns, world.getCurrentTurn().getValue()));
	}
	
	public void removeCondition(Condition condition) {
		conditions.remove(condition);
	}
	
	public void removeAllDiseases() {
		Iterator<Entry<Condition, ConditionInfo>> conditionIterator = conditions.entrySet().iterator();
		while (conditionIterator.hasNext()) {
			Entry<Condition, ConditionInfo> conditionEntry = conditionIterator.next();
			if (conditionEntry.getKey().isDisease()) {
				conditionIterator.remove();
			}
		}
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
	
	public void onTurn(WorldObject worldObject, World world) {
		for(Entry<Condition, ConditionInfo> entry : conditions.entrySet()) {
			int startTurns = entry.getValue().getStartTurn();
			entry.getKey().onTurn(worldObject, world, startTurns);
			int turnsItWillLast = entry.getValue().getTurnsItWillLast();
			turnsItWillLast--;
			if (turnsItWillLast != 0) {
				entry.getValue().setTurnsItWillLast(turnsItWillLast);
			} else {
				conditions.remove(entry.getKey());
			}
		}
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
}
