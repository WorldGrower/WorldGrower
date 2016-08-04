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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.worldgrower.CommonerNameGenerator;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.AttributeGenerator;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.ReasonsImpl;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.ArmorPropertyUtils;
import org.worldgrower.goal.EnergyPropertyUtils;
import org.worldgrower.goal.HitPointPropertyUtils;
import org.worldgrower.goal.MeleeDamagePropertyUtils;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.start.CharacterAttributes;
import org.worldgrower.personality.Personality;
import org.worldgrower.profession.PlayerCharacterProfession;

public class CommonerGenerator implements Serializable {

	private final Random random;
	private final CommonerImageIds commonerImageIds;
	private final CommonerNameGenerator commonerNameGenerator;
	
	public CommonerGenerator(int seed, CommonerImageIds commonerImageIds, CommonerNameGenerator commonerNameGenerator) {
		this.random = new Random(seed);
		this.commonerImageIds = commonerImageIds;
		this.commonerNameGenerator = commonerNameGenerator;
	}

	public int generateCursedCommoner(int x, int y, World world, WorldObject organization) {
		int id = generateCommoner(x, y, world, organization);
		WorldObject cursedCommoner = world.findWorldObjectById(id);
		cursedCommoner.setProperty(Constants.CURSE, Curse.TOAD_CURSE);
		
		return id;
	}
	
