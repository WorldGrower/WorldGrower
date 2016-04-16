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
import org.worldgrower.WorldObject;

public class UTestEnergyPropertyUtils {

	@Test
	public void testCalculateEnergyMax() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(1000, EnergyPropertyUtils.calculateEnergyMax(performer));
		
		performer.setProperty(Constants.CONSTITUTION, 8);
		assertEquals(950, EnergyPropertyUtils.calculateEnergyMax(performer));
		
		performer.setProperty(Constants.CONSTITUTION, 20);
		assertEquals(1250, EnergyPropertyUtils.calculateEnergyMax(performer));
	}
	
	@Test
	public void testIncrement() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		performer.setProperty(Constants.CONSTITUTION, 8);
		performer.setProperty(Constants.ENERGY, 900);
		
		EnergyPropertyUtils.increment(performer, 5);
		assertEquals(905, performer.getProperty(Constants.ENERGY).intValue());
		
		EnergyPropertyUtils.increment(performer, 1000);
		assertEquals(950, performer.getProperty(Constants.ENERGY).intValue());
	}
}
