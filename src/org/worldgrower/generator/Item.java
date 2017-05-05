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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
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
	IRON_MACE(ItemType.WEAPON), 
	IRON_KATAR(ItemType.WEAPON),
	IRON_DAGGER(ItemType.WEAPON),
	IRON_MORNINGSTAR(ItemType.WEAPON),
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
	REPAIR_HAMMER(ItemType.TOOL), 
	WOOD(ItemType.RESOURCE), 
	STONE(ItemType.RESOURCE), 
	GOLD(ItemType.RESOURCE), 
	ORE(ItemType.RESOURCE), 
	SOUL_GEM(ItemType.RESOURCE),
	FILLED_SOUL_GEM(ItemType.RESOURCE),
	NEWS_PAPER(ItemType.BOOK), 
	BLOOD(ItemType.DRINK), 
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
	HEALING_POTION(ItemType.DRINK),
	CURE_POISON_POTION(ItemType.DRINK),
	CURE_DISEASE_POTION(ItemType.DRINK),
	LEATHER(ItemType.RESOURCE),
	LEATHER_SHIRT(ItemType.ARMOR), 
	LEATHER_HAT(ItemType.ARMOR), 
	LEATHER_BOOTS(ItemType.ARMOR), 
	LEATHER_GLOVES(ItemType.ARMOR), 
	LEATHER_PANTS(ItemType.ARMOR), 
	STEEL(ItemType.RESOURCE),
	STEEL_CLAYMORE(ItemType.WEAPON), 
	STEEL_GREATSWORD(ItemType.WEAPON), 
	STEEL_AXE(ItemType.WEAPON), 
	STEEL_GREATAXE(ItemType.WEAPON), 
	STEEL_CUIRASS(ItemType.ARMOR), 
	STEEL_HELMET(ItemType.ARMOR), 
	STEEL_GAUNTLETS(ItemType.ARMOR), 
	STEEL_GREAVES(ItemType.ARMOR), 
	STEEL_SHIELD(ItemType.ARMOR), 
	STEEL_BOOTS(ItemType.ARMOR),
	STEEL_MACE(ItemType.WEAPON), 
	STEEL_KATAR(ItemType.WEAPON),
	STEEL_DAGGER(ItemType.WEAPON),
	STEEL_MORNINGSTAR(ItemType.WEAPON),
	SHORTBOW(ItemType.WEAPON),
	CHANGE_GENDER_POTION(ItemType.DRINK),
	REMOVE_CURSE_POTION(ItemType.DRINK),
	LAMP(ItemType.MISC),
	SAW(ItemType.TOOL)
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
	private static final String IRON_DAGGER_NAME = "iron dagger";
	private static final String IRON_MORNINGSTAR_NAME = "iron morningstar";
	
	private static final String LONGBOW_NAME = "longbow";
	private static final String SHORTBOW_NAME = "shortbow";
	private static final String POISON_NAME = "poison";

	private static final String COTTON_SHIRT_NAME = "cotton shirt";
	private static final String COTTON_HAT_NAME = "cotton hat";
	private static final String COTTON_BOOTS_NAME = "cotton boots";
	private static final String COTTON_GLOVES_NAME = "cotton gloves";
	private static final String COTTON_PANTS_NAME = "cotton pants";
	
	private static final String LEATHER_SHIRT_NAME = "leather shirt";
	private static final String LEATHER_HAT_NAME = "leather hat";
	private static final String LEATHER_BOOTS_NAME = "leather boots";
	private static final String LEATHER_GLOVES_NAME = "leather gloves";
	private static final String LEATHER_PANTS_NAME = "leather pants";
	
	private static final String BED_NAME = "bed";
	
	private static final String STEEL_CLAYMORE_NAME = "steel claymore";
	private static final String STEEL_AXE_NAME = "steel axe";
	private static final String STEEL_GREATSWORD_NAME = "steel greatsword";
	private static final String STEEL_GREATAXE_NAME = "steel greataxe";
	private static final String STEEL_CUIRASS_NAME = "steel cuirass";
	private static final String STEEL_HELMET_NAME = "steel helmet";
	private static final String STEEL_GAUNTLETS_NAME = "steel gauntlets";
	private static final String STEEL_GREAVES_NAME = "steel greaves";
	private static final String STEEL_BOOTS_NAME = "steel boots";
	private static final String STEEL_SHIELD_NAME = "steel shield";
	private static final String STEEL_MACE_NAME = "steel mace";
	private static final String STEEL_KATAR_NAME = "steel katar";
	private static final String STEEL_DAGGER_NAME = "steel dagger";
	private static final String STEEL_MORNINGSTAR_NAME = "steel morningstar";
	
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
	
	public void addToInventory(WorldObject performer, WorldObject target, SkillProperty skillProperty, IntProperty qualityProperty, World world) {
		double skillBonus = SkillUtils.useSkill(performer, skillProperty, world.getWorldStateChangedListeners());
		int quantity = target.getProperty(qualityProperty);
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		inventory.addQuantity(generate(skillBonus), quantity);
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron claymore is a one-handed melee weapon.");
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron greatsword is a two-handed melee weapon.");
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron axe is used for cutting wood and as a one-handed melee weapon.");
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron greataxe is a two-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_CUIRASS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_CUIRASS_NAME);
			properties.put(Constants.PRICE, 300);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (50 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 30);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_CUIRASS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "An iron cuirass is a piece of torso armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_HELMET, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_HELMET_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (30 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 10);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_HELMET);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "An iron helmet is a piece of head armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GAUNTLETS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GAUNTLETS_NAME);
			properties.put(Constants.PRICE, 150);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (30 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GAUNTLETS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "Iron gauntlets are a piece of arm armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GREAVES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GREAVES_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (30 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 8);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEGS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_GREAVES);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "Iron greaves are a piece of leg armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_SHIELD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_SHIELD_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (450 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 10);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.RIGHT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_SHIELD);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "An iron shield is a piece of armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_BOOTS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_BOOTS_NAME);
			properties.put(Constants.PRICE, 150);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (30 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_BOOTS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "Iron boots are a piece of feet armor.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.IRON_DAGGER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_DAGGER_NAME);
			properties.put(Constants.PRICE, 40);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (10 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 20);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_DAGGER);
			properties.put(Constants.LONG_DESCRIPTION, "An iron dagger is used as a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.IRON_MORNINGSTAR, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_MORNINGSTAR_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.BLUDGEONING);
			properties.put(Constants.WEIGHT, 25);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_MORNINGSTAR);
			properties.put(Constants.LONG_DESCRIPTION, "An iron morningstar is used as a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.BERRIES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "berries");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.FOOD, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.BERRY);
			properties.put(Constants.LONG_DESCRIPTION, "Berries can be harvested and eaten.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.GRAPES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "grapes");
			properties.put(Constants.GRAPE, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.GRAPES);
			properties.put(Constants.LONG_DESCRIPTION, "Grapes are used to produced wine.");
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
			properties.put(Constants.LONG_DESCRIPTION, "Drinking wine restores a character's water level and may make it intoxicated.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A longbow allows ranged attacks.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.PAPER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "paper");
			properties.put(Constants.PAPER, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.PAPER);
			properties.put(Constants.LONG_DESCRIPTION, "Paper is used for spellbooks and newspapers.");
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
			properties.put(Constants.LONG_DESCRIPTION, "Drinking water restores a character's water level.");
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
			properties.put(Constants.LONG_DESCRIPTION, "meat is used as food.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.SPELLBOOK, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "spellbook");
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.SPELL_BOOK);
			properties.put(Constants.LONG_DESCRIPTION, "a spellbook contains spells that can be learned.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.KEY, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "key");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.KEY);
			properties.put(Constants.LONG_DESCRIPTION, "a key is used to access a locked container.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.NIGHT_SHADE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "nightshade");
			properties.put(Constants.NIGHT_SHADE, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.NIGHT_SHADE);
			properties.put(Constants.LONG_DESCRIPTION, "nightshade is used to create poisons.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.POISON, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, POISON_NAME);
			properties.put(Constants.PRICE, 50);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.POISON_DAMAGE, (int) (4 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.POISON);
			properties.put(Constants.LONG_DESCRIPTION, "Drinking poison poisons a character, making it lose hit points for several turns.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.COTTON, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "cotton");
			properties.put(Constants.COTTON, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.COTTON);
			properties.put(Constants.LONG_DESCRIPTION, "cotton is used to create light armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A cotton shirt is a piece of torso armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A cotton hat is a piece of head armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "cotton boots are a piece of feet armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "cotton gloves are a piece of arm armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "cotton pants are a piece of leg armor.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A bed increases the energy that is restored when sleeping in a house that contains it.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.OIL, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "oil");
			properties.put(Constants.OIL, 1);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.OIL);
			properties.put(Constants.LONG_DESCRIPTION, "oil is used to make things flammable.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A fishing pole is used for fishing.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.FISH, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "fish");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.FOOD, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.IMAGE_ID, ImageIds.RAW_FISH);
			properties.put(Constants.LONG_DESCRIPTION, "fish are used as food.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A smithing hammer is used for smithing equipment.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.NEWS_PAPER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, NEWS_PAPER_NAME);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.NEWS_PAPER);
			properties.put(Constants.LONG_DESCRIPTION, "a newspaper transmits information when read.");
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
			properties.put(Constants.LONG_DESCRIPTION, "Drinking blood restores a vampire's blood level.");
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron mace is a one-handed melee weapon.");
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
			properties.put(Constants.LONG_DESCRIPTION, "An iron katar is a one-handed melee weapon.");
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
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a sleeping potion puts a character to sleep for several turns.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.MINIATURE_CHEST, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "miniature chest");
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.CHEST);
			properties.put(Constants.LONG_DESCRIPTION, "An miniature chest is used as a focus for the secret chest spell.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.REMAINS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "remains");
			properties.put(Constants.PRICE, 0);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.IMAGE_ID, ImageIds.SKELETAL_REMAINS);
			properties.put(Constants.LONG_DESCRIPTION, "Remains are all that remains of a dead character and can be buried.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A pickaxe is used for mining.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A scythe is used for harvesting food.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A butcher knife is used for butchering cattle.");
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
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a healing potion restores hit points.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.CURE_POISON_POTION, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "cure poison potion");
			properties.put(Constants.CURE_POISON, Boolean.TRUE);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.CURE_POISON_POTION);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a cure poison potion cures a character from being poisoned.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.CURE_DISEASE_POTION, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "cure disease potion");
			properties.put(Constants.CURE_DISEASE, Boolean.TRUE);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.CURE_DISEASE_POTION);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a cure disease potion cures a character from all diseases.");
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
			properties.put(Constants.LONG_DESCRIPTION, "A lockpick is used for opening locked containers.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.LEATHER_SHIRT, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LEATHER_SHIRT_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (40 * skillBonus));
			properties.put(Constants.WEIGHT, 3);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LEATHER_SHIRT);
			properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
			properties.put(Constants.LONG_DESCRIPTION, "A leather shirt is a piece of torso armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.LEATHER_HAT, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LEATHER_HAT_NAME);
			properties.put(Constants.PRICE, 30);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (15 * skillBonus));
			properties.put(Constants.WEIGHT, 2);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LEATHER_HAT);
			properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
			properties.put(Constants.LONG_DESCRIPTION, "A leather hat is a piece of head armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.LEATHER_BOOTS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LEATHER_BOOTS_NAME);
			properties.put(Constants.PRICE, 40);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (15 * skillBonus));
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LEATHER_BOOTS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
			properties.put(Constants.LONG_DESCRIPTION, "leather boots are a piece of feet armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.LEATHER_GLOVES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LEATHER_GLOVES_NAME);
			properties.put(Constants.PRICE, 30);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (15 * skillBonus));
			properties.put(Constants.WEIGHT, 1);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LEATHER_ARMS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
			properties.put(Constants.LONG_DESCRIPTION, "leather gloves are a piece of arm armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.LEATHER_PANTS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, LEATHER_PANTS_NAME);
			properties.put(Constants.PRICE, 40);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (15 * skillBonus));
			properties.put(Constants.WEIGHT, 2);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEGS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.LEATHER_PANTS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.LIGHT);
			properties.put(Constants.LONG_DESCRIPTION, "leather pants are a piece of leg armor.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.STEEL_CLAYMORE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_CLAYMORE_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (15 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 25);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_CLAYMORE);
			properties.put(Constants.LONG_DESCRIPTION, "A steel claymore is a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});

		addItem(Item.STEEL_GREATSWORD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_GREATSWORD_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (28 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 42);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.TWO_HANDED_WEAPON, Boolean.TRUE);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_GREATSWORD);
			properties.put(Constants.LONG_DESCRIPTION, "A steel greatsword is a two-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_AXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_AXE_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (15 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.WOOD_CUTTING_QUALITY, (int)(3 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_AXE);
			properties.put(Constants.LONG_DESCRIPTION, "A steel axe is used for cutting wood and as a melee weapon.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_GREATAXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_GREATAXE_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (28 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 40);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.TWO_HANDED_WEAPON, Boolean.TRUE);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_GREATAXE);
			properties.put(Constants.LONG_DESCRIPTION, "A steel greataxe is a two-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_CUIRASS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_CUIRASS_NAME);
			properties.put(Constants.PRICE, 400);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (980 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 32);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_CUIRASS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "A steel cuirass is a piece of torso armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_HELMET, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_HELMET_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (680 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_HELMET);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "A steel helmet is a piece of head armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_GAUNTLETS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_GAUNTLETS_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (680 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_GAUNTLETS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "steel gauntlets are a piece of arm armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_GREAVES, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_GREAVES_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (680 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 10);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEGS_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_GREAVES);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "steel greaves are a piece of leg armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_SHIELD, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_SHIELD_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (8000 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 12);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.RIGHT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_SHIELD);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "A steel shield is a piece of armor.");
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.STEEL_BOOTS, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_BOOTS_NAME);
			properties.put(Constants.PRICE, 200);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.ARMOR, (int) (680 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.WEIGHT, 14);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_BOOTS);
			properties.put(Constants.ARMOR_TYPE, ArmorType.HEAVY);
			properties.put(Constants.LONG_DESCRIPTION, "steel boots are a piece of feet armor.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.STEEL_MACE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_MACE_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (15 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.BLUDGEONING);
			properties.put(Constants.WEIGHT, 24);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_MACE);
			properties.put(Constants.LONG_DESCRIPTION, "A steel mace is a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.STEEL_KATAR, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_KATAR_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (15 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.PIERCING);
			properties.put(Constants.WEIGHT, 24);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_KATAR);
			properties.put(Constants.LONG_DESCRIPTION, "A steel katar is a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.STEEL_DAGGER, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_DAGGER_NAME);
			properties.put(Constants.PRICE, 80);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (12 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 20);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_DAGGER);
			properties.put(Constants.LONG_DESCRIPTION, "A steel dagger is a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.STEEL_MORNINGSTAR, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, STEEL_MORNINGSTAR_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (15 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.WEIGHT, 24);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.STEEL_MORNINGSTAR);
			properties.put(Constants.LONG_DESCRIPTION, "A steel morningstar is a one-handed melee weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.SHORTBOW, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, SHORTBOW_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (5 * COMBAT_MULTIPLIER * skillBonus));
			properties.put(Constants.RANGE, 3);
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.SHORT_BOW);
			properties.put(Constants.LONG_DESCRIPTION, "A shortbow is a ranged weapon.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.CHANGE_GENDER_POTION, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "change gender potion");
			properties.put(Constants.CHANGE_GENDER, Boolean.TRUE);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.CHANGE_GENDER_POTION);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a change gender potion changes a person's gender.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.REMOVE_CURSE_POTION, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "remove curse potion");
			properties.put(Constants.REMOVE_CURSE, Boolean.TRUE);
			properties.put(Constants.WATER, (int) (10 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.REMOVE_CURSE_POTION);
			properties.put(Constants.PRICE, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.LONG_DESCRIPTION, "Drinking a remove curse potion removes a character's curse.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.LAMP, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "lamp");
			properties.put(Constants.LIGHT_SOURCE, Boolean.TRUE);
			properties.put(Constants.IMAGE_ID, ImageIds.LAMP);
			properties.put(Constants.PRICE, 20);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.LONG_DESCRIPTION, "Equipping a lamp generates light so that a character can look further into the dark.");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.SAW, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, "saw");
			properties.put(Constants.PRICE, 10);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 2);
			properties.put(Constants.DAMAGE, 1 * COMBAT_MULTIPLIER);
			properties.put(Constants.DAMAGE_TYPE, DamageType.SLASHING);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.SAW_QUALITY, (int)(2 * skillBonus));
			properties.put(Constants.IMAGE_ID, ImageIds.SAW);
			properties.put(Constants.LONG_DESCRIPTION, "A saw is used by carpenters for building buildings");
			return new WorldObjectImpl(properties);
		});
		
		addItem(Item.WOOD, new DefaultItemGenerator(Constants.WOOD, 1, ImageIds.WOOD,  "Wood is cut from trees and is used for building construction")::addDefault);
		addItem(Item.STONE, new DefaultItemGenerator(Constants.STONE, 1, ImageIds.STONE, "Stone is mined and is used for building construction")::addDefault);
		addItem(Item.GOLD, new DefaultItemGenerator(Constants.GOLD, 1, ImageIds.GOLD, "Gold is used to create gold coins")::addDefault);
		addItem(Item.ORE, new DefaultItemGenerator(Constants.ORE, 1, ImageIds.IRON, "Iron is used to create tools, weapons and armor")::addDefault);
		addItem(Item.SOUL_GEM, new DefaultItemGenerator(Constants.SOUL_GEM, 1, ImageIds.SOUL_GEM, "Soulgems are mined and are used by necromancy spells")::addDefault);
		addItem(Item.FILLED_SOUL_GEM, new DefaultItemGenerator(Constants.FILLED_SOUL_GEM, 1, ImageIds.FILLED_SOUL_GEM, "Filled soulgems are used by necromancy spells")::addDefault);
		addItem(Item.LEATHER, new DefaultItemGenerator(Constants.LEATHER, 1, ImageIds.LEATHER, "Leather is obtained when butchering cattle and is used for creating light armor")::addDefault);
		addItem(Item.STEEL, new DefaultItemGenerator(Constants.STEEL, 1, ImageIds.STEEL, "Steel is obtained by using iron ore, and is used to create weapons and armor")::addDefault);
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
		private final String longDescription;
		
		public DefaultItemGenerator(IntProperty propertyKey, int quantity, ImageIds initialImageId, String longDescription) {
			super();
			this.propertyKey = propertyKey;
			this.quantity = quantity;
			this.initialImageId = initialImageId;
			this.longDescription = longDescription;
		}

		private WorldObject addDefault(double skillBonus) {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.QUANTITY, quantity);
			properties.put(Constants.PRICE, 1);
			properties.put(propertyKey, 1);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.NAME, propertyKey.getName());
			properties.put(Constants.IMAGE_ID, initialImageId);
			properties.put(Constants.LONG_DESCRIPTION, longDescription);
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
		newsPaper.setProperty(Constants.TEXT, newsPaperText);
		newsPaper.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap(knowledgeList));
		return newsPaper;
	}

	private static String generateNewsPaperText(List<Knowledge> knowledgeList, int[] knowledgeIndices, World world) {
		StringBuilder builder = new StringBuilder();
		KnowledgeToDescriptionMapper mapper = new KnowledgeToDescriptionMapper();
		List<Integer> knowledgeInts = IntStream.of(knowledgeIndices).boxed().collect(Collectors.toList());
		for(int i=0; i<knowledgeList.size(); i++) {
			Knowledge knowledge = knowledgeList.get(i);
			if (knowledgeInts.contains(knowledge.getId())) {
				builder.append(mapper.getStatementDescription(knowledge, world)).append(". ");
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
	
	public String getLongDescription() {
		return DEFAULT_WORLD_OBJECTS.get(this).getProperty(Constants.LONG_DESCRIPTION);
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
	
	public static boolean isClothesEquipment(WorldObject worldObject) {
		Item item = worldObject.getProperty(Constants.ITEM_ID);
		return item == Item.COTTON_BOOTS || item == Item.COTTON_GLOVES || item == Item.COTTON_HAT || item == Item.COTTON_PANTS || item == Item.COTTON_SHIRT;
	}
	
	public static boolean isLeatherEquipment(WorldObject worldObject) {
		Item item = worldObject.getProperty(Constants.ITEM_ID);
		return item == Item.LEATHER_BOOTS || item == Item.LEATHER_GLOVES || item == Item.LEATHER_HAT || item == Item.LEATHER_PANTS || item == Item.LEATHER_SHIRT;
	}
	
	public static Item[] getSortedValues() {
		Item[] values = values();
		Comparator<Item> descriptionComparator = (Item o1, Item o2) -> o1.getDescription().compareTo(o2.getDescription());
		Arrays.sort(values, descriptionComparator);
		return values;
	}
	
	public static List<Item> getItems(ItemType... itemTypes) {
		List<ItemType> itemTypesList = Arrays.asList(itemTypes);
		List<Item> resourceItems = new ArrayList<>();
		for (Item item : values()) {
			if (itemTypesList.contains(item.getItemType())) {
				resourceItems.add(item);
			}
		}
		return resourceItems;
	}
	
	public static List<Item> getItems(ItemType itemType1, ItemType itemType2) {
		List<Item> resourceItems = new ArrayList<>();
		for (Item item : values()) {
			if (item.getItemType() == itemType1 || item.getItemType() == itemType2) {
				resourceItems.add(item);
			}
		}
		return resourceItems;
	}
	
	public static List<Item> getItems(ItemType itemType) {
		List<Item> resourceItems = new ArrayList<>();
		for (Item item : values()) {
			if (item.getItemType() == itemType) {
				resourceItems.add(item);
			}
		}
		return resourceItems;
	}
}
