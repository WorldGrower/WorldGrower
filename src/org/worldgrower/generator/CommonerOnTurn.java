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
package org.worldgrower.generator;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Background;
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.DrownUtils;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.goal.WeightPropertyUtils;

public class CommonerOnTurn implements OnTurn {

	public static final int PREGNANCY_DURATION = 200;
	
	private final CommonerGenerator commonerGenerator;
	private final WorldObject organization;
	
	public CommonerOnTurn(CommonerGenerator commonerGenerator, WorldObject organization) {
		this.commonerGenerator = commonerGenerator;
		this.organization = organization;
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		propertiesOnTurn(worldObject, world);
		
		worldObject.getProperty(Constants.CONDITIONS).onTurn(worldObject, world, creatureTypeChangedListeners);
		Background background = worldObject.getProperty(Constants.BACKGROUND);
		if (background != null) {
			background.checkForNewGoals(worldObject, world);
		}
		
		DrownUtils.checkForDrowning(worldObject, world);
		checkPregnancy(worldObject, world);
		adjustPrices(worldObject, world);
		checkForDisease(worldObject, world);
	}

	private void checkForDisease(WorldObject worldObject, World world) {
		int food = worldObject.getProperty(Constants.FOOD);
		int water = worldObject.getProperty(Constants.WATER);
		
		if (food == 0 && water == 0) {
			Conditions conditions = worldObject.getProperty(Constants.CONDITIONS);
			if (!conditions.hasCondition(Condition.ATAXIA_CONDITION)) {
				Conditions.add(worldObject, Condition.ATAXIA_CONDITION, Integer.MAX_VALUE, world);
			}
		}
	}

	void adjustPrices(WorldObject worldObject, World world) {
		if (worldObject.isControlledByAI()) {
			if (world.getCurrentTurn().getValue() % 200 == 0) {
				Prices prices = worldObject.getProperty(Constants.PRICES);
				ItemCountMap itemsSold = worldObject.getProperty(Constants.ITEMS_SOLD);
				
				for(Item itemSold : itemsSold.getItems()) {
					int soldCount = itemsSold.get(itemSold);
					WorldObjectContainer inventory = worldObject.getProperty(Constants.INVENTORY);
					int indexOfItemSold = inventory.getIndexFor(w -> w.getProperty(Constants.ITEM_ID) == itemSold);
					int inventoryCount = getInventoryCount(inventory, indexOfItemSold);
					
					Integer oldPrice = prices.getPrice(itemSold);
					Integer newPrice = calculateNewPrice(soldCount, inventoryCount, oldPrice);
					if (newPrice != null) {
						prices.setPrice(itemSold, newPrice);
						//System.out.println("price changed for " + itemSold + ": " + oldPrice + " --> " + newPrice);
					}
				}
				itemsSold.clear();
			}
		}
	}

	int getInventoryCount(WorldObjectContainer inventory, int indexOfItemSold) {
		int inventoryCount;
		if (indexOfItemSold != -1) {
			inventoryCount = inventory.get(indexOfItemSold).getProperty(Constants.QUANTITY);
		} else {
			inventoryCount = 0;
		}
		return inventoryCount;
	}

	Integer calculateNewPrice(int soldCount, int inventoryCount, int oldPrice) {
		Integer newPrice = null;
		if (inventoryCount == 0) {
			newPrice = oldPrice * 2;
		} else if (soldCount > inventoryCount) {
			newPrice = oldPrice + (oldPrice / 10 + 1);
		} else if (soldCount < inventoryCount) {
			newPrice = oldPrice - (oldPrice / 10 + 1);
			if (newPrice <= 0) {
				newPrice = 1;
			}
		}
		return newPrice;
	}

	private void checkPregnancy(WorldObject worldObject, World world) {
		Integer pregnancy = worldObject.getProperty(Constants.PREGNANCY);
		if (pregnancy != null) {
			pregnancy = pregnancy + 1;
			worldObject.setProperty(Constants.PREGNANCY, pregnancy);
			
			if (pregnancy > PREGNANCY_DURATION) {
				worldObject.setProperty(Constants.PREGNANCY, pregnancy - 201);
				
				int performerX = worldObject.getProperty(Constants.X);
				int performerY = worldObject.getProperty(Constants.Y);
				int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
				if (position != null) {
					int id = commonerGenerator.generateCommoner(position[0] + performerX, position[1] + performerY, world, organization);
					worldObject.setProperty(Constants.PREGNANCY, null);
					worldObject.getProperty(Constants.CHILDREN).add(id);
					
					everyoneInVicinityKnowsOfChild(worldObject, id, world);
					
					logChildBirth(worldObject, world, id);
				}
			}
		}
	}

	private void logChildBirth(WorldObject worldObject, World world, int id) {
		WorldObject child = world.findWorldObject(Constants.ID, id);
		String message = worldObject.getProperty(Constants.NAME) + " has given birth to " + child.getProperty(Constants.NAME);
		world.logAction(Actions.DO_NOTHING_ACTION, worldObject, worldObject, Args.EMPTY, message);
	}

	private void propertiesOnTurn(WorldObject worldObject, World world) {
		boolean isUndead = worldObject.getProperty(Constants.CREATURE_TYPE) == CreatureType.UNDEAD_CREATURE_TYPE;
		if (isUndead) {
			worldObject.increment(Constants.FOOD, 1000);
			worldObject.increment(Constants.WATER, 1000);
		} else {
			worldObject.increment(Constants.FOOD, -1);
			worldObject.increment(Constants.WATER, -1);
		}
		worldObject.increment(Constants.SOCIAL, -1);
		worldObject.increment(Constants.ENERGY, -1);
		worldObject.increment(Constants.ALCOHOL_LEVEL, -1);
		
		if (worldObject.getProperty(Constants.FOOD) < 100) {
			worldObject.increment(Constants.ENERGY, -1);
		}
		if (worldObject.getProperty(Constants.WATER) < 100) {
			worldObject.increment(Constants.ENERGY, -1);
		}
		
		if (WeightPropertyUtils.isCarryingTooMuch(worldObject)) {
			worldObject.increment(Constants.ENERGY, -1);
		}
		
		if (worldObject.hasProperty(Constants.VAMPIRE_BLOOD_LEVEL)) {
			if (worldObject.getProperty(Constants.VAMPIRE_BLOOD_LEVEL) < 10) {
				worldObject.increment(Constants.ENERGY, -5);
			}
			worldObject.increment(Constants.VAMPIRE_BLOOD_LEVEL, -1);
		}
	}
	
	private void everyoneInVicinityKnowsOfChild(WorldObject parent, int childId, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(parent, parent, Constants.CHILD_BIRTH_ID, childId, world);
	}
}
