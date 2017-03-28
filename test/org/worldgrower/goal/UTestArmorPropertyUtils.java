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
		
		assertEquals(170 * Item.COMBAT_MULTIPLIER, ArmorPropertyUtils.calculateArmor(performer));
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
		assertEquals(12, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, Item.IRON_SHIELD.generate(1f));
		assertEquals(13, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.COTTON_HAT.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.COTTON_SHIRT.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.COTTON_GLOVES.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.COTTON_PANTS.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.COTTON_BOOTS.generate(1f));
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
		assertEquals(6, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.LEATHER_HAT.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.LEATHER_SHIRT.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.LEATHER_GLOVES.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.LEATHER_PANTS.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.LEATHER_BOOTS.generate(1f));
		assertEquals(9, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.HEAD_EQUIPMENT, Item.STEEL_HELMET.generate(1f));
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.STEEL_CUIRASS.generate(1f));
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.STEEL_GAUNTLETS.generate(1f));
		performer.setProperty(Constants.LEGS_EQUIPMENT, Item.STEEL_GREAVES.generate(1f));
		performer.setProperty(Constants.FEET_EQUIPMENT, Item.STEEL_BOOTS.generate(1f));
		assertEquals(15, ArmorPropertyUtils.calculateDamageResist(performer));
		
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, Item.STEEL_SHIELD.generate(1f));
		assertEquals(16, ArmorPropertyUtils.calculateDamageResist(performer));
	}
	
	@Test
	public void testCalculateDamageResistForTwoHandedWeapon() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		assertEquals(0, ArmorPropertyUtils.calculateDamageResist(performer));
		
		WorldObject ironClaymore = Item.IRON_CLAYMORE.generate(1f);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, ironClaymore);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, ironClaymore);
		
		assertEquals(0, ArmorPropertyUtils.calculateDamageResist(performer));
	}
	
	@Test
	public void testGetSkillBonus() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		assertEquals(1.0, ArmorPropertyUtils.getSkillBonus(performer, Item.IRON_CUIRASS.generate(1f)), 0.1);
		assertEquals(1.0, ArmorPropertyUtils.getSkillBonus(performer, Item.COTTON_SHIRT.generate(1f)), 0.1);
	}
}
