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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.creaturetype.CreatureType;

public class WorldStateChangedListeners {

	private final List<WorldStateChangedListener> worldStateChangedListeners = new ArrayList<>();
	
	public void addWorldStateChangedListener(WorldStateChangedListener listener) {
		worldStateChangedListeners.add(listener);
	}
	
	public void fireCreatureTypeChanged(WorldObject worldObject, CreatureType newCreatureType, String description) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.creatureTypeChange(worldObject, newCreatureType, description);
		}
	}
	
	public void fireElectionFinished(WorldObject winner, WorldObject organization, IdList candidates) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.electionFinished(winner, organization, candidates);
		}
	}
	
	public void legalActionsChanged(List<LegalAction> changedLegalActions, WorldObject villagerLeader) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.legalActionsChanged(changedLegalActions, villagerLeader);
		}
	}
	
	public void thrownOutOfGroup(WorldObject worldObject, WorldObject target, int[] args, ManagedOperation action, IdList oldGroup, IdList newGroup) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.thrownOutOfGroup(worldObject, target, args, action, oldGroup, newGroup);
		}
	}
	
	public void skillIncreased(WorldObject worldObject, SkillProperty skillProperty, int oldValue, int newValue) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.skillIncreased(worldObject, skillProperty, oldValue, newValue);
		}
	}
	
	public void levelIncreased(WorldObject worldObject, int newValue) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.levelIncreased(worldObject, newValue);
		}
	}
	
	public void conditionGained(WorldObject worldObject, Condition condition) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.conditionGained(worldObject, condition);
		}
	}
	
	public void conditionLost(WorldObject worldObject, Condition condition) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.conditionLost(worldObject, condition);
		}
	}
	
	public void lostLeadership(WorldObject worldObject, WorldObject organization) {
		for(WorldStateChangedListener worldStateChangedListener : worldStateChangedListeners) {
			worldStateChangedListener.lostLeadership(worldObject, organization);
		}
	}
}
