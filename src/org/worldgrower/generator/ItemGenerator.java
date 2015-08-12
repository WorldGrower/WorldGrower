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
package org.worldgrower.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.ArmorType;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.gui.ImageIds;

public class ItemGenerator {

	public static final String IRON_CLAYMORE_NAME = "Iron Claymore";
	public static final String IRON_CUIRASS_NAME = "Iron Cuirass";
	public static final String IRON_HELMET_NAME = "Iron Helmet";
	public static final String IRON_GAUNTLETS_NAME = "Iron Gauntlets";
	public static final String IRON_BOOTS_NAME = "Iron Gauntlets";
	
	public static final String LONGBOW_NAME = "Longbow";
	public static final String POISON_NAME = "Poison";

	public static final String COTTON_SHIRT_NAME = "Cotton Shirt";
	public static final String COTTON_HAT_NAME = "Cotton Hat";
	public static final String COTTON_BOOTS_NAME = "Cotton Boots";
	public static final String COTTON_GLOVES_NAME = "Cotton Gloves";
	public static final String COTTON_PANTS_NAME = "Cotton Pants";
	
	public static WorldObject getIronClaymore(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, IRON_CLAYMORE_NAME);
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.DAMAGE, (int) (12 * skillBonus));
		properties.put(Constants.WEIGHT, 22);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.IRON_CLAYMORE);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getIronCuirass(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, IRON_CUIRASS_NAME);
		properties.put(Constants.PRICE, 300);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (10 * skillBonus));
		properties.put(Constants.WEIGHT, 30);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.IRON_CUIRASS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getIronHelmet(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, IRON_HELMET_NAME);
		properties.put(Constants.PRICE, 100);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (4 * skillBonus));
		properties.put(Constants.WEIGHT, 10);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.IRON_HELMET);
		properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getIronGauntlets(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, IRON_GAUNTLETS_NAME);
		properties.put(Constants.PRICE, 150);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (5 * skillBonus));
		properties.put(Constants.WEIGHT, 12);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.IRON_GAUNTLETS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getIronBoots(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, IRON_BOOTS_NAME);
		properties.put(Constants.PRICE, 150);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (5 * skillBonus));
		properties.put(Constants.WEIGHT, 12);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.IRON_BOOTS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject generateBerries() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "berries");
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.BERRY);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject generateGrapes() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "grapes");
		properties.put(Constants.GRAPE, 1);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.GRAPES);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject generateWine(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "wine");
		properties.put(Constants.WINE, 1);
		properties.put(Constants.WATER, (int) (10 * skillBonus));
		properties.put(Constants.IMAGE_ID, ImageIds.WINE);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject getLongBow(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, LONGBOW_NAME);
		properties.put(Constants.PRICE, 100);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.DAMAGE, (int) (4 * skillBonus));
		properties.put(Constants.RANGE, 4);
		properties.put(Constants.WEIGHT, 22);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.LONGBOW);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject generatePaper() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "paper");
		properties.put(Constants.PAPER, 1);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.PAPER);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject generateWater() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "water");
		properties.put(Constants.WATER, 1);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.WATER);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject generateSpellBook(MagicSpell magicSpell) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "spellbook");
		properties.put(Constants.KNOWN_SPELLS, Arrays.asList(magicSpell));
		properties.put(Constants.PRICE, 100);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.SPELL_BOOK);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject generateKey(WorldObject structureToLock) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "key");
		properties.put(Constants.LOCK_ID, structureToLock.getProperty(Constants.ID));
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.KEY);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject generateNightShade() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "nightshade");
		properties.put(Constants.NIGHT_SHADE, 1);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.NIGHT_SHADE);
		return new WorldObjectImpl(properties);
	}

	public static WorldObject generatePoison(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, POISON_NAME);
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.POISON_DAMAGE, (int) (4 * skillBonus));
		properties.put(Constants.IMAGE_ID, ImageIds.POISON);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject generateCotton() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "cotton");
		properties.put(Constants.COTTON, 1);
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getCottonShirt(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, COTTON_SHIRT_NAME);
		properties.put(Constants.PRICE, 100);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (2 * skillBonus));
		properties.put(Constants.WEIGHT, 2);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_SHIRT);
		properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getCottonHat(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, COTTON_HAT_NAME);
		properties.put(Constants.PRICE, 30);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (1 * skillBonus));
		properties.put(Constants.WEIGHT, 1);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_HAT);
		properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getCottonBoots(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, COTTON_BOOTS_NAME);
		properties.put(Constants.PRICE, 40);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (1 * skillBonus));
		properties.put(Constants.WEIGHT, 1);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_BOOTS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getCottonGloves(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, COTTON_GLOVES_NAME);
		properties.put(Constants.PRICE, 30);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (1 * skillBonus));
		properties.put(Constants.WEIGHT, 1);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_ARMS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getCottonPants(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, COTTON_PANTS_NAME);
		properties.put(Constants.PRICE, 40);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.ARMOR, (int) (2 * skillBonus));
		properties.put(Constants.WEIGHT, 2);
		properties.put(Constants.EQUIPMENT_HEALTH, 1000);
		properties.put(Constants.EQUIPMENT_SLOT, Constants.LEGS_EQUIPMENT);
		properties.put(Constants.IMAGE_ID, ImageIds.COTTON_PANTS);
		properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
		return new WorldObjectImpl(properties);
	}
	
	public static WorldObject getBed(double skillBonus) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Bed");
		properties.put(Constants.PRICE, 50);
		properties.put(Constants.SELLABLE, false);
		properties.put(Constants.WEIGHT, 5);
		properties.put(Constants.SLEEP_COMFORT, (int)(2 * skillBonus));
		properties.put(Constants.IMAGE_ID, ImageIds.BED);
		return new WorldObjectImpl(properties);
	}
}
