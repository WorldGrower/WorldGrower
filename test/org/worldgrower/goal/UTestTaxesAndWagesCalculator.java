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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestTaxesAndWagesCalculator {

	private TaxesAndWagesCalculator calculator = new TaxesAndWagesCalculator();
	
	@Test
	public void testCalculateNoIncomeNoExpense() {
		World world = new WorldImpl(1, 1, null, null);
		createVillagersOrganization(world);
		
		TaxesAndWages taxesAndWages = calculator.calculate(world);
		assertEquals(0, taxesAndWages.getShackTaxRate());
		assertEquals(0, taxesAndWages.getHouseTaxRate());
		assertEquals(10, taxesAndWages.getSheriffWage());
		assertEquals(10, taxesAndWages.getTaxCollectorWage());
	}
	
	@Test
	public void testCalculateOneWage() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		world.addWorldObject(performer);
		
		addHouse(performer, world);
		
		createVillagersOrganization(world);
		
		TaxesAndWages taxesAndWages = calculator.calculate(world);
		assertEquals(1, taxesAndWages.getShackTaxRate());
		assertEquals(2, taxesAndWages.getHouseTaxRate());
		assertEquals(10, taxesAndWages.getSheriffWage());
		assertEquals(10, taxesAndWages.getTaxCollectorWage());
	}
	
	@Test
	public void testCalculateTenHousesNoWage() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		for(int i=0; i<10; i++) {
			addHouse(performer, world);
		}
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.SHACK_TAX_RATE, 1);
		villagersOrganization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		
		TaxesAndWages taxesAndWages = calculator.calculate(world);
		assertEquals(1, taxesAndWages.getShackTaxRate());
		assertEquals(2, taxesAndWages.getHouseTaxRate());
		assertEquals(10, taxesAndWages.getSheriffWage());
		assertEquals(10, taxesAndWages.getTaxCollectorWage());
	}
	
	private void addHouse(WorldObject performer, World world) {
		int houseId = BuildingGenerator.generateHouse(0, 0, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		return organization;
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(7, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}