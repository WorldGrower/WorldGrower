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
import org.worldgrower.generator.BerryBushImageCalculator;
import org.worldgrower.generator.Item;

public class BerryBushFoodSource implements FoodSource {
	private static final int MIN_FOOD = 100;
	private static final int MAX_FOOD = 500;
		
	private int foodSource = 1;
	private int foodProduced = 1;

	@Override
	public void eat(WorldObject performer, WorldObject target, World world) {
		int foodIncrease = FoodPropertyUtils.FOOD_MULTIPLIER * FoodPropertyUtils.calculateFarmingQuantity(performer);
		performer.increment(Constants.FOOD, foodIncrease);
		foodSource -= MIN_FOOD;

		target.setProperty(Constants.IMAGE_ID, BerryBushImageCalculator.getImageId(target, world));
		
		checkFoodSourceExhausted(target);
	}
	
	private void checkFoodSourceExhausted(WorldObject target) {
		if (foodSource <= 2 * MIN_FOOD && foodProduced == MAX_FOOD) {
			target.setProperty(Constants.HIT_POINTS, 0);
		}
	}

	@Override
	public void harvest(WorldObject performer, WorldObject target, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		
		WorldObject harvestedFood = Item.BERRIES.generate(1f);
		int quantity = FoodPropertyUtils.calculateFarmingQuantity(performer);
		inventoryPerformer.addQuantity(harvestedFood, quantity);

		foodSource -= MIN_FOOD;
		
		target.setProperty(Constants.IMAGE_ID, BerryBushImageCalculator.getImageId(target, world));
		checkFoodSourceExhausted(target);
	}

	@Override
	public boolean hasEnoughFood() {
		return foodSource >= MIN_FOOD;
	}

	@Override
	public void increaseFoodAmount(int foodIncrease, WorldObject target, World world) {
		if (foodProduced < MAX_FOOD) {
			foodSource = Math.min(MAX_FOOD, foodSource + foodIncrease);
		}
		foodProduced = Math.min(MAX_FOOD, foodProduced + foodIncrease);
		checkFoodSourceExhausted(target);

		target.setProperty(Constants.IMAGE_ID, BerryBushImageCalculator.getImageId(target, world));
	}
	
	@Override
	public void increaseFoodAmountToMax(WorldObject target, World world) {
		while (foodProduced < MAX_FOOD) {
			increaseFoodAmount(1, target, world);
		}
	}
}
