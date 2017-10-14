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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.TreeType;

public class TreeWoodSource implements WoodSource {

	private static final int MIN_WOOD = 50;
	private static final int MAX_WOOD = 500;
	
	private int woodSource;
	private int woodProduced;

	public TreeWoodSource(int amount) {
		int normalizedAmount = Math.min(MAX_WOOD, amount);
		woodSource = normalizedAmount;
		woodProduced = normalizedAmount;
	}
	
	@Override
	public WorldObject harvest(WorldObject performer, WorldObject target, World world) {
		int quantity = WoodPropertyUtils.calculateLumberingQuantity(performer);
		WorldObject harvestedWood = Item.WOOD.generate(1f);
		performer.getProperty(Constants.INVENTORY).addQuantity(harvestedWood, quantity);
		woodSource -= MIN_WOOD;
		
		checkWoodSourceExhausted(target);
		target.setProperty(Constants.IMAGE_ID, TreeType.getTreeImageId(target));
		
		if (woodSource == 0) {
			target.setProperty(Constants.HIT_POINTS, 0);
		}
		
		return harvestedWood;
	}

	@Override
	public boolean hasEnoughWood() {
		return woodSource > MIN_WOOD;
	}
	
	private void checkWoodSourceExhausted(WorldObject target) {
		if (woodSource <=  2 * MIN_WOOD && woodProduced == MAX_WOOD) {
			target.setProperty(Constants.HIT_POINTS, 0);
		}
	}

	@Override
	public void increaseWoodAmount(int woodIncrease, WorldObject target, World world) {
		if (woodProduced < MAX_WOOD) {
			woodSource = Math.min(MAX_WOOD, woodSource + woodIncrease);
		}
		woodProduced = Math.min(MAX_WOOD, woodProduced + woodIncrease);
		checkWoodSourceExhausted(target);
		
		target.setProperty(Constants.IMAGE_ID, TreeType.getTreeImageId(target));
	}

	@Override
	public void increaseWoodAmountToMax(WorldObject target, World world) {
		while (woodProduced < MAX_WOOD) {
			increaseWoodAmount(1, target, world);
		}
	}
}
