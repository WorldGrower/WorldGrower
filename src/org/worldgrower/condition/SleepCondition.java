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

import java.util.List;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DefaultGoalObstructedHandler;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageIds;

public class SleepCondition implements Condition {

	public SleepCondition(List<Condition> allConditions) {
		allConditions.add(this);
	}

	@Override
	public boolean canTakeAction() {
		return false;
	}

	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public String getDescription() {
		return "sleeping";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
		if (worldObject.hasProperty(Constants.CONSTITUTION)) {
			Actions.REST_ACTION.execute(worldObject, worldObject, Args.EMPTY, world);
		}
	}
	
	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject, World world) {
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
		if (DefaultGoalObstructedHandler.performerAttacked(managedOperation)) {
			Conditions.remove(target, this, world);
		}
	}
	
	@Override
	public boolean isMagicEffect() {
		return true;
	}
	
	@Override
	public String getLongerDescription() {
		return "a sleeping creature can't take actions and awakes when taking damage";
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.SLEEPING_INDICATOR;
	}
}
