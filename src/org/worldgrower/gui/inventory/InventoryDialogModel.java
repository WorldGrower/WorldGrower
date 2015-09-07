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
package org.worldgrower.gui.inventory;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.WeightPropertyUtils;

public class InventoryDialogModel {

	private final WorldObject playerCharacter;
	
	public InventoryDialogModel(WorldObject playerCharacter) {
		this.playerCharacter = playerCharacter;
	}

	public int getMoney() {
		return playerCharacter.getProperty(Constants.GOLD);
	}
	
	public int getWeight() {
		return WeightPropertyUtils.getTotalWeight(playerCharacter);
	}
	
	public int getCarryingCapacity() {
		return WeightPropertyUtils.getCarryingCapacity(playerCharacter);
	}
}
