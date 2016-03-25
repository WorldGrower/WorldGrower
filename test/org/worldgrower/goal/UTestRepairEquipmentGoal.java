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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestRepairEquipmentGoal {

	private RepairEquipmentGoal goal = Goals.REPAIR_EQUIPMENT_GOAL;

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateGoalDamagedEquipment() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.REPAIR_HAMMER.generate(1f), 20);
		
		addDamagedIronCuirass(performer);
		
		assertEquals(Actions.REPAIR_EQUIPMENT_IN_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(Arrays.asList(1), Arrays.asList(goal.calculateGoal(performer, world).getArgs()[0]));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		addDamagedIronCuirass(performer);
		addDamagedIronCuirass(performer);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private void addDamagedIronCuirass(WorldObject performer) {
		WorldObject ironCuirass = Item.IRON_CUIRASS.generate(1f);
		ironCuirass.setProperty(Constants.EQUIPMENT_HEALTH, 0);
		performer.getProperty(Constants.INVENTORY).add(ironCuirass);
	}
}