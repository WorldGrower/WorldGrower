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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

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
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 1);
		assertEquals(0, CraftUtils.distance(performer, Constants.WOOD, 1));
	}
	
	@Test
	public void testDistanceForWoodAndOre() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(2000, CraftUtils.distance(performer, 1, 1));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 1);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 1);
		assertEquals(0, CraftUtils.distance(performer, 1, 1));
	}
	
	@Test
	public void testGetRequirementsDescription() {
		assertEquals("Requirements: 1 wood", CraftUtils.getRequirementsDescription(Constants.WOOD, 1));
		assertEquals("Requirements: sufficient energy", CraftUtils.getRequirementsDescription(Constants.ENERGY, 1));
		assertEquals("Requirements: person needs to be adjacent", CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1));
		assertEquals("Requirements: distance to target less than 4 tiles", CraftUtils.getRequirementsDescription(Constants.DISTANCE, 4));
	}
	
	@Test
	public void testGetRequirementsDescriptionForMultiple() {
		assertEquals("Requirements: 1 wood, 2 stone", CraftUtils.getRequirementsDescription(Constants.WOOD, 1, Constants.STONE, 2));
	}
	
	@Test
	public void testGetRequirementsDescriptionWithDescription() {
		assertEquals("Requirements: 1 wood, description", CraftUtils.getRequirementsDescription(Constants.WOOD, 1, "description"));
	}
	
	@Test
	public void testGetRequirementsDescriptionWithSkill() {
		assertEquals("Requirements: skill illusion 7, 1 wood", CraftUtils.getRequirementsDescription(Constants.ILLUSION_SKILL, 7, Constants.WOOD, 1));
	}
	
	@Test
	public void testIsValidBuildTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createWorldObject(0, 0, 1, 1);
		assertEquals(true, CraftUtils.isValidBuildTarget(Actions.BUILD_SHRINE_ACTION, performer, target, world));
	}
}
