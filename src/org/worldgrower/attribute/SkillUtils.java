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

import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.profession.Profession;

public class SkillUtils {

	public static void addAllSkills(Map<ManagedProperty<?>, Object> properties) {
		properties.put(Constants.BLUFF_SKILL, new Skill());
		properties.put(Constants.INSIGHT_SKILL, new Skill());
		properties.put(Constants.HAND_TO_HAND_SKILL, new Skill());
		properties.put(Constants.ONE_HANDED_SKILL, new Skill());
		properties.put(Constants.TWO_HANDED_SKILL, new Skill());
		properties.put(Constants.PERCEPTION_SKILL, new Skill());
		properties.put(Constants.DIPLOMACY_SKILL, new Skill());
		properties.put(Constants.INTIMIDATE_SKILL, new Skill());
		properties.put(Constants.SMITHING_SKILL, new Skill());
		properties.put(Constants.ALCHEMY_SKILL, new Skill());
		properties.put(Constants.ARCHERY_SKILL, new Skill());
		properties.put(Constants.THIEVERY_SKILL, new Skill());
		properties.put(Constants.EVOCATION_SKILL, new Skill());
		properties.put(Constants.ILLUSION_SKILL, new Skill());
		properties.put(Constants.FARMING_SKILL, new Skill());
		properties.put(Constants.MINING_SKILL, new Skill());
		properties.put(Constants.LUMBERING_SKILL, new Skill());
		properties.put(Constants.RELIGION_SKILL, new Skill());
	}
	
	public static boolean canTargetTeachPerformer(WorldObject performer, WorldObject target) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		
		if (performerProfession != null) {
			SkillProperty skillProperty = performerProfession.getSkillProperty();
			if (skillProperty != null) {
				int performerSkillLevel = performer.getProperty(skillProperty).getLevel();
				int targetSkillLevel = target.getProperty(skillProperty).getLevel();
				
				return (performerSkillLevel + 5 < targetSkillLevel);
			}
		}
		return false;
	}
	
	public static void teachSkill(WorldObject performer, SkillProperty skillProperty) {
		for(int i=0; i<10; i++) {
			performer.getProperty(skillProperty).use();
		}
	}
	
	public static double useSkill(WorldObject performer, SkillProperty skill) {
		double result = 1.0f + (performer.getProperty(skill).getLevel() / 100.0f);
		performer.getProperty(skill).use();
		return result;
	}
	
	public static int getRealEnergyUse(WorldObject performer, SkillProperty skill, int energyUse) {
		return (int)(energyUse / (1.0f + (performer.getProperty(skill).getLevel() / 100.0f)));
	}
	
	public static void useEnergy(WorldObject performer, SkillProperty skill, int energyUse) {
		performer.increment(Constants.ENERGY, -(int)(energyUse / useSkill(performer, skill)));
	}
	
	public static int distanceForEnergyUse(WorldObject performer, SkillProperty skill, int energyUse) {
		if (performer.getProperty(Constants.ENERGY) >= getRealEnergyUse(performer, skill, energyUse)) {
			return 0;
		} else {
			return getRealEnergyUse(performer, skill,energyUse) - performer.getProperty(Constants.ENERGY);
		}
	}
}
