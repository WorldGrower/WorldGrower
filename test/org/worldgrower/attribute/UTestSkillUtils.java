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
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.profession.Professions;

public class UTestSkillUtils {

	@Test
	public void testCanTargetTeachPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(10));
		performer.setProperty(Constants.PROFESSION, Professions.LUMBERJACK_PROFESSION);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(20));
		target.setProperty(Constants.PROFESSION, Professions.LUMBERJACK_PROFESSION);
		
		assertEquals(true, SkillUtils.canTargetTeachPerformer(performer, target));
		assertEquals(false, SkillUtils.canTargetTeachPerformer(target, performer));
	}
	
	@Test
	public void testGetSkillBonus() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(10));
		assertEquals(1.0f, SkillUtils.getSkillBonus(performer, Constants.LUMBERING_SKILL), 0.1f);
		
		performer.setProperty(Constants.LUMBERING_SKILL, new Skill(20));
		assertEquals(1.1f, SkillUtils.getSkillBonus(performer, Constants.LUMBERING_SKILL), 0.1f);
	}
	
	@Test
	public void testGetRealEnergyUse() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(20));
		assertEquals(90, SkillUtils.getRealEnergyUse(performer, Constants.LUMBERING_SKILL, 100));
	}
	
	@Test
	public void testHasEnoughEnergy() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(10));
		
		performer.setProperty(Constants.ENERGY, 200);
		assertEquals(true, SkillUtils.hasEnoughEnergy(performer, Constants.LUMBERING_SKILL, 100));
		
		performer.setProperty(Constants.ENERGY, 50);
		assertEquals(false, SkillUtils.hasEnoughEnergy(performer, Constants.LUMBERING_SKILL, 100));
	}
	
	@Test
	public void testGetSkillsForAttribute() {
		List<SkillProperty> expectedSkills = Arrays.asList(Constants.LIGHT_ARMOR_SKILL, Constants.ARCHERY_SKILL, Constants.THIEVERY_SKILL);
		List<SkillProperty> actualSkills = SkillUtils.getSkillsForAttribute(Constants.DEXTERITY);
		assertEquals(new HashSet<>(expectedSkills), new HashSet<>(actualSkills));
	}
}
