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
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestEquipmentGoal {

	private EquipmentGoal goal = Goals.EQUIPMENT_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalIronClaymore() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		assertEquals(Actions.CRAFT_IRON_CLAYMORE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalIronCuirass() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CLAYMORE.generate(1f));
		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		assertEquals(Actions.CRAFT_IRON_CUIRASS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	private void equip(WorldObject performer, Item item) {
		WorldObject equipment = item.generate(1f);
		UnCheckedProperty<WorldObject> equipmentSlot = equipment.getProperty(Constants.EQUIPMENT_SLOT);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(equipment);
		performer.setProperty(equipmentSlot, equipment);
	}
	
	@Test
	public void testCalculateGoalIronHelmet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		assertEquals(Actions.CRAFT_IRON_HELMET_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalIronGauntlets() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_HELMET.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		assertEquals(Actions.CRAFT_IRON_GAUNTLETS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalIronBoots() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_HELMET.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GAUNTLETS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		assertEquals(Actions.CRAFT_IRON_GREAVES_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCompletelyEquipped() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		equip(performer, Item.IRON_CLAYMORE);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_HELMET.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GAUNTLETS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_BOOTS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREAVES.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		//TODO: should be null & add equipping the items & light/heavy armor split & equip now only works with LEFT_HAND weapon
		assertEquals(null, goal.calculateGoal(performer, world));
	}
}