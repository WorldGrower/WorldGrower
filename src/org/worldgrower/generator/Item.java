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
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.Knowledge;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.conversation.KnowledgeToDescriptionMapper;
import org.worldgrower.gui.ImageIds;

public enum Item {
	IRON_CLAYMORE, IRON_GREATSWORD, IRON_AXE, IRON_GREATAXE, IRON_CUIRASS, IRON_HELMET, IRON_GAUNTLETS, IRON_GREAVES, IRON_SHIELD, IRON_BOOTS, BERRIES, GRAPES, WINE, LONGBOW, PAPER, WATER, MEAT, SPELLBOOK, KEY, NIGHT_SHADE, POISON, COTTON, COTTON_SHIRT, COTTON_HAT, COTTON_BOOTS, COTTON_GLOVES, COTTON_PANTS, BED, OIL, FISHING_POLE, FISH, REPAIR_HAMMER, WOOD, STONE, GOLD, ORE, SOUL_GEM, NEWS_PAPER;

	public static final String NEWS_PAPER_NAME = "news paper";
	private static final String IRON_CLAYMORE_NAME = "Iron Claymore";
	private static final String IRON_AXE_NAME = "Iron Axe";
	private static final String IRON_GREATSWORD_NAME = "Iron Greatsword";
	private static final String IRON_GREATAXE_NAME = "Iron Greataxe";
	private static final String IRON_CUIRASS_NAME = "Iron Cuirass";
	private static final String IRON_HELMET_NAME = "Iron Helmet";
	private static final String IRON_GAUNTLETS_NAME = "Iron Gauntlets";
	private static final String IRON_GREAVES_NAME = "Iron Greaves";
	private static final String IRON_BOOTS_NAME = "Iron boots";
	private static final String IRON_SHIELD_NAME = "Iron Shield";
	
	private static final String LONGBOW_NAME = "Longbow";
	private static final String POISON_NAME = "Poison";

	//TODO: make name private
	public static final String COTTON_SHIRT_NAME = "Cotton Shirt";
	public static final String COTTON_HAT_NAME = "Cotton Hat";
	public static final String COTTON_BOOTS_NAME = "Cotton Boots";
	public static final String COTTON_GLOVES_NAME = "Cotton Gloves";
	public static final String COTTON_PANTS_NAME = "Cotton Pants";
	
	public static final String BED_NAME = "Bed";
	
	private final static Map<Item, Function<Double, WorldObject>> ITEMS = new HashMap<>();
	
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
			properties.put(Constants.DAMAGE, (int) (12 * skillBonus));
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
			properties.put(Constants.DAMAGE, (int) (24 * skillBonus));
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
			properties.put(Constants.DAMAGE, (int) (12 * skillBonus));
			properties.put(Constants.WEIGHT, 22);
			properties.put(Constants.EQUIPMENT_HEALTH, 1000);
			properties.put(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT);
			properties.put(Constants.IMAGE_ID, ImageIds.IRON_AXE);
			return new WorldObjectImpl(properties);
		});
	
		addItem(Item.IRON_GREATAXE, skillBonus -> {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();
			properties.put(Constants.NAME, IRON_GREATAXE_NAME);
			properties.put(Constants.PRICE, 100);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.DAMAGE, (int) (24 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (10 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (4 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (5 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (2 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (10 * skillBonus));
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
			properties.put(Constants.ARMOR, (int) (5 * skillBonus));
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
			properties.put(Constants.DAMAGE, (int) (4 * skillBonus));
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
			properties.put(Constants.POISON_DAMAGE, (int) (4 * skillBonus));
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
			properties.put(Constants.DAMAGE, 1);
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
			properties.put(Constants.NAME, "repair hammer");
			properties.put(Constants.PRICE, 10);
			properties.put(Constants.SELLABLE, false);
			properties.put(Constants.WEIGHT, 1);
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
		
		addItem(Item.WOOD, new DefaultItemGenerator(Constants.WOOD, 1, ImageIds.WOOD)::addDefault);
		addItem(Item.STONE, new DefaultItemGenerator(Constants.STONE, 1, ImageIds.STONE)::addDefault);
		addItem(Item.GOLD, new DefaultItemGenerator(Constants.GOLD, 1, ImageIds.GOLD)::addDefault);
		addItem(Item.ORE, new DefaultItemGenerator(Constants.ORE, 1, ImageIds.IRON)::addDefault);
		addItem(Item.SOUL_GEM, new DefaultItemGenerator(Constants.SOUL_GEM, 1, ImageIds.SOUL_GEM)::addDefault);
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
	
	
	
	public static WorldObject generateSpellBook(MagicSpell magicSpell) {
		WorldObject spellBook = Item.SPELLBOOK.generate(1f);
		spellBook.setProperty(Constants.MAGIC_SPELL, magicSpell);
		return spellBook;
	}
	
	public static WorldObject generateKey(int structureToLockId) {
		WorldObject key = Item.KEY.generate(1f);
		key.setProperty(Constants.LOCK_ID, structureToLockId);
		return key;
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
	
	public String getDescription() {
		return generate(1f).getProperty(Constants.NAME);
	}
	
	public int getPrice() {
		return generate(1f).getProperty(Constants.PRICE);
	}
	
	public static Item value(int index) {
		return Item.values()[index];
	}
}
