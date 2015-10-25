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

import org.worldgrower.WorldObject;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.attribute.IdList;
import org.worldgrower.creaturetype.CreatureType;

public class WorldStateChangedListeners {

	private final List<WorldStateChangedListener> worldStateChangedListeners = new ArrayList<>();
	
	public void addCreatureTypeChangedListener(WorldStateChangedListener listener) {
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
}
