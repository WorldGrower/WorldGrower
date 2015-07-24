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
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class Conditions implements Serializable {

	private final Map<Condition, Integer> conditions = new HashMap<>();
	
	public void addCondition(Condition condition, int turns) {
		conditions.put(condition, turns);
	}
	
	public void removeCondition(Condition condition) {
		conditions.remove(condition);
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
		for(Entry<Condition, Integer> entry : conditions.entrySet()) {
			entry.getKey().onTurn(worldObject, world);
			int turns = entry.getValue();
			turns--;
			if (turns != 0) {
				entry.setValue(turns);
			} else {
				conditions.remove(entry.getKey());
			}
		}
	}

	public boolean hasCondition(Condition condition) {
		return conditions.containsKey(condition);
	}

	@Override
	public String toString() {
		return "Conditions [conditions=" + conditions + "]";
	}
}
