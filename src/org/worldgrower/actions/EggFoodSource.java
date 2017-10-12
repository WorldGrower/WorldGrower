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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class EggFoodSource implements FoodSource {

	@Override
	public void eat(WorldObject performer, WorldObject target, World world) {
		performer.increment(Constants.FOOD, FoodPropertyUtils.FOOD_MULTIPLIER);
		target.setProperty(Constants.HIT_POINTS, 0);
	}

	@Override
	public void harvest(WorldObject performer, WorldObject target, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		
		WorldObject harvestedFood = Item.EGG.generate(1f);
		inventoryPerformer.addQuantity(harvestedFood, 1);

		target.setProperty(Constants.HIT_POINTS, 0);
	}

	@Override
	public boolean hasEnoughFood() {
		return true;
	}

	@Override
	public void increaseFoodAmount(int foodIncrease, WorldObject target, World world) {
	}

	@Override
	public void increaseFoodAmountToMax(WorldObject target, World world) {
	}
}
