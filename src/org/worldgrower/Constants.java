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
package org.worldgrower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.attribute.BackgroundProperty;
import org.worldgrower.attribute.BooleanProperty;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.BuildingsListProperty;
import org.worldgrower.attribute.ConditionsProperty;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.attribute.IdContainer;
import org.worldgrower.attribute.IdListProperty;
import org.worldgrower.attribute.IdMapProperty;
import org.worldgrower.attribute.IdProperty;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.attribute.KnowledgeMapProperty;
import org.worldgrower.attribute.Location;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PersonalityProperty;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.PropertyCountMapProperty;
import org.worldgrower.attribute.Reasons;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.StringProperty;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainerProperty;
import org.worldgrower.attribute.WorldObjectProperty;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.deity.Deity;
import org.worldgrower.deity.DeityAttributes;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.TreeType;
import org.worldgrower.goal.Goal;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;

/**
 * The Constants class holds all properties constants, describing them with name, type and possible values.
 */
public class Constants {

	public static final List<ManagedProperty<?>> ALL_PROPERTIES = new ArrayList<>();
	
	private static final boolean NULLABLE = true;
	private static final boolean NOT_NULLABLE = false;
	
	public static final IntProperty X = new IntProperty("X", null, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty Y = new IntProperty("Y", null, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WIDTH = new IntProperty("WIDTH", 1, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty HEIGHT = new IntProperty("HEIGHT", 1, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ORIGINAL_WIDTH = new IntProperty("ORIGINAL_WIDTH", 1, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ORIGINAL_HEIGHT = new IntProperty("ORIGINAL_HEIGHT", 1, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty HIT_POINTS = new IntProperty("HP", 0, null, NOT_NULLABLE, ALL_PROPERTIES); // TODO: max = HPmax
	public static final IntProperty HIT_POINTS_MAX = new IntProperty("HPmax", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty LEVEL = new IntProperty("level", 1, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final StringProperty NAME = new StringProperty("NAME", NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<MetaInformation> META_INFORMATION = new UnCheckedProperty<>("metaInformation", ALL_PROPERTIES); 
	
	public static final IntProperty STRENGTH = new IntProperty("STR", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty DEXTERITY = new IntProperty("DEX", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty CONSTITUTION = new IntProperty("CON", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty INTELLIGENCE = new IntProperty("INT", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WISDOM = new IntProperty("WIS", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty CHARISMA = new IntProperty("CHA", 0, 20, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final SkillProperty BLUFF_SKILL = new SkillProperty("bluff", ALL_PROPERTIES, "Used to mislead people during a conversion");
	public static final SkillProperty INSIGHT_SKILL = new SkillProperty("insight", ALL_PROPERTIES, "Used to see through bluffs");
	public static final SkillProperty HAND_TO_HAND_SKILL = new SkillProperty("hand-to-hand", ALL_PROPERTIES, "Used with hand to hand combat");
	public static final SkillProperty ONE_HANDED_SKILL = new SkillProperty("one-handed", ALL_PROPERTIES, "Used when wielding one handed weapons");
	public static final SkillProperty TWO_HANDED_SKILL = new SkillProperty("two-handed", ALL_PROPERTIES, "Used when wielding two handed weapons");
	public static final SkillProperty PERCEPTION_SKILL = new SkillProperty("perception", ALL_PROPERTIES, "Used to spot hidden things and see through illusions");
	public static final SkillProperty DIPLOMACY_SKILL = new SkillProperty("diplomacy", ALL_PROPERTIES, "Used to influence people during a conversion");
	public static final SkillProperty INTIMIDATE_SKILL = new SkillProperty("intimidate", ALL_PROPERTIES, "Used to intimidate people during a conversion");
	public static final SkillProperty SMITHING_SKILL = new SkillProperty("smithing", ALL_PROPERTIES, "Used to craft weapons and equipment");
	public static final SkillProperty ALCHEMY_SKILL = new SkillProperty("alchemy", ALL_PROPERTIES, "Used to brew potions");
	public static final SkillProperty ARCHERY_SKILL = new SkillProperty("archery", ALL_PROPERTIES, "Used when wielding bows");
	public static final SkillProperty THIEVERY_SKILL = new SkillProperty("thievery", ALL_PROPERTIES, "Used when stealing items");
	public static final SkillProperty EVOCATION_SKILL = new SkillProperty("evocation", ALL_PROPERTIES, " Used with evocation magic: fire bolt, ray of frost, etc");
	public static final SkillProperty ILLUSION_SKILL = new SkillProperty("illusion", ALL_PROPERTIES, "Used to create illusions");
	public static final SkillProperty RESTORATION_SKILL = new SkillProperty("restoration", ALL_PROPERTIES, "Used with restoration magic like healing spells");
	public static final SkillProperty FARMING_SKILL = new SkillProperty("farming", ALL_PROPERTIES, "Used by farmers when tending to plants and food");
	public static final SkillProperty MINING_SKILL = new SkillProperty("mining", ALL_PROPERTIES, "Used by miners when mining for stone, ore or gold");
	public static final SkillProperty LUMBERING_SKILL = new SkillProperty("lumbering", ALL_PROPERTIES, "Used by lumberjacks when cutting down and processing trees");
	public static final SkillProperty WEAVING_SKILL = new SkillProperty("weaving", ALL_PROPERTIES, "Used to make light armor from cotton and harvestign cotton");
	public static final SkillProperty LIGHT_ARMOR_SKILL = new SkillProperty("light armor", ALL_PROPERTIES, "Improves armor rating of light armor");
	public static final SkillProperty HEAVY_ARMOR_SKILL = new SkillProperty("heavy armor", ALL_PROPERTIES, "Improves armor rating of heavy armor");
	public static final SkillProperty CARPENTRY_SKILL = new SkillProperty("carpentry", ALL_PROPERTIES, "Used when building houses or furniture");
	public static final SkillProperty TRANSMUTATION_SKILL = new SkillProperty("transmutation", ALL_PROPERTIES, "Used for transmutation magic like enlarge or reduce spells");
	public static final SkillProperty ENCHANTMENT_SKILL = new SkillProperty("enchantment", ALL_PROPERTIES, "Used for enchantment magic like sleep or paralyze spells");
	public static final SkillProperty NECROMANCY_SKILL = new SkillProperty("necromancy", ALL_PROPERTIES, "Used for necromancy magic like dealing necrotic damage or animate spells");
	public static final SkillProperty FISHING_SKILL = new SkillProperty("fishing", ALL_PROPERTIES, "Used by fishers when fishing");
	
	public static final UnCheckedProperty<CreatureType> CREATURE_TYPE = new UnCheckedProperty<CreatureType>("creatureType", ALL_PROPERTIES);
	
	public static final IntProperty FOOD_SOURCE = new IntProperty("foodSource",0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty FOOD = new IntProperty("food", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WATER = new IntProperty("water", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WATER_SOURCE = new IntProperty("waterSource", 0, 2000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WOOD_SOURCE = new IntProperty("woodSource", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty GRAPE_SOURCE = new IntProperty("grapeSource", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty GRAPE = new IntProperty("grapes", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WINE = new IntProperty("wine", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty PAPER = new IntProperty("paper", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty COTTON_SOURCE = new IntProperty("cottonSource", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty COTTON = new IntProperty("cotton", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty MEAT_SOURCE = new IntProperty("meat", 0, 25, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty FOOD_PRODUCED = new IntProperty("foodProduced",0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WOOD_PRODUCED = new IntProperty("woodProduced",0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty WOOD = new IntProperty("wood", 0, 50, NOT_NULLABLE, ALL_PROPERTIES);
	public static final BuildingsListProperty BUILDINGS = new BuildingsListProperty("buildings", ALL_PROPERTIES);
	public static final IntProperty ENERGY = new IntProperty("energy", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty PAPER_MILL_QUALITY = new IntProperty("papermill", 0, 50, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty LIBRARY_QUALITY = new IntProperty("libraryQuality", 0, 50, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WORKBENCH_QUALITY = new IntProperty("workbench", 0, 50, NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<BuildingType> BUILDING_TYPE = new UnCheckedProperty<>("buildingType", ALL_PROPERTIES);
	
	public static final IntProperty DAMAGE = new IntProperty("damage", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty RANGE = new IntProperty("range", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty DAMAGE_RESIST = new IntProperty("damageResist", 0, 800, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ARMOR = new IntProperty("armor", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WEIGHT = new IntProperty("weight", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty EQUIPMENT_HEALTH = new IntProperty("equipmentHealth", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty POISON_DAMAGE = new IntProperty("poison damage", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty REPAIR_QUALITY = new IntProperty("smithing tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty PICKAXE_QUALITY = new IntProperty("mining tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty SCYTHE_QUALITY = new IntProperty("farming tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WOOD_CUTTING_QUALITY = new IntProperty("wood cutting tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty LOCKPICK_QUALITY = new IntProperty("lockpicking tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty BUTCHER_QUALITY = new IntProperty("butchering tool", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty SAW_QUALITY = new IntProperty("saw", 0, null, NOT_NULLABLE, ALL_PROPERTIES);	
	
	public static final UnCheckedProperty<DamageType> DAMAGE_TYPE = new UnCheckedProperty<>("damageType", ALL_PROPERTIES);
	
	public static final UnCheckedProperty<WorldObject> HEAD_EQUIPMENT = new UnCheckedProperty<WorldObject>("headEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> TORSO_EQUIPMENT = new UnCheckedProperty<WorldObject>("torsoEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> ARMS_EQUIPMENT = new UnCheckedProperty<WorldObject>("armsEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> LEGS_EQUIPMENT = new UnCheckedProperty<WorldObject>("legsEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> FEET_EQUIPMENT = new UnCheckedProperty<WorldObject>("feetEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> LEFT_HAND_EQUIPMENT = new UnCheckedProperty<WorldObject>("leftHandEquipment", ALL_PROPERTIES);
	public static final UnCheckedProperty<WorldObject> RIGHT_HAND_EQUIPMENT = new UnCheckedProperty<WorldObject>("rightHandEquipment", ALL_PROPERTIES);
	
	public static final UnCheckedProperty<UnCheckedProperty<WorldObject>> EQUIPMENT_SLOT = new UnCheckedProperty<UnCheckedProperty<WorldObject>>("equipmentSlot", ALL_PROPERTIES);
	public static final BooleanProperty TWO_HANDED_WEAPON = new BooleanProperty("twoHandedWeapon", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ARMOR_TYPE = new IntProperty("armorType", 0, 1, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IdListProperty GROUP = new IdListProperty("group", ALL_PROPERTIES);
	public static final IdMapProperty RELATIONSHIPS = new IdMapProperty("relationships", ALL_PROPERTIES);
	public static final IdProperty MATE_ID = new IdProperty("mate", ALL_PROPERTIES);
	public static final IntProperty MATE_TURN = new IntProperty("mateTurn", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final IdListProperty CHILDREN = new IdListProperty("children", ALL_PROPERTIES);
	public static final IdProperty CHILD_BIRTH_ID = new IdProperty("childBirthId", ALL_PROPERTIES);
	public static final StringProperty GENDER = new StringProperty("gender", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty PREGNANCY = new IntProperty("pregnancy", null, null, NULLABLE, ALL_PROPERTIES);
	public static final IntProperty SOCIAL = new IntProperty("social", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final BackgroundProperty BACKGROUND = new BackgroundProperty("background", ALL_PROPERTIES);
	public static final IdProperty ORGANIZATION_LEADER_ID = new IdProperty("leader", ALL_PROPERTIES);
	public static final IntProperty ORGANIZATION_LEADER_START_TURN = new IntProperty("organizationLeaderStartTurn", null, null, NULLABLE, ALL_PROPERTIES);
	public static final IdListProperty ORGANIZATION_REBEL_IDS = new IdListProperty("rebelIds", ALL_PROPERTIES);
	public static final KnowledgeMapProperty KNOWLEDGE_MAP = new KnowledgeMapProperty("knowledgeMap", ALL_PROPERTIES);
	public static final UnCheckedProperty<Goal> ORGANIZATION_GOAL = new UnCheckedProperty<>("organizationGoal", ALL_PROPERTIES);
	public static final UnCheckedProperty<LegalActions> LEGAL_ACTIONS = new UnCheckedProperty<>("legalActions", ALL_PROPERTIES);
	public static final IdMapProperty BOUNTY = new IdMapProperty("bounty", ALL_PROPERTIES);
	
	public static final WorldObjectProperty FACADE = new WorldObjectProperty("facade", NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<Reasons> REASONS = new UnCheckedProperty<>("reasons", ALL_PROPERTIES);
	public static final StringProperty TEXT = new StringProperty("text", NOT_NULLABLE, ALL_PROPERTIES);

	public static final WorldObjectContainerProperty INVENTORY = new WorldObjectContainerProperty("inventory", ALL_PROPERTIES);
	public static final IntProperty PRICE = new IntProperty("price", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<Prices> PRICES = new UnCheckedProperty<Prices>("prices", ALL_PROPERTIES);
	public static final BooleanProperty SELLABLE = new BooleanProperty("sellable", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty QUANTITY = new IntProperty("quantity", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty GOLD = new IntProperty("gold", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final PropertyCountMapProperty<ManagedProperty<?>> DEMANDS = new PropertyCountMapProperty<ManagedProperty<?>>("demands", ALL_PROPERTIES);
	
	public static final UnCheckedProperty<Profession> PROFESSION = new UnCheckedProperty<Profession>("profession", ALL_PROPERTIES);

	public static final UnCheckedProperty<Deity> DEITY = new UnCheckedProperty<>("deity", ALL_PROPERTIES);
	public static final IdProperty PLACE_OF_WORSHIP_ID = new IdProperty("placeOfWorshipId", ALL_PROPERTIES);
	public static final BooleanProperty CAN_BE_WORSHIPPED = new BooleanProperty("canBeWorshipped", NOT_NULLABLE ,ALL_PROPERTIES);
	
	public static final IntProperty SLEEP_COMFORT = new IntProperty("sleep comfort", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty SMITH_QUALITY = new IntProperty("smithing", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty FISHING_POLE_QUALITY = new IntProperty("fishing tool", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty ID = new IntProperty("Id", null, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<ImageIds> IMAGE_ID = new UnCheckedProperty<>("ImageId", ALL_PROPERTIES);
	public static final UnCheckedProperty<LookDirection> LOOK_DIRECTION = new UnCheckedProperty<>("lookDirection", ALL_PROPERTIES);
	
	public static final IntProperty STONE = new IntProperty("stone", 0, 15, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ORE = new IntProperty("iron ore", 0, 15, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty OIL = new IntProperty("oil", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty SOUL_GEM = new IntProperty("soulgem", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty FILLED_SOUL_GEM = new IntProperty("filled soulgem", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty STEEL = new IntProperty("steel", 0, 15, NOT_NULLABLE, ALL_PROPERTIES);
	
	
	public static final IntProperty STONE_SOURCE = new IntProperty("stoneSource", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ORE_SOURCE = new IntProperty("oreSource", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty GOLD_SOURCE = new IntProperty("goldSource", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty OIL_SOURCE = new IntProperty("oilSource", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty SOUL_GEM_SOURCE = new IntProperty("soulGemSource", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final UnCheckedProperty<Curse> CURSE = new UnCheckedProperty<Curse>("curse", ALL_PROPERTIES);
	public static final ConditionsProperty CONDITIONS = new ConditionsProperty("conditions", ALL_PROPERTIES);
	public static final PersonalityProperty PERSONALITY = new PersonalityProperty("personality", ALL_PROPERTIES);
	
	public static final IntProperty RELATIONSHIP_VALUE = new IntProperty("relationshipValue", -1000, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IdProperty ILLUSION_CREATOR_ID = new IdProperty("illusionCreatorId", ALL_PROPERTIES);
	public static final IntProperty TURNS_TO_LIVE = new IntProperty("turnsToLive", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final UnCheckedProperty<List<ManagedOperation>> KNOWN_SPELLS = new UnCheckedProperty<>("knownSpells", ALL_PROPERTIES);
	public static final PropertyCountMapProperty<MagicSpell> STUDYING_SPELLS = new PropertyCountMapProperty<MagicSpell>("studyingSpells", ALL_PROPERTIES);
	public static final UnCheckedProperty<ManagedOperation> MAGIC_SPELL = new UnCheckedProperty<>("magicSpell", ALL_PROPERTIES);
	
	public static final BooleanProperty FLAMMABLE = new BooleanProperty("flammable", NOT_NULLABLE, ALL_PROPERTIES);

	public static final IntProperty LOCK_STRENGTH = new IntProperty("lockStrength", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty LOCKED = new BooleanProperty("locked", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdProperty LOCK_ID = new IdProperty("lockId", ALL_PROPERTIES);
	public static final IdProperty MAGIC_LOCK_CREATOR_ID = new IdProperty("magicLockCreatorId", ALL_PROPERTIES);
	public static final IntProperty TRAPPED_CONTAINER_DAMAGE = new IntProperty("trappedContainerDamage", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty NIGHT_SHADE_SOURCE = new IntProperty("nightShadeSource", 0, 500, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty NIGHT_SHADE = new IntProperty("nightshade", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty DECEASED_WORLD_OBJECT = new BooleanProperty("deceasedWorldObject", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdProperty CREATOR_ID = new IdProperty("creatorId", ALL_PROPERTIES);
	public static final UnCheckedProperty<Goal> GIVEN_ORDER = new UnCheckedProperty<>("givenOrder", ALL_PROPERTIES);
	public static final BooleanProperty MINION_ORGANIZATION = new BooleanProperty("minionOrganization", NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IdProperty ORGANIZATION_ID = new IdProperty("organizationId", ALL_PROPERTIES);
	public static final IntProperty TURN_COUNTER = new IntProperty("turnCounter", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdListProperty CANDIDATES = new IdListProperty("candidates", ALL_PROPERTIES);
	public static final IdMapProperty VOTES = new IdMapProperty("votes", ALL_PROPERTIES);
	
	public static final IntProperty SHACK_TAX_RATE = new IntProperty("shackTaxRate", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty HOUSE_TAX_RATE = new IntProperty("houseTaxRate", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ORGANIZATION_GOLD = new IntProperty("organizationGold", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdMapProperty TAXES_PAID_TURN = new IdMapProperty("taxesPaidTurn", ALL_PROPERTIES);
	public static final IdMapProperty PAY_CHECK_PAID_TURN = new IdMapProperty("payCheckPaidTurn", ALL_PROPERTIES);
	public static final BooleanProperty CAN_COLLECT_TAXES = new BooleanProperty("canCollectTaxes", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty CAN_ATTACK_CRIMINALS = new BooleanProperty("canAttackCriminals", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty PROFESSION_START_TURN = new IntProperty("professionStartTurn", null, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdMapProperty TURNS_IN_JAIL = new IdMapProperty("turnsInJail", ALL_PROPERTIES);
	public static final IntProperty SHERIFF_WAGE = new IntProperty("sheriffWage", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty TAX_COLLECTOR_WAGE = new IntProperty("taxCollectorWage", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final BooleanProperty ONLY_OWNERS_CAN_VOTE = new BooleanProperty("only owners can vote", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty ONLY_MALES_CAN_VOTE = new BooleanProperty("only males can vote", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty ONLY_FEMALES_CAN_VOTE = new BooleanProperty("only females can vote", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty ONLY_UNDEAD_CAN_VOTE = new BooleanProperty("only undead can vote", NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty VAMPIRE_BLOOD_LEVEL = new IntProperty("vampireBloodLevel", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	public static final StringProperty DEATH_REASON = new StringProperty("deathReason", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdProperty SACRIFICIAL_ALTAR_CREATOR_ID = new IdProperty("sacrificialAltarCreatorId", ALL_PROPERTIES);
	public static final IntProperty GHOUL_MEAT_LEVEL = new IntProperty("ghoulMeatLevel", 0, 1000, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IdProperty BRAWL_OPPONENT_ID = new IdProperty("brawlOpponentId", ALL_PROPERTIES);
	public static final IntProperty BRAWL_STAKE_GOLD = new IntProperty("brawlStakeGold", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final IdProperty DRINKING_CONTEST_OPPONENT_ID = new IdProperty("drinkingContestOpponentId", ALL_PROPERTIES);
	public static final IntProperty DRINKING_CONTEST_STAKE_GOLD = new IntProperty("drinkingContestStakeGold", 0, null, NULLABLE, ALL_PROPERTIES);
	
	public static final IdListProperty ARENA_IDS = new IdListProperty("ArenaIds", ALL_PROPERTIES);
	public static final IdListProperty ARENA_FIGHTER_IDS =  new IdListProperty("fighterForArenaOwnerId", ALL_PROPERTIES);
	public static final IdProperty ARENA_OPPONENT_ID = new IdProperty("arenaOpponentId", ALL_PROPERTIES);
	public static final IntProperty ARENA_PAY_CHECK_GOLD = new IntProperty("arenapayCheckGold", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final IntProperty ARENA_DONATED_TURN = new IntProperty("arenaDonatedTurn", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final IntProperty WORSHIP_COUNTER = new IntProperty("worshipCounter", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IntProperty ALCOHOL_LEVEL = new IntProperty("alcohol", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<Item> ITEM_ID = new UnCheckedProperty<>("itemId", ALL_PROPERTIES);
	public static final UnCheckedProperty<ItemCountMap> ITEMS_SOLD = new UnCheckedProperty<>("itemsSold", ALL_PROPERTIES);
	
	public static final IntProperty NEWSPAPER_READ_TURN = new IntProperty("newsPaperReadTurn", 0, null, NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty PASSABLE = new BooleanProperty("passable", NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<Location> LOCATION = new UnCheckedProperty<>("location", ALL_PROPERTIES);
	public static final IdProperty ASSASSINATE_TARGET_ID = new IdProperty("assassinateTargetId", ALL_PROPERTIES);
	public static final IntProperty SLEEP_INDUCING_DRUG_STRENGTH = new IntProperty("sleepInducingDrugStrength", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdProperty FEAR_CASTER_ID = new IdProperty("fearCasterId", ALL_PROPERTIES);
	public static final IntProperty WEAVERY_QUALITY = new IntProperty("weavery", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty BREWERY_QUALITY = new IntProperty("brewery", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty APOTHECARY_QUALITY = new IntProperty("apothecary", 0, 100, NOT_NULLABLE, ALL_PROPERTIES);
	
	public static final IdProperty LEASH_ID = new IdProperty("leashId", ALL_PROPERTIES);
	public static final IdProperty CATTLE_OWNER_ID = new IdProperty("cattleOwnerId", ALL_PROPERTIES);
	public static final BooleanProperty SECRET_CHEST = new BooleanProperty("secretChest", NOT_NULLABLE, ALL_PROPERTIES);
	public static final IdProperty CHEST_ID = new IdProperty("chestId", ALL_PROPERTIES);
	public static final IdListProperty ANIMAL_ENEMIES = new IdListProperty("animalEnemies", ALL_PROPERTIES);
	public static final IntProperty HIT_POINTS_HEALED = new IntProperty("hitPointsHealed", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty VOTING_CANDIDATE_TURNS = new IntProperty("votingCandidateTurns", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final IntProperty VOTING_TOTAL_TURNS = new IntProperty("votingTotalTurns", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty CURE_POISON = new BooleanProperty("curePoison", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty CURE_DISEASE = new BooleanProperty("cureDisease", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty REMOVE_CURSE = new BooleanProperty("removeCurse", NOT_NULLABLE, ALL_PROPERTIES);	
	public static final IntProperty LEATHER = new IntProperty("leather", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	public static final StringProperty LONG_DESCRIPTION = new StringProperty("longDescription", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty CHANGE_GENDER = new BooleanProperty("changeGender", NOT_NULLABLE, ALL_PROPERTIES);
	public static final BooleanProperty LIGHT_SOURCE = new BooleanProperty("lightSource", NOT_NULLABLE, ALL_PROPERTIES);
	public static final UnCheckedProperty<TreeType> TREE_TYPE = new UnCheckedProperty<>("treeType", ALL_PROPERTIES);
	public static final UnCheckedProperty<DeityAttributes> DEITY_ATTRIBUTES = new UnCheckedProperty<>("deityAttributes", ALL_PROPERTIES);
	
	//special property not used in WorldObject
	public static final IntProperty DISTANCE = new IntProperty("distance", 0, null, NOT_NULLABLE, ALL_PROPERTIES);
	
	private static final List<IdContainer> ID_PROPERTIES = calculateIdProperties();
	
	public static List<IdContainer> getIdProperties() {
		return ID_PROPERTIES;
	}
	
	private static List<IdContainer> calculateIdProperties() {
		List<IdContainer> result = new ArrayList<>();
		for(ManagedProperty<?> property : Constants.ALL_PROPERTIES) {
			if (property instanceof IdContainer) {
				result.add((IdContainer)property);
			}
		}
		return result;
	}
	
	public static boolean isTool(ManagedProperty<?> propertyKey) {
		return propertyKey == Constants.SCYTHE_QUALITY
				|| propertyKey == Constants.WOOD_CUTTING_QUALITY
				|| propertyKey == Constants.FISHING_POLE_QUALITY
				|| propertyKey == Constants.PICKAXE_QUALITY
				|| propertyKey == Constants.LOCKPICK_QUALITY
				|| propertyKey == Constants.REPAIR_QUALITY
				|| propertyKey == Constants.SAW_QUALITY;
	}
	
	public static List<ManagedProperty<?>> getToolProperties() {
		List<ManagedProperty<?>> toolProperties = new ArrayList<>();
		for(ManagedProperty<?> property : ALL_PROPERTIES) {
			if (isTool(property)) {
				toolProperties.add(property);
			}
		}
		
		return toolProperties;
	}
	
	private static final List<ManagedProperty<?>> POSSIBLE_DEMAND_PROPERTIES = 
		Arrays.asList(
		Constants.FOOD, 
		Constants.WATER,
		Constants.WOOD,
		Constants.STONE,
		Constants.ORE,
		Constants.COTTON,
		Constants.ALCOHOL_LEVEL,
		Constants.GOLD,
		Constants.GRAPE,
		Constants.OIL,
		Constants.POISON_DAMAGE,
		Constants.SOUL_GEM,
		Constants.FISHING_POLE_QUALITY,
		Constants.REPAIR_QUALITY,
		Constants.PICKAXE_QUALITY,
		Constants.SCYTHE_QUALITY,
		Constants.SAW_QUALITY,
		Constants.WOOD_CUTTING_QUALITY,
		Constants.LOCKPICK_QUALITY,
		Constants.SLEEP_COMFORT,
		Constants.BREWERY_QUALITY,
		Constants.SMITH_QUALITY,
		Constants.WORKBENCH_QUALITY,
		Constants.PAPER_MILL_QUALITY,
		Constants.WEAVERY_QUALITY,
		Constants.APOTHECARY_QUALITY,
		Constants.BUTCHER_QUALITY,
		Constants.HIT_POINTS_HEALED,
		Constants.LEATHER,
		Constants.STEEL
		);
	
	public static List<ManagedProperty<?>> getPossibleDemandProperties() {
		return POSSIBLE_DEMAND_PROPERTIES;
	}
}
