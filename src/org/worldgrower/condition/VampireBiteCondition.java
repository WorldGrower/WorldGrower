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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

public class VampireBiteCondition implements Condition {

	public VampireBiteCondition(List<Condition> allConditions) {
		allConditions.add(this);
	}

	@Override
	public boolean canTakeAction() {
		return true;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public String getDescription() {
		return "porphyric hemophilia";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
		int currentTurn = world.getCurrentTurn().getValue();
		
		if (currentTurn - startTurn > 1000) {
			VampireUtils.vampirizePerson(worldObject, creatureTypeChangedListeners);
			worldObject.getProperty(Constants.CONDITIONS).setConditionToEndOnNextOnTurn(this);
		}
	}
	
	@Override
	public boolean isDisease() {
		return true;
	}

	@Override
	public void conditionEnds(WorldObject worldObject, World world) {
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
	}
	
	@Override
	public boolean isMagicEffect() {
		return false;
	}

	@Override
	public String getLongerDescription() {
		return "a creature with porphyric hemophilia will eventually turn into a vampire";
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.BLOOD;
	}
}
