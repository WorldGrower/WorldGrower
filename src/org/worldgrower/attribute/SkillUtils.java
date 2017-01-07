/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.profession.Profession;

public class SkillUtils {

	public static void addAllSkills(Map<ManagedProperty<?>, Object> properties) {
		addSkill(Constants.BLUFF_SKILL, properties);
		addSkill(Constants.INSIGHT_SKILL, properties);
		addSkill(Constants.HAND_TO_HAND_SKILL, properties);
		addSkill(Constants.ONE_HANDED_SKILL, properties);
		addSkill(Constants.TWO_HANDED_SKILL, properties);
		addSkill(Constants.PERCEPTION_SKILL, properties);
		addSkill(Constants.DIPLOMACY_SKILL, properties);
		addSkill(Constants.INTIMIDATE_SKILL, properties);
		addSkill(Constants.SMITHING_SKILL, properties);
		addSkill(Constants.ALCHEMY_SKILL, properties);
		addSkill(Constants.ARCHERY_SKILL, properties);
		addSkill(Constants.THIEVERY_SKILL, properties);
		addSkill(Constants.EVOCATION_SKILL, properties);
		addSkill(Constants.ILLUSION_SKILL, properties);
		addSkill(Constants.RESTORATION_SKILL, properties);
		addSkill(Constants.FARMING_SKILL, properties);
		addSkill(Constants.MINING_SKILL, properties);
		addSkill(Constants.LUMBERING_SKILL, properties);
		addSkill(Constants.WEAVING_SKILL, properties);
		addSkill(Constants.LIGHT_ARMOR_SKILL, properties);
		addSkill(Constants.HEAVY_ARMOR_SKILL, properties);
		addSkill(Constants.CARPENTRY_SKILL, properties);
		addSkill(Constants.TRANSMUTATION_SKILL, properties);
		addSkill(Constants.ENCHANTMENT_SKILL, properties);
		addSkill(Constants.NECROMANCY_SKILL, properties);
		addSkill(Constants.FISHING_SKILL, properties);
	}
	
	private static final Map<SkillProperty, IntProperty> SKILLS_TO_ATTRIBUTE_MAP = new HashMap<>();
	
