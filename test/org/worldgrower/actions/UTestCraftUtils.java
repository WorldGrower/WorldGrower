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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestCraftUtils {

	@Test
	public void testIsValidTarget() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		
		assertEquals(true, CraftUtils.isValidTarget(performer, performer, null));
		assertEquals(false, CraftUtils.isValidTarget(performer, target, null));
	}
	
	@Test
	public void testDistance() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(1000, CraftUtils.distance(performer, Constants.WOOD, 1));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Constants.WOOD, 1, null);
		assertEquals(0, CraftUtils.distance(performer, Constants.WOOD, 1));
	}
	
	@Test
	public void testGetRequirementsDescription() {
		assertEquals("Requirements: 1 wood", CraftUtils.getRequirementsDescription(Constants.WOOD, 1));
	}
	
	@Test
	public void testGetRequirementsDescriptionForMultiple() {
		assertEquals("Requirements: 1 wood, 2 stone", CraftUtils.getRequirementsDescription(Constants.WOOD, 1, Constants.STONE, 2));
	}
}
