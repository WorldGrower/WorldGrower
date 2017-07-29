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

import java.io.Serializable;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.profession.Professions;

public class TaxesAndWagesCalculator implements Serializable {

	public TaxesAndWages calculate(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		int shackTaxRate = villagersOrganization.getProperty(Constants.SHACK_TAX_RATE);
		int houseTaxRate = villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE);
		int sheriffWage = villagersOrganization.getProperty(Constants.SHERIFF_WAGE);
		int taxCollectorWage = villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE);
		
		int totalIncome = calculateTotalIncome(shackTaxRate, houseTaxRate, world);
		int totalExpense = calculateTotalExpense(sheriffWage, taxCollectorWage, world);
		
		if (totalExpense > totalIncome) {
			shackTaxRate++;
			houseTaxRate++;
			
			if (houseTaxRate <= shackTaxRate) {
				houseTaxRate++;
			}
		} else if (totalExpense < totalIncome) {
			int numberOfOwnedShacks = HousePropertyUtils.getOwnedBuildingCount(BuildingType.SHACK, world);
			int numberOfOwnedHouses = HousePropertyUtils.getOwnedBuildingCount(BuildingType.HOUSE, world);
			
			if (houseTaxRate > shackTaxRate + 1) {
				if (totalExpense < totalIncome - numberOfOwnedHouses) {
					houseTaxRate--;
					totalIncome = calculateTotalIncome(shackTaxRate, houseTaxRate, world);
				}
			}
			if (totalExpense < totalIncome - numberOfOwnedShacks) {
				shackTaxRate--;
			}
		}
		
		return new TaxesAndWages(shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage);
	}
	
	public TaxesAndWages getCurrentTaxesAndWages(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		int shackTaxRate = villagersOrganization.getProperty(Constants.SHACK_TAX_RATE);
		int houseTaxRate = villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE);
		int sheriffWage = villagersOrganization.getProperty(Constants.SHERIFF_WAGE);
		int taxCollectorWage = villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE);
		
		return new TaxesAndWages(shackTaxRate, houseTaxRate, sheriffWage, taxCollectorWage);
	}
	
	private int calculateTotalIncome(int shackTaxRate, int houseTaxRate, World world) {
		int numberOfOwnedShacks = HousePropertyUtils.getOwnedBuildingCount(BuildingType.SHACK, world);
		int totalShackIncome = shackTaxRate * numberOfOwnedShacks;
		
		int numberOfOwnedHouses = HousePropertyUtils.getOwnedBuildingCount(BuildingType.HOUSE, world);
		int totalHouseIncome = houseTaxRate * numberOfOwnedHouses;
		
		return totalShackIncome + totalHouseIncome;
	}
	
	private int calculateTotalExpense(int sheriffWage, int taxCollectorWage, World world) {
		int numberOfSheriffs = Professions.getProfessionCount(world, Constants.CAN_ATTACK_CRIMINALS);
		int totalSheriffExpense = sheriffWage * numberOfSheriffs;
		
		int numberOfTaxCollectors = Professions.getProfessionCount(world, Constants.CAN_COLLECT_TAXES);
		int totalTaxCollectorExpense = taxCollectorWage * numberOfTaxCollectors;
		
		return totalSheriffExpense + totalTaxCollectorExpense;
	}
}
