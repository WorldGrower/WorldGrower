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
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class EnlargedCondition implements Condition {

	public EnlargedCondition(List<Condition> allConditions) {
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
		return "enlarged";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
	}
	
	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject, World world) {
		int newWidth = worldObject.getProperty(Constants.ORIGINAL_WIDTH);
		int newHeight = worldObject.getProperty(Constants.ORIGINAL_HEIGHT);
	
		LocationPropertyUtils.updateLocation(
				worldObject,
				worldObject.getProperty(Constants.X), 
				worldObject.getProperty(Constants.Y), 
				newWidth, 
				newHeight, 
				world);
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
		return "an enlarged creature deals more damage with physical attacks";
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.ENLARGE_MAGIC_SPELL;
	}
}