	static {
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.BLUFF_SKILL, Constants.CHARISMA);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.INSIGHT_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.HAND_TO_HAND_SKILL, Constants.STRENGTH);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.ONE_HANDED_SKILL, Constants.STRENGTH);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.TWO_HANDED_SKILL, Constants.STRENGTH);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.PERCEPTION_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.DIPLOMACY_SKILL, Constants.CHARISMA);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.INTIMIDATE_SKILL, Constants.CHARISMA);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.SMITHING_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.ALCHEMY_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.ARCHERY_SKILL, Constants.DEXTERITY);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.THIEVERY_SKILL, Constants.DEXTERITY);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.EVOCATION_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.ILLUSION_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.RESTORATION_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.FARMING_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.MINING_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.LUMBERING_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.WEAVING_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.LIGHT_ARMOR_SKILL, Constants.DEXTERITY);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.HEAVY_ARMOR_SKILL, Constants.STRENGTH);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.CARPENTRY_SKILL, Constants.WISDOM);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.TRANSMUTATION_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.ENCHANTMENT_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.NECROMANCY_SKILL, Constants.INTELLIGENCE);
		SKILLS_TO_ATTRIBUTE_MAP.put(Constants.FISHING_SKILL, Constants.WISDOM);
	}
	
	public static List<SkillProperty> getSkillsForAttribute(IntProperty attributeProperty) {
		List<SkillProperty> skillProperties = new ArrayList<>();
		for(Entry<SkillProperty, IntProperty> entry : SKILLS_TO_ATTRIBUTE_MAP.entrySet()) {
			if (entry.getValue() == attributeProperty) {
				skillProperties.add(entry.getKey());
			}
		}
		
		return skillProperties;
	}
	
	public static IntProperty getAttributeForSkill(SkillProperty skillProperty) {
		return SKILLS_TO_ATTRIBUTE_MAP.get(skillProperty);
	}
	
	public static List<IntProperty> getAttributes() {
		return Arrays.asList(Constants.STRENGTH, Constants.CONSTITUTION, Constants.DEXTERITY, Constants.INTELLIGENCE, Constants.WISDOM, Constants.CHARISMA);
	}
	
	public static List<SkillProperty> getSkills() {
		return new ArrayList<>(SKILLS_TO_ATTRIBUTE_MAP.keySet());
	}
	
	public static List<SkillProperty> getSortedSkills() {
		List<SkillProperty> skills = getSkills();
		skills.sort(new Comparator<SkillProperty>() {

			@Override
			public int compare(SkillProperty o1, SkillProperty o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		return skills;
	}
	
	private static void addSkill(SkillProperty skillProperty, Map<ManagedProperty<?>, Object> properties) {
		IntProperty attribute = SKILLS_TO_ATTRIBUTE_MAP.get(skillProperty);
		int attributeValue = (Integer) properties.get(attribute);
		properties.put(skillProperty, new Skill(attributeValue));
	}
	
	public static boolean canTargetTeachPerformer(WorldObject performer, WorldObject target) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		
		if (performerProfession != null) {
			SkillProperty skillProperty = performerProfession.getSkillProperty();
			if (skillProperty != null) {
				int performerSkillLevel = skillProperty.getLevel(performer);
				int targetSkillLevel = skillProperty.getLevel(target);
				
				return (performerSkillLevel + 5 < targetSkillLevel);
			}
		}
		return false;
	}
	
	public static void teachSkill(WorldObject performer, SkillProperty skillProperty, WorldStateChangedListeners worldStateChangedListeners) {
		for(int i=0; i<10; i++) {
			performer.getProperty(skillProperty).use(performer, skillProperty, worldStateChangedListeners);
		}
	}
	
	public static double useSkill(WorldObject performer, SkillProperty skill, WorldStateChangedListeners worldStateChangedListeners) {
		double result = getSkillBonus(performer, skill);
		performer.getProperty(skill).use(performer, skill, worldStateChangedListeners);
		return result;
	}

	public static int useSkillLevel(WorldObject performer, SkillProperty skillProperty, WorldStateChangedListeners worldStateChangedListeners) {
		Skill skill = performer.getProperty(skillProperty);
		int level = skillProperty.getLevel(performer);
		skill.use(performer, skillProperty, worldStateChangedListeners);
		return level;
	}
	
	public static double getSkillBonus(WorldObject performer, SkillProperty skill) {
		double result = 1.0f + (skill.getLevel(performer) / 50.0f);
		return result;
	}
	
	public static int getRealEnergyUse(WorldObject performer, SkillProperty skill, int energyUse) {
		return (int)(energyUse / (getSkillBonus(performer, skill)));
	}
	
	public static void useEnergy(WorldObject performer, SkillProperty skill, int energyUse, WorldStateChangedListeners worldStateChangedListeners) {
		performer.increment(Constants.ENERGY, -(int)(energyUse / useSkill(performer, skill, worldStateChangedListeners)));
	}
	
	public static boolean hasEnoughEnergy(WorldObject performer, SkillProperty skill, int energyUse) {
		if (performer.getProperty(Constants.ENERGY) >= getRealEnergyUse(performer, skill, energyUse)) {
			return true;
		} else {
			return false;
		}
	}

	public static void useSkill(WorldObject performer, SkillProperty skill, int count, WorldStateChangedListeners worldStateChangedListeners) {
		performer.getProperty(skill).use(count, performer, skill, worldStateChangedListeners);
		
	}

	public static int getLogarithmicSkillBonus(WorldObject performer, SkillProperty skill) {
		return 1 + (skill.getLevel(performer) / 5);
	}
}