	public int generateCommoner(int x, int y, World world, WorldObject organization) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		final ImageIds imageId;
		final String gender;
		final String name;
		if (random.nextFloat() > 0.5f) {
			imageId = commonerImageIds.getNextFemaleCommonerImageId();
			gender = "female";
			name = commonerNameGenerator.getNextFemaleCommonerName();
		} else {
			imageId = commonerImageIds.getNextMaleCommonerImageId();
			gender = "male";
			name = commonerNameGenerator.getNextMaleCommonerName();
		}
		
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, name);
		properties.put(Constants.LEVEL, 1);
		
		new AttributeGenerator(random).addCommonerAttributes(properties);
		
		SkillUtils.addAllSkills(properties);
		HitPointPropertyUtils.addHitPointProperties(properties);
		properties.put(Constants.KNOWN_SPELLS, new ArrayList<>());
		properties.put(Constants.STUDYING_SPELLS, new PropertyCountMap<MagicSpell>());
		
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, imageId);
		properties.put(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
		properties.put(Constants.FOOD, 800);
		properties.put(Constants.WATER, 800);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.PROFESSION, null);
		properties.put(Constants.INVENTORY, new WorldObjectContainer());
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.RELATIONSHIPS, new IdRelationshipMap());
		properties.put(Constants.REASONS, new ReasonsImpl());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 500);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.BACKGROUND, new BackgroundImpl());
		properties.put(Constants.GOLD, 100);
		properties.put(Constants.ORGANIZATION_GOLD, 0);
		properties.put(Constants.PRICES, new Prices());
		properties.put(Constants.BUILDINGS, new BuildingList());
		properties.put(Constants.GROUP, new IdList().add(organization));
		properties.put(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		properties.put(Constants.ARENA_IDS, new IdList());
		properties.put(Constants.ARENA_FIGHTER_IDS, new IdList());
		properties.put(Constants.WORSHIP_COUNTER, 0);
		properties.put(Constants.ALCOHOL_LEVEL, 0);
		properties.put(Constants.ITEMS_SOLD, new ItemCountMap());
		Personality personality = new Personality();
		properties.put(Constants.PERSONALITY, personality);
		
		properties.put(Constants.DAMAGE, 8);
		addEquipmentProperties(properties);
		properties.put(Constants.ARMOR, 10);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject creature = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new CommonerOnTurn(this, organization), new CommonerWorldEvaluationFunction());
		world.addWorldObject(creature);
		
		creature.setProperty(Constants.ENERGY, EnergyPropertyUtils.calculateEnergyMax(creature));
		creature.setProperty(Constants.DAMAGE, MeleeDamagePropertyUtils.calculateMeleeDamage(creature));
		creature.setProperty(Constants.ARMOR, ArmorPropertyUtils.calculateArmor(creature));
		creature.setProperty(Constants.DAMAGE_RESIST, ArmorPropertyUtils.calculateDamageResist(creature));
		personality.initialize(creature);
		
		return id;
	}
	
	public static WorldObject createPlayerCharacter(int id, String playerName, String playerProfession, String gender, World world, CommonerGenerator commonerGenerator, WorldObject organization, CharacterAttributes characterAttributes, ImageIds playerCharacterImageId) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		
		properties.put(Constants.X, 5);
		properties.put(Constants.Y, 5);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, playerName);
		properties.put(Constants.LEVEL, 1);
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, playerCharacterImageId);
		properties.put(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
		properties.put(Constants.FOOD, 800);
		properties.put(Constants.WATER, 800);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.GROUP, new IdList().add(organization));
		
		WorldObjectContainer inventory = new WorldObjectContainer();
		inventory.addQuantity(Item.IRON_CLAYMORE.generate(1f));
		inventory.addQuantity(Item.IRON_GREATSWORD.generate(1f));
		inventory.addQuantity(Item.IRON_CUIRASS.generate(1f));
		inventory.addQuantity(Item.LONGBOW.generate(1f));
		properties.put(Constants.INVENTORY, inventory);
		properties.put(Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		properties.put(Constants.GOLD, 100);
		properties.put(Constants.ORGANIZATION_GOLD, 0);
		properties.put(Constants.PRICES, new Prices());
		
		properties.put(Constants.PROFESSION, new PlayerCharacterProfession(playerProfession));
		properties.put(Constants.RELATIONSHIPS, new IdRelationshipMap());
		properties.put(Constants.CHILDREN, new IdList());
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.GENDER, gender);
		properties.put(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.CONDITIONS, new Conditions());
		properties.put(Constants.BUILDINGS, new BuildingList());
		properties.put(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		properties.put(Constants.ARENA_IDS, new IdList());
		properties.put(Constants.ARENA_FIGHTER_IDS, new IdList());
		properties.put(Constants.WORSHIP_COUNTER, 0);
		properties.put(Constants.ALCOHOL_LEVEL, 0);
		properties.put(Constants.ITEMS_SOLD, new ItemCountMap());
		
		addEquipmentProperties(properties);
		properties.put(Constants.ARMOR, 0);
		
		properties.put(Constants.STRENGTH, characterAttributes.getStrength());
		properties.put(Constants.DEXTERITY, characterAttributes.getDexterity());
		properties.put(Constants.CONSTITUTION, characterAttributes.getConstitution());
		properties.put(Constants.INTELLIGENCE, characterAttributes.getIntelligence());
		properties.put(Constants.WISDOM, characterAttributes.getWisdom());
		properties.put(Constants.CHARISMA, characterAttributes.getCharisma());
		
		SkillUtils.addAllSkills(properties);
		HitPointPropertyUtils.addHitPointProperties(properties);
		properties.put(Constants.KNOWN_SPELLS, new ArrayList<>());
		properties.put(Constants.STUDYING_SPELLS, new PropertyCountMap<ManagedOperation>());

		properties.put(Constants.DAMAGE, 2);
		properties.put(Constants.DAMAGE_RESIST, 10);

		if (Boolean.getBoolean("DEBUG")) {
			((List<Object>)properties.get(Constants.KNOWN_SPELLS)).addAll(Actions.getMagicSpells());
		}
		
		final WorldObject playerCharacter = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, new CommonerOnTurn(commonerGenerator, organization), null);
		
		playerCharacter.setProperty(Constants.ENERGY, EnergyPropertyUtils.calculateEnergyMax(playerCharacter));
		playerCharacter.setProperty(Constants.DAMAGE, MeleeDamagePropertyUtils.calculateMeleeDamage(playerCharacter));
		playerCharacter.setProperty(Constants.ARMOR, ArmorPropertyUtils.calculateArmor(playerCharacter));
		playerCharacter.setProperty(Constants.DAMAGE_RESIST, ArmorPropertyUtils.calculateDamageResist(playerCharacter));
		
		return playerCharacter;
	}

	private static void addEquipmentProperties(Map<ManagedProperty<?>, Object> properties) {
		properties.put(Constants.HEAD_EQUIPMENT, null);
		properties.put(Constants.TORSO_EQUIPMENT, null);
		properties.put(Constants.ARMS_EQUIPMENT, null);
		properties.put(Constants.LEGS_EQUIPMENT, null);
		properties.put(Constants.FEET_EQUIPMENT, null);
		properties.put(Constants.LEFT_HAND_EQUIPMENT, null);
		properties.put(Constants.RIGHT_HAND_EQUIPMENT, null);
	}
	
	public static int generateSkeletalRemains(WorldObject originalWorldObject, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		int id = world.generateUniqueId();
		
		properties.put(Constants.X, originalWorldObject.getProperty(Constants.X));
		properties.put(Constants.Y, originalWorldObject.getProperty(Constants.Y));
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.HIT_POINTS, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.HIT_POINTS_MAX, 15 * Item.COMBAT_MULTIPLIER);
		properties.put(Constants.NAME, "skeletal remains of " + originalWorldObject.getProperty(Constants.NAME));
		properties.put(Constants.ARMOR, 10);
		properties.put(Constants.DECEASED_WORLD_OBJECT, Boolean.TRUE);
		properties.put(Constants.DEATH_REASON, originalWorldObject.getProperty(Constants.DEATH_REASON));
		properties.put(Constants.PRICE, 1);
		properties.put(Constants.WEIGHT, 1);//TODO: calculate weight?
		properties.put(Constants.SELLABLE, false);
		
		properties.put(Constants.ID, id);
		properties.put(Constants.IMAGE_ID, ImageIds.SKELETAL_REMAINS);
		properties.put(Constants.LOOK_DIRECTION, null);
		properties.put(Constants.INVENTORY, originalWorldObject.getProperty(Constants.INVENTORY));
		Integer organizationGold = originalWorldObject.getProperty(Constants.ORGANIZATION_GOLD);
		properties.put(Constants.GOLD, originalWorldObject.getProperty(Constants.GOLD) + (organizationGold != null ? organizationGold.intValue() : 0));
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject creature = new WorldObjectImpl(properties);
		world.addWorldObject(creature);
		
		return id;
	}
}
