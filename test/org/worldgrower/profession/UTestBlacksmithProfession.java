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
package org.worldgrower.profession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class UTestBlacksmithProfession {

	BlacksmithProfession profession;
	List<Profession> professions;
	
	@Before
	public void setUp() {
		professions = new ArrayList<Profession>();
		profession = new BlacksmithProfession(professions);
	}
	
	@Test
	public void testGetDescription() {
		assertEquals(profession.getDescription(), "blacksmith");
	}

	@Test
	public void testGetProfessionGoals() {
		List<Goal> goals = profession.getProfessionGoals();
		assertEquals(goals.get(0), Goals.SMITH_GOAL);
		assertEquals(goals.get(1), Goals.CRAFT_EQUIPMENT_GOAL);
		assertEquals(goals.get(2), Goals.MARK_EQUIPMENT_AS_SELLABLE_GOAL);
		assertEquals(goals.get(3), Goals.CREATE_REPAIR_HAMMER_GOAL);
		assertEquals(goals.get(4), Goals.MARK_REPAIR_HAMMERS_AS_SELLABLE_GOAL);
		assertEquals(goals.get(5), Goals.MINT_GOLD_GOAL);
	}

	@Test
	public void testGetSkillProperty() {
		assertEquals(profession.getSkillProperty(), Constants.SMITHING_SKILL);
	}

	//@Test
	//public void testReadResolve() throws ObjectStreamException {
	//	BlacksmithProfession profession2 = new BlacksmithProfession(professions);
	//	assertEquals(profession.readResolveImpl(), profession2);
	//}

	@Test
	public void testIsPaidByVillagerLeader() {
		assertFalse(profession.isPaidByVillagerLeader());
	}

	@Test
	public void testAvoidEnemies() {
		assertTrue(profession.avoidEnemies());
	}
	
	@Test
	public void testGetSellItems() {
		List<Item> item = new ArrayList<Item>();
		item.add(Item.IRON_AXE);
		item.add(Item.IRON_CLAYMORE);
		item.add(Item.IRON_GREATAXE);
		item.add(Item.IRON_GREATSWORD);
		item.add(Item.IRON_HELMET);
		item.add(Item.IRON_CUIRASS);
		item.add(Item.IRON_GAUNTLETS);
		item.add(Item.IRON_GREAVES);
		item.add(Item.IRON_SHIELD);
		assertEquals(profession.getSellItems(), item);
	}
}