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
package org.worldgrower.deity;

import java.util.List;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldOnTurn;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;

public class DeityWorldOnTurn implements WorldOnTurn {
	private final transient WorldStateChangedListeners creatureTypeChangedListeners = new WorldStateChangedListeners();
	
	@Override
	public void onTurn(World world) {

		for(Deity deity : Deity.ALL_DEITIES) {
			deity.onTurn(world, creatureTypeChangedListeners);
		}
	}
	
	@Override
	public void addWorldStateChangedListener(WorldStateChangedListener listener) {
		this.creatureTypeChangedListeners.addCreatureTypeChangedListener(listener);
	}

	@Override
	public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description) {
		creatureTypeChangedListeners.fireCreatureTypeChanged(worldObject, newCreatureType, description);
	}

	@Override
	public void electionFinished(WorldObject winner, WorldObject organization) {
	}

	@Override
	public void legalActionsChanged(List<LegalAction> changedLegalActions, WorldObject villagerLeader) {
	}
}
