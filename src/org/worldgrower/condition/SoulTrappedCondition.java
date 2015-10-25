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

import org.worldgrower.Constants;
import org.worldgrower.DefaultGoalObstructedHandler;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;

public class SoulTrappedCondition implements Condition {

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
		return "soul trapped";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn, WorldStateChangedListeners creatureTypeChangedListeners) {
	}
	
	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject) {
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
		if (DefaultGoalObstructedHandler.performerAttacked(managedOperation)) {
			if (target.getProperty(Constants.HIT_POINTS) <= 1) {
				// capture soul
				System.out.println("capture soul");
				WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
				int indexOfEmptySoulGem = performerInventory.getIndexFor(w -> isEmptySoulGem(w));
				int indexOfFilledSoulGem = performerInventory.getIndexFor(w -> isFilledSoulGem(w));
			
				if (indexOfEmptySoulGem != -1) {
					performerInventory.removeQuantity(Constants.SOUL_GEM, -1);
					
					if (indexOfFilledSoulGem != -1) {
						performerInventory.addQuantity(indexOfFilledSoulGem);
					} else {
						WorldObject worldObject = performerInventory.addQuantity(Constants.SOUL_GEM, 1, ImageIds.FILLED_SOUL_GEM);
						worldObject.setProperty(Constants.SOUL_GEM_FILLED, Boolean.TRUE);
					}
				}
			}
		}
	}

	private boolean isEmptySoulGem(WorldObject w) {
		return w.hasProperty(Constants.SOUL_GEM) && (!w.hasProperty(Constants.SOUL_GEM_FILLED) || !w.getProperty(Constants.SOUL_GEM_FILLED));
	}
	
	private boolean isFilledSoulGem(WorldObject w) {
		return w.hasProperty(Constants.SOUL_GEM) && w.hasProperty(Constants.SOUL_GEM_FILLED) && w.getProperty(Constants.SOUL_GEM_FILLED);
	}
	
	@Override
	public boolean isMagicEffect() {
		return true;
	}
}
