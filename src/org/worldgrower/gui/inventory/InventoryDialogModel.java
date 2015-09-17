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

import java.awt.Image;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.WeightPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class InventoryDialogModel {

	private final WorldObject playerCharacter;
	private final WorldObject target;
	
	public InventoryDialogModel(WorldObject playerCharacter) {
		this.playerCharacter = playerCharacter;
		this.target = null;
	}
	
	public InventoryDialogModel(WorldObject playerCharacter, WorldObject target) {
		this.playerCharacter = playerCharacter;
		this.target = target;
	}

	public int getPlayerCharacterMoney() {
		return playerCharacter.getProperty(Constants.GOLD);
	}
	
	public int getPlayerCharacterWeight() {
		return WeightPropertyUtils.getTotalWeight(playerCharacter);
	}
	
	public int getPlayerCharacterCarryingCapacity() {
		return WeightPropertyUtils.getCarryingCapacity(playerCharacter);
	}
	
	public int getTargetMoney() {
		return target.getProperty(Constants.GOLD);
	}
	
	public int getTargetWeight() {
		return WeightPropertyUtils.getTotalWeight(target);
	}
	
	public int getTargetCarryingCapacity() {
		return WeightPropertyUtils.getCarryingCapacity(target);
	}
	
	public Image getPlayerCharacterImage(ImageInfoReader imageInfoReader) {
		ImageIds imageIdPerformer = playerCharacter.getProperty(Constants.IMAGE_ID);
		return imageInfoReader.getImage(imageIdPerformer, null);
	}
	
	public Image getTargetImage(ImageInfoReader imageInfoReader) {
		ImageIds imageIdPerformer = target.getProperty(Constants.IMAGE_ID);
		return imageInfoReader.getImage(imageIdPerformer, null);
	}
	
	public WorldObjectContainer getPlayerCharacterInventory() {
		return playerCharacter.getProperty(Constants.INVENTORY);
	}
	
	public WorldObjectContainer getTargetInventory() {
		return target.getProperty(Constants.INVENTORY);
	}
	
	public boolean hasTarget() {
		return target != null;
	}
}
