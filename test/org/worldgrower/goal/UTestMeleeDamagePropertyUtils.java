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

public class UTestMeleeDamagePropertyUtils {

	@Test
	public void testCalculateMeleeDamage() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(2 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		assertEquals(12 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
		
		WorldObject twoHandedGreatsword = Item.IRON_GREATSWORD.generate(1f);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, twoHandedGreatsword);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, twoHandedGreatsword);
		assertEquals(24 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
	}
	
	@Test
	public void testCalculateMeleeDamageDualWielding() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(2 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		assertEquals(24 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
	}
	
	@Test
	public void testCalculateMeleeDamageIronGauntlets() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(2 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
		
		performer.setProperty(Constants.ARMS_EQUIPMENT, Item.IRON_GAUNTLETS.generate(1f));
		assertEquals(7 * Item.COMBAT_MULTIPLIER, MeleeDamagePropertyUtils.calculateMeleeDamage(performer));
	}
	
	@Test
	public void testSetTwoHandedWeapons() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		MeleeDamagePropertyUtils.setTwoHandedWeapons(performer, Constants.LEFT_HAND_EQUIPMENT);
		assertEquals(null, performer.getProperty(Constants.LEFT_HAND_EQUIPMENT));
		assertEquals(null, performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT));
	}
	
	@Test
	public void testSetTwoHandedWeaponsRemoveTwoHandedWeapon() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		WorldObject twoHandedGreatsword = Item.IRON_GREATSWORD.generate(1f);
		WorldObject oneHandedClaymore = Item.IRON_CLAYMORE.generate(1f);
		oneHandedClaymore.setProperty(Constants.ID, 3);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, oneHandedClaymore);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, twoHandedGreatsword);
		
		MeleeDamagePropertyUtils.setTwoHandedWeapons(performer, Constants.LEFT_HAND_EQUIPMENT);
		assertEquals(oneHandedClaymore, performer.getProperty(Constants.LEFT_HAND_EQUIPMENT));
		assertEquals(null, performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT));
	}
}
