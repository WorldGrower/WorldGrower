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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestWaterPropertyUtils {

	@Test
	public void testEveryoneInVicinityKnowsOfPoisoning() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.KNOWLEDGE_MAP, new KnowledgeMap(), 2);
		WorldObject target = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.KNOWLEDGE_MAP, new KnowledgeMap(), 3);
		performer.setProperty(Constants.PERCEPTION_SKILL, new Skill(10));
		target.setProperty(Constants.PERCEPTION_SKILL, new Skill(10));
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		int wellId = BuildingGenerator.buildWell(5, 5, world, 1f);
		WorldObject well = world.findWorldObjectById(wellId);
		WaterPropertyUtils.everyoneInVicinityKnowsOfPoisoning(performer, well, world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(wellId, Constants.POISON_DAMAGE));
		assertEquals(true, target.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(wellId, Constants.POISON_DAMAGE));
	}
	
	@Test
	public void testDrink() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.KNOWLEDGE_MAP, new KnowledgeMap(), 2);
		WorldObject waterTarget = Item.HEALING_POTION.generate(1f);
		
		performer.setProperty(Constants.HIT_POINTS, 10 * Item.COMBAT_MULTIPLIER);
		performer.setProperty(Constants.HIT_POINTS_MAX, 20 * Item.COMBAT_MULTIPLIER);
		
		WaterPropertyUtils.drink(performer, waterTarget, world);
		assertEquals(14 * Item.COMBAT_MULTIPLIER, performer.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testDrinkCurePoisonPotion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, 0, 1, 1, Constants.CONDITIONS, new Conditions(), 2);
		WorldObject waterTarget = Item.CURE_POISON_POTION.generate(1f);
		
		Conditions.add(performer, Condition.POISONED_CONDITION, 8, world);
		
		WaterPropertyUtils.drink(performer, waterTarget, world);
		assertEquals(false, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
	}
}
