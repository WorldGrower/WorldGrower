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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goals;

public class FearCondition implements Condition {

	public FearCondition(List<Condition> allConditions) {
		allConditions.add(this);
	}

	@Override
	public boolean canTakeAction() {
		return false;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public String getDescription() {
		return "feared";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
		Integer fearCasterId = worldObject.getProperty(Constants.FEAR_CASTER_ID);
		if (fearCasterId != null) {
			WorldObject fearCaster = world.findWorldObject(Constants.ID, fearCasterId);
			int[] args = Goals.PROTECT_ONSE_SELF_GOAL.calculateMoveArgs(worldObject, world, Arrays.asList(fearCaster));
			if (args != null) {
				Actions.MOVE_ACTION.execute(worldObject, worldObject, args, world);
			}
		}
	}
	
	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject, World world) {
		worldObject.removeProperty(Constants.FEAR_CASTER_ID);
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
	}
	
	@Override
	public boolean isMagicEffect() {
		return true;
	}
	
	@Override
	public String getLongerDescription() {
		return "a feared creature can't take actions except moving away from the caster";
	}
}
