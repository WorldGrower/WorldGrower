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
package org.worldgrower.actions;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldOnTurn;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.ArenaPropertyUtils;

public class ArenaFightOnTurn implements WorldOnTurn {

	@Override
	public void onTurn(World world) {
		List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> ArenaPropertyUtils.personIsScheduledToFight(w));
		for(WorldObject worldObject : worldObjects) {
			Integer opponentId = worldObject.getProperty(Constants.ARENA_OPPONENT_ID);
			if (opponentId.intValue() != -1) {
				if (worldObject.getProperty(Constants.HIT_POINTS).intValue() <= 1 || worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION)) {
					
					WorldObject opponent = world.findWorldObject(Constants.ID, opponentId);
					ArenaPropertyUtils.addPayCheck(worldObject);
					ArenaPropertyUtils.addPayCheck(opponent);
					
					worldObject.setProperty(Constants.ARENA_OPPONENT_ID, null);
					opponent.setProperty(Constants.ARENA_OPPONENT_ID, null);
				}
			}
		}
	}

	@Override
	public void addWorldStateChangedListener(WorldStateChangedListener listener) {
	}

	@Override
	public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description) {
	}

	@Override
	public void electionFinished(WorldObject winner, WorldObject organization) {
	}

	@Override
	public void legalActionsChanged(List<LegalAction> changedLegalActions, WorldObject villagerLeader) {
	}
}
