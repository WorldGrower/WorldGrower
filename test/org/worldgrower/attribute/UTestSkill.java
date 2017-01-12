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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.WeightPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.start.CharacterAttributes;

public class UTestSkill {

	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testGetLevel() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.LUMBERING_SKILL, new Skill(20));
		
		assertEquals(10, performer.getProperty(Constants.LUMBERING_SKILL).getLevel(performer));
		
		Conditions.add(performer, Condition.INTOXICATED_CONDITION, 8, world);
		assertEquals(5, performer.getProperty(Constants.LUMBERING_SKILL).getLevel(performer));
	}
	
	@Test
	public void testIncreaseCharacterLevel() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		WorldObject performer = createPlayerCharacter(world, organization);
		
		assertEquals(1, performer.getProperty(Constants.LEVEL).intValue());
		assertEquals(20 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals(20 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS_MAX).intValue());
		assertEquals(100, WeightPropertyUtils.getCarryingCapacity(performer));
		assertEquals(1000, performer.getProperty(Constants.ENERGY).intValue());
		
		Skill.increaseLevel(performer, new WorldStateChangedListeners());
		assertEquals(2, performer.getProperty(Constants.LEVEL).intValue());
		assertEquals(21 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals(21 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS_MAX).intValue());
		assertEquals(110, WeightPropertyUtils.getCarryingCapacity(performer));
		assertEquals(1010, performer.getProperty(Constants.ENERGY).intValue());
	}
	
	private WorldObject createPlayerCharacter(World world, WorldObject organization) {
		CharacterAttributes characterAttributes = new CharacterAttributes(10, 10, 10, 10, 10, 10);
		WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(0, "player", "adventurer" , "female", world, commonerGenerator, organization, characterAttributes, ImageIds.KNIGHT);
		return playerCharacter;
	}
}
