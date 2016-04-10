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
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.Goal;

public class UTestPlayerCharacterProfession  {

	PlayerCharacterProfession profession;
	List<Profession> professions;
	
	@Before
	public void setUp() {
		professions = new ArrayList<Profession>();
		profession = new PlayerCharacterProfession("player");
	}
	
	@Test
	public void testGetDescription() {
		assertEquals(profession.getDescription(), "player");
	}

	@Test
	public void testGetProfessionGoals() {
		List<Goal> goals = profession.getProfessionGoals();
		List<Goal> empty = new ArrayList<Goal>();
		assertEquals(goals, empty);
	}

	@Test
	public void testGetSkillProperty() {
		assertNull(profession.getSkillProperty());
	}

	//@Test
	//public void testReadResolve() throws ObjectStreamException {
	//	PlayerCharacterProfession profession2 = new PlayerCharacterProfession(professions);
	//	assertEquals(profession.readResolveImpl(), profession2);
	//}

	@Test
	public void testIsPaidByVillagerLeader() {
		assertFalse(profession.isPaidByVillagerLeader());
	}

	@Test
	public void testAvoidEnemies() {
		assertFalse(profession.avoidEnemies());
	}
	
	@Test
	public void testGetSellItems() {
		List<Item> item = new ArrayList<Item>();
		assertEquals(profession.getSellItems(), item);
	}
}
