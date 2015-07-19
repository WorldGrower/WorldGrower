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
	}
	
}
