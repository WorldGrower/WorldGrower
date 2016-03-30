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

public class UTestArmorPropertyUtils {

	@Test
	public void testCalculateArmor() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(0, ArmorPropertyUtils.calculateArmor(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.IRON_HELMET.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.IRON_CUIRASS.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.IRON_GAUNTLETS.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.IRON_GREAVES.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.IRON_BOOTS.generate(1f));
		
		assertEquals(26 * Item.COMBAT_MULTIPLIER, ArmorPropertyUtils.calculateArmor(performer));
	}
	
	@Test
	public void testCalculateDamageResist() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(0, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.IRON_HELMET.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.IRON_CUIRASS.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.IRON_GAUNTLETS.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.IRON_GREAVES.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.IRON_BOOTS.generate(1f));
		
		assertEquals(10, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.COTTON_HAT.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.COTTON_SHIRT.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.COTTON_GLOVES.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.COTTON_PANTS.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.COTTON_BOOTS.generate(1f));
		
		assertEquals(6, ArmorPropertyUtils.calculateDamageResist(performer));
	}
	
	@Test
	public void testGetSkillBonus() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		assertEquals(1.0, ArmorPropertyUtils.getSkillBonus(performer, Item.IRON_CUIRASS.generate(1f)), 0.1);
		assertEquals(1.0, ArmorPropertyUtils.getSkillBonus(performer, Item.COTTON_SHIRT.generate(1f)), 0.1);
	}
}
