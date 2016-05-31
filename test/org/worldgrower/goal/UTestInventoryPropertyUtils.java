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
import org.worldgrower.generator.Item;

public class UTestInventoryPropertyUtils {

	@Test
	public void testCleanupEquipmentSlots() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, "Test");
		
		WorldObject ironCuirass = Item.IRON_CUIRASS.generate(1f);
		performer.getProperty(Constants.INVENTORY).add(ironCuirass);
		performer.setProperty(Constants.TORSO_EQUIPMENT, ironCuirass);
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.IRON_HELMET.generate(1f));
		
		InventoryPropertyUtils.cleanupEquipmentSlots(performer);
		
		assertEquals("iron cuirass", performer.getProperty(Constants.TORSO_EQUIPMENT).getProperty(Constants.NAME));
		assertEquals(null, performer.getProperty(Constants.HEAD_EQUIPMENT));
	}
}
