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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestEquipmentGoal {

	private EquipmentGoal goal = Goals.EQUIPMENT_GOAL;

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		addSmith(world, performer);
		
		assertEquals(Actions.PLANT_TREE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalIronClaymore() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		addSmith(world, performer);
		
		assertEquals(Actions.CRAFT_IRON_AXE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalIronCuirass() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		addSmith(world, performer);
		
		assertEquals(Actions.CRAFT_IRON_CUIRASS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCottonShirt() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 8);
		
		addWeavery(world, performer);

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.COTTON.generate(1f), 20);

		assertEquals(Actions.WEAVE_COTTON_SHIRT_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	private void addWeavery(World world, WorldObject performer) {
		int weaveryId = BuildingGenerator.generateWeavery(0, 0, world, performer);
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(weaveryId, BuildingType.WEAVERY));
	}

	private void equip(WorldObject performer, Item item) {
		EquipmentPropertyUtils.equip(performer, item);
	}

	@Test
	public void testCalculateGoalIronHelmet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		addSmith(world, performer);
		
		assertEquals(Actions.CRAFT_IRON_HELMET_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalIronGauntlets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		addSmith(world, performer);
		
		assertEquals(Actions.CRAFT_IRON_GAUNTLETS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalIronBoots() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		equip(performer, Item.IRON_GAUNTLETS);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		addSmith(world, performer);
		
		assertEquals(Actions.CRAFT_IRON_GREAVES_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalEquipClaymore() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CLAYMORE.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEquipIronCuirass() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEquipIronHelmet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_HELMET.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEquipIronGauntlets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GAUNTLETS.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEquipIronBoots() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		equip(performer, Item.IRON_GAUNTLETS);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_BOOTS.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	

	@Test
	public void testCalculateGoalEquipIronGreaves() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		equip(performer, Item.IRON_GAUNTLETS);
		equip(performer, Item.IRON_BOOTS);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREAVES.generate(1f), 20);

		assertEquals(Actions.EQUIP_INVENTORY_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCompletelyEquipped() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.STRENGTH, 20);

		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		equip(performer, Item.IRON_GAUNTLETS);
		equip(performer, Item.IRON_BOOTS);
		equip(performer, Item.IRON_GREAVES);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		equip(performer, Item.IRON_CLAYMORE);
		equip(performer, Item.IRON_CUIRASS);
		equip(performer, Item.IRON_HELMET);
		equip(performer, Item.IRON_GAUNTLETS);
		equip(performer, Item.IRON_BOOTS);
		equip(performer, Item.IRON_GREAVES);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private void addSmith(World world, WorldObject performer) {
		int smithId = BuildingGenerator.generateSmith(0, 0, world, performer);
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(smithId, BuildingType.SMITH));
	}
}