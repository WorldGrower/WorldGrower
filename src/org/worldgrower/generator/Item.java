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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.ArmorType;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.conversation.KnowledgeToDescriptionMapper;
import org.worldgrower.gui.ImageIds;

public enum Item {
	
	IRON_CLAYMORE(ItemType.WEAPON), 
	IRON_GREATSWORD(ItemType.WEAPON), 
	IRON_AXE(ItemType.WEAPON), 
	IRON_GREATAXE(ItemType.WEAPON), 
	IRON_CUIRASS(ItemType.ARMOR), 
	IRON_HELMET(ItemType.ARMOR), 
	IRON_GAUNTLETS(ItemType.ARMOR), 
	IRON_GREAVES(ItemType.ARMOR), 
	IRON_SHIELD(ItemType.ARMOR), 
	IRON_BOOTS(ItemType.ARMOR), 
	BERRIES(ItemType.FOOD), 
	GRAPES(ItemType.INGREDIENT), 
	LONGBOW(ItemType.WEAPON), 
	PAPER(ItemType.RESOURCE), 
	WATER(ItemType.DRINK),
	WINE(ItemType.DRINK),
	MEAT(ItemType.FOOD), 
	SPELLBOOK(ItemType.BOOK), 
	KEY(ItemType.KEY), 
	NIGHT_SHADE(ItemType.INGREDIENT), 
	POISON(ItemType.DRINK), 
	COTTON(ItemType.RESOURCE), 
	COTTON_SHIRT(ItemType.ARMOR), 
	COTTON_HAT(ItemType.ARMOR), 
	COTTON_BOOTS(ItemType.ARMOR), 
	COTTON_GLOVES(ItemType.ARMOR), 
	COTTON_PANTS(ItemType.ARMOR), 
	BED(ItemType.MISC),
	OIL(ItemType.RESOURCE), 
	FISHING_POLE(ItemType.TOOL), 
	FISH(ItemType.FOOD), 
	REPAIR_HAMMER(ItemType.MISC), 
	WOOD(ItemType.RESOURCE), 
	STONE(ItemType.RESOURCE), 
	GOLD(ItemType.RESOURCE), 
	ORE(ItemType.RESOURCE), 
	SOUL_GEM(ItemType.RESOURCE), 
	NEWS_PAPER(ItemType.BOOK), 
	BLOOD(ItemType.DRINK), 
	IRON_MACE(ItemType.WEAPON), 
	IRON_KATAR(ItemType.WEAPON),
	SLEEPING_POTION(ItemType.DRINK),
	MINIATURE_CHEST(ItemType.MISC),
	REMAINS(ItemType.MISC),
	PICKAXE(ItemType.TOOL),
	SCYTHE(ItemType.TOOL),
	// fictional item types to set demands and prices
	SHACK(ItemType.MISC),
	HOUSE(ItemType.MISC),
	INN(ItemType.MISC),
	BREWERY(ItemType.MISC),
	SMITH(ItemType.MISC),
	WORKBENCH(ItemType.MISC),
	PAPERMILL(ItemType.MISC),
	WEAVERY(ItemType.MISC),
	APOTHECARY(ItemType.MISC),
	CHEST(ItemType.MISC),
	LOCKPICK(ItemType.TOOL),
	BUTCHER_KNIFE(ItemType.TOOL),
	HEALING_POTION(ItemType.DRINK)
	;

	public static final int COMBAT_MULTIPLIER = 10;
	
	private static final String NEWS_PAPER_NAME = "news paper";
	private static final String IRON_CLAYMORE_NAME = "iron claymore";
	private static final String IRON_AXE_NAME = "iron axe";
	private static final String IRON_GREATSWORD_NAME = "iron greatsword";
	private static final String IRON_GREATAXE_NAME = "iron greataxe";
	private static final String IRON_CUIRASS_NAME = "iron cuirass";
	private static final String IRON_HELMET_NAME = "iron helmet";
	private static final String IRON_GAUNTLETS_NAME = "iron gauntlets";
	private static final String IRON_GREAVES_NAME = "iron greaves";
	private static final String IRON_BOOTS_NAME = "iron boots";
	private static final String IRON_SHIELD_NAME = "iron shield";
	private static final String IRON_MACE_NAME = "iron mace";
	private static final String IRON_KATAR_NAME = "iron katar";
	
	private static final String LONGBOW_NAME = "longbow";
	private static final String POISON_NAME = "poison";

	private static final String COTTON_SHIRT_NAME = "cotton shirt";
	private static final String COTTON_HAT_NAME = "cotton hat";
	private static final String COTTON_BOOTS_NAME = "cotton boots";
	private static final String COTTON_GLOVES_NAME = "cotton gloves";
	private static final String COTTON_PANTS_NAME = "cotton pants";
	
	private static final String BED_NAME = "bed";
	
	private final ItemType itemType;
	
	private Item(ItemType itemType) {
		this.itemType = itemType;
	}
	
	private final static Map<Item, Function<Double, WorldObject>> ITEMS = new HashMap<>();
	private final static Map<Item, WorldObject> DEFAULT_WORLD_OBJECTS = new HashMap<>();
	
	private static void addItem(Item identifier, Function<Double, WorldObject> function) {
		ITEMS.put(identifier, function);
	}
	
	public WorldObject generate(double skillBonus) {
		WorldObject item = ITEMS.get(this).apply(skillBonus);
		item.setProperty(Constants.ITEM_ID, this);
		return item;
	}
	
	static {
		addItem(Item.IRON_CLAYMORE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_CLAYMORE_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_CLAYMORE);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.IRON_GREATSWORD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GREATSWORD_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (24 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 40);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.TWO_HANDED_WEAPON, Boolean.TRUE);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GREATSWORD);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_AXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_AXE_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.WOOD_CUTTING_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_AXE);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GREATAXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GREATAXE_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (24 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 40);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.TWO_HANDED_WEAPON, Boolean.TRUE);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GREATAXE);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_CUIRASS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_CUIRASS_NAME);
			properties.put(Constants.PRICE, 300);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (10 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 30);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_CUIRASS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_HELMET, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_HELMET_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (4 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 10);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_HELMET);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GAUNTLETS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GAUNTLETS_NAME);
			properties.put(Constants.PRICE, 150);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (5 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GAUNTLETS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GREAVES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GREAVES_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (2 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 8);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEGS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GREAVES);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_SHIELD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_SHIELD_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (10 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 10);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.RIGHT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_SHIELD);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_BOOTS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_BOOTS_NAME);
			properties.put(Constants.PRICE, 150);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (5 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_BOOTS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.BERRIES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "berries");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.FOOD, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.BERRY);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.GRAPES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "grapes");
			properties.put(Constants.GRAPE, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.GRAPES);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.WINE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "wine");
			properties.put(Constants.WINE, 1);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.ALCOHOL_LEVEL, (int) (5 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.WINE);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.LONGBOW, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LONGBOW_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (4 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.RANGE, 4);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LONGBOW);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.PAPER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "paper");
			properties.put(Constants.PAPER, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.PAPER);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.WATER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "water");
			properties.put(Constants.WATER, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.WATER);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.MEAT, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "meat");
			properties.put(Constants.FOOD, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.MEAT);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.SPELLBOOK, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "spellbook");
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.SPELL_BOOK);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.KEY, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "key");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.KEY);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.NIGHT_SHADE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "nightshade");
			properties.put(Constants.NIGHT_SHADE, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.NIGHT_SHADE);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.POISON, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, POISON_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.POISON_DAMAGE, (int) (4 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.POISON);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.COTTON, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "cotton");
			properties.put(Constants.COTTON, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.COTTON);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.COTTON_SHIRT, skillBonus -> {
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
		});
	
		addItem(Item.COTTON_HAT, skillBonus -> {
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
		});
	
		addItem(Item.COTTON_BOOTS, skillBonus -> {
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
		});
	
		addItem(Item.COTTON_GLOVES, skillBonus -> {
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
		});
	
		addItem(Item.COTTON_PANTS, skillBonus -> {
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
		});
	
		addItem(Item.BED, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, BED_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 5);
			properties.put(Constants.SLEEP_COMFORT, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.BED);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.OIL, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "oil");
			properties.put(Constants.OIL, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.OIL);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.FISHING_POLE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "fishing pole");
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.PIERCING);
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.FISHING_POLE_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.FISHING_POLE);
			return new WorldObjectImpl(properties);
		});

		addItem(Item.FISH, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "fish");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.FOOD, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.RAW_FISH);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.REPAIR_HAMMER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "smithing hammer");
			properties.put(Constants.PRICE, 10);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.BLUDGEONING);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.REPAIR_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.REPAIR_HAMMER);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.NEWS_PAPER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, NEWS_PAPER_NAME);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.NEWS_PAPER);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.BLOOD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "blood");
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.VAMPIRE_BLOOD_LEVEL, (int) (10 * skillBonus));
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.BLOOD);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.IRON_MACE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_MACE_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.BLUDGEONING);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_MACE);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.IRON_KATAR, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_KATAR_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.PIERCING);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_KATAR);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.SLEEPING_POTION, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "sleeping potion");
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.SLEEP_INDUCING_DRUG_STRENGTH, (int) (3 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.SLEEPING_POTION);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.MINIATURE_CHEST, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "miniature chest");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.CHEST);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.REMAINS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "remains");
			properties.put(Constants.PRICE, 0);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.SKELETAL_REMAINS);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.PICKAXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "pickaxe");
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.PIERCING);
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.PICKAXE_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.PICKAXE);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.SCYTHE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "scythe");
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.SCYTHE_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.SCYTHE);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.BUTCHER_KNIFE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "butcher knife");
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.BUTCHER_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.BUTCHER_KNIFE);
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.HEALING_POTION, skillBonus -> {
			int hitPointsRestored = (int)(4 * Item.COMBAT_MULTIPLIER * skillBonus);
			
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "healing potion");
			properties.put(Constants.HIT_POINTS_HEALED, hitPointsRestored);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.HEALING_POTION);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			return new WorldObjectImpl(properties);
		});
		
		for (BuildingType buildingType : BuildingType.values()) {
			Item buildingItem = mapBuildingTypeToItem(buildingType);
			addItem(buildingItem, skillBonus -> {
				Map<ManagedProperty<?>, Object> properties = new HashMap<>();
				properties.put(Constants.NAME, buildingType.getDescription());
				properties.put(Constants.PRICE, buildingType.getPrice());
				properties.put(Constants.SELLABLE, false);
				properties.put(buildingType.getQualityProperty(), 0);
				properties.put(Constants.IMAGE_ID, buildingType.getImageId());
				return new WorldObjectImpl(properties);
			});
		}
		
		addItem(Item.LOCKPICK, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "lockpick");
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.PIERCING);
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.LOCKPICK_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.LOCKPICK);
			return new WorldObjectImpl(properties);
		});
		
		
		addItem(Item.WOOD, new DefaultItemGenerator(Constants.WOOD, 1, ImageIds.WOOD)::addDefault);
		addItem(Item.STONE, new DefaultItemGenerator(Constants.STONE, 1, ImageIds.STONE)::addDefault);
		addItem(Item.GOLD, new DefaultItemGenerator(Constants.GOLD, 1, ImageIds.GOLD)::addDefault);
		addItem(Item.ORE, new DefaultItemGenerator(Constants.ORE, 1, ImageIds.IRON)::addDefault);
		addItem(Item.SOUL_GEM, new DefaultItemGenerator(Constants.SOUL_GEM, 1, ImageIds.SOUL_GEM)::addDefault);
	}
	
	static {
		for(Item item : Item.values()) {
			DEFAULT_WORLD_OBJECTS.put(item, item.generate(1f));
		}
	}
	
	private static class DefaultItemGenerator {
		private final IntProperty propertyKey;
		private final int quantity;
		private final ImageIds initialImageId;
		
		public DefaultItemGenerator(IntProperty propertyKey, int quantity, ImageIds initialImageId) {
			super();
			this.propertyKey = propertyKey;
			this.quantity = quantity;
			this.initialImageId = initialImageId;
		}

		private WorldObject addDefault(double skillBonus) {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.QUANTITY, quantity);
			properties.put(Constants.PRICE, 1);
			properties.put(propertyKey, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.NAME, propertyKey.getName());
			properties.put(Constants.IMAGE_ID, initialImageId);
			WorldObject worldObject = new WorldObjectImpl(properties);
			return worldObject;
		}
	}
	
	public static Item mapBuildingTypeToItem(BuildingType buildingType) {
		return Item.values()[Item.SHACK.ordinal() + buildingType.ordinal()];
	}
	
	public static WorldObject generateSpellBook(MagicSpell magicSpell) {
		WorldObject spellBook = Item.SPELLBOOK.generate(1f);
		spellBook.setProperty(Constants.MAGIC_SPELL, magicSpell);
		return spellBook;
	}
	
	public static WorldObject generateKey(int structureToLockId, World world) {
		WorldObject key = Item.KEY.generate(1f);
		WorldObject structureToLock = world.findWorldObjectById(structureToLockId);
		key.setProperty(Constants.NAME, getKeyName(structureToLock));
		key.setProperty(Constants.LOCK_ID, structureToLockId);
		return key;
	}
	
	public static String getKeyName(WorldObject structureToLock) {
		return "key to " + structureToLock.getProperty(Constants.NAME);
	}
	
	public static WorldObject generateNewsPaper(List<Knowledge> knowledgeList, int[] knowledgeIndices, World world) {
		WorldObject newsPaper = Item.NEWS_PAPER.generate(1f);
		String newsPaperText = generateNewsPaperText(knowledgeList, knowledgeIndices, world);
		newsPaper.setProperty(Constants.TEXT, newsPaperText.toString());
		newsPaper.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap(knowledgeList));
		return newsPaper;
	}

	private static String generateNewsPaperText(List<Knowledge> knowledgeList, int[] knowledgeIndices, World world) {
		StringBuilder builder = new StringBuilder();
		KnowledgeToDescriptionMapper mapper = new KnowledgeToDescriptionMapper();
		List<Integer> knowledgeInts = IntStream.of(knowledgeIndices).boxed().collect(Collectors.toList());
		for(int i=0; i<knowledgeList.size(); i++) {
			if (knowledgeInts.contains(i)) {
				Knowledge knowledge = knowledgeList.get(i);
				builder.append(mapper.getStatementDescription(knowledge, world)).append("\n");
			}
		}
		return builder.toString();
	}
	
	public static WorldObject generateMiniatureChest(WorldObject chest) {
		WorldObject miniatureChest = Item.MINIATURE_CHEST.generate(1f);
		miniatureChest.setProperty(Constants.CHEST_ID, chest.getProperty(Constants.ID));
		return miniatureChest;
	}
	
	public String getDescription() {
		return DEFAULT_WORLD_OBJECTS.get(this).getProperty(Constants.NAME);
	}
	
	public int getPrice() {
		return DEFAULT_WORLD_OBJECTS.get(this).getProperty(Constants.PRICE);
	}
	
	public ImageIds getImageId() {
		return DEFAULT_WORLD_OBJECTS.get(this).getProperty(Constants.IMAGE_ID);
	}
	
	public static Item value(int index) {
		return Item.values()[index];
	}

	public ItemType getItemType() {
		return itemType;
	}

	public static Item getItemFor(ManagedProperty<?> managedProperty) {
		for(Item item : Item.values()) {
			if (DEFAULT_WORLD_OBJECTS.get(item).hasProperty(managedProperty)) {
				return item;
			}
		}
		throw new IllegalStateException("Property " + managedProperty + " not found in items"); 
	}
}
