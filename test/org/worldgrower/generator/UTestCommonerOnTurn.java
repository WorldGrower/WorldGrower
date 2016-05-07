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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.start.CharacterAttributes;

public class UTestCommonerOnTurn {

	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testOnTurnOfCommonAttributes() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		WorldObject playerCharacter = createPlayerCharacter(world, organization);
		
		assertEquals(1000, playerCharacter.getProperty(Constants.ENERGY).intValue());
		assertEquals(500, playerCharacter.getProperty(Constants.FOOD).intValue());
		assertEquals(500, playerCharacter.getProperty(Constants.WATER).intValue());
		
		playerCharacter.onTurn(world, new WorldStateChangedListeners());
		assertEquals(998, playerCharacter.getProperty(Constants.ENERGY).intValue());
		assertEquals(499, playerCharacter.getProperty(Constants.FOOD).intValue());
		assertEquals(499, playerCharacter.getProperty(Constants.WATER).intValue());
	}
	
	@Test
	public void testOnTurnNoFoodNoWater() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		WorldObject playerCharacter = createPlayerCharacter(world, organization);
		
		assertEquals(1000, playerCharacter.getProperty(Constants.ENERGY).intValue());
		playerCharacter.setProperty(Constants.FOOD, 0);
		playerCharacter.setProperty(Constants.WATER, 0);
		
		playerCharacter.onTurn(world, new WorldStateChangedListeners());
		assertEquals(996, playerCharacter.getProperty(Constants.ENERGY).intValue());
		assertEquals(0, playerCharacter.getProperty(Constants.FOOD).intValue());
		assertEquals(0, playerCharacter.getProperty(Constants.WATER).intValue());
	}

	private WorldObject createPlayerCharacter(World world, WorldObject organization) {
		CharacterAttributes characterAttributes = new CharacterAttributes(10, 10, 10, 10, 10, 10);
		WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(0, "player", "adventurer" , "female", world, commonerGenerator, organization, characterAttributes, ImageIds.KNIGHT);
		return playerCharacter;
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		return world.findWorldObject(Constants.ID, commonerId);
	}
	
	@Test
	public void testGiveBirth() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		CharacterAttributes characterAttributes = new CharacterAttributes(10, 10, 10, 10, 10, 10);
		WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(7, "player", "adventurer" , "female", world, commonerGenerator, organization, characterAttributes, ImageIds.KNIGHT);

		playerCharacter.setProperty(Constants.PREGNANCY, 500);
		playerCharacter.onTurn(world, new WorldStateChangedListeners());
		
		assertEquals(null, playerCharacter.getProperty(Constants.PREGNANCY));
		assertEquals(1, playerCharacter.getProperty(Constants.CHILDREN).size());
	}
	
	@Test
	public void testGetInventoryCount() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		WorldObject playerCharacter = createPlayerCharacter(world, organization);
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
		inventory.addQuantity(Item.BERRIES.generate(1f));
		
		assertEquals(1, new CommonerOnTurn(commonerGenerator, organization).getInventoryCount(inventory, inventory.getIndexFor(Constants.FOOD)));
		assertEquals(0, new CommonerOnTurn(commonerGenerator, organization).getInventoryCount(inventory, -1));
	}
	
	@Test
	public void testCalculateNewPrice() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		int soldCount = 0;
		int inventoryCount = 0;
		int oldPrice = 1;
		assertEquals(2, new CommonerOnTurn(commonerGenerator, organization).calculateNewPrice(soldCount, inventoryCount, oldPrice).intValue());
		
		inventoryCount = 10;
		assertEquals(1, new CommonerOnTurn(commonerGenerator, organization).calculateNewPrice(soldCount, inventoryCount, oldPrice).intValue());
		
		soldCount = 10;
		assertEquals(null, new CommonerOnTurn(commonerGenerator, organization).calculateNewPrice(soldCount, inventoryCount, oldPrice));
		
		soldCount = 20;
		assertEquals(2, new CommonerOnTurn(commonerGenerator, organization).calculateNewPrice(soldCount, inventoryCount, oldPrice).intValue());
	}
	
	@Test
	public void testAdjustPrices() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		WorldObject commoner = createCommoner(world, organization);
		commoner.getProperty(Constants.ITEMS_SOLD).add(Item.BERRIES, 20);
		Prices prices = commoner.getProperty(Constants.PRICES);
		
		assertEquals(1, prices.getPrice(Item.BERRIES));
		new CommonerOnTurn(commonerGenerator, organization).adjustPrices(commoner, world);
		assertEquals(2, prices.getPrice(Item.BERRIES));
		
		assertEquals(false, commoner.getProperty(Constants.ITEMS_SOLD).contains(Item.BERRIES));
	}
}