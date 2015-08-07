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
package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.ChooseProfessionAction;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

import static org.junit.Assert.assertEquals;
import static org.worldgrower.actions.ChooseProfessionAction.ProfessionEvaluation;

public class UTestChooseProfessionAction {

	@Test
	public void testProfessionEvaluationAdd() {
		ProfessionEvaluation professionEvaluation1 = new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10);
		ProfessionEvaluation professionEvaluation2 = new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10);
		assertEquals(20, professionEvaluation1.add(professionEvaluation2).getEvaluation());
	}
	
	@Test
	public void testProfessionEvaluationSort() {
		List<ProfessionEvaluation> professionEvaluations = Arrays.asList(
				new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10), 
				new ProfessionEvaluation(Professions.FARMER_PROFESSION, 20)
			);
		Collections.sort(professionEvaluations);
		
		assertEquals(20, professionEvaluations.get(0).getEvaluation());
		assertEquals(10, professionEvaluations.get(1).getEvaluation());
	}
	
	@Test
	public void testGetProfessionCounts() {
		List<WorldObject> worldObjects = new ArrayList<>();
		worldObjects.add(TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));
		worldObjects.add(TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION));
		worldObjects.add(TestUtils.createIntelligentWorldObject(3, Constants.PROFESSION, Professions.BLACKSMITH_PROFESSION));
		
		Map<Profession, Integer> professionCounts = ChooseProfessionAction.getProfessionCounts(worldObjects);
		
		assertEquals(2, professionCounts.size());
		assertEquals(Integer.valueOf(2), professionCounts.get(Professions.FARMER_PROFESSION));
		assertEquals(Integer.valueOf(1), professionCounts.get(Professions.BLACKSMITH_PROFESSION));
	}
	
	@Test
	public void testGetProfessionEvaluationsByCompetition() {
		World world = new WorldImpl(10, 10, null);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));

		WorldObject performer = TestUtils.createWorldObject(1, "jobseeker");
		List<ProfessionEvaluation> professionEvaluations = ChooseProfessionAction.getProfessionEvaluationsByCompetition(performer, world);
		
		assertEquals(9, professionEvaluations.size());
		assertProfessionEvaluation(professionEvaluations.get(0), Professions.PRIEST_PROFESSION, 5);
		assertProfessionEvaluation(professionEvaluations.get(1), Professions.BLACKSMITH_PROFESSION, 5);
		assertProfessionEvaluation(professionEvaluations.get(2), Professions.THIEF_PROFESSION, 5);
		assertProfessionEvaluation(professionEvaluations.get(3), Professions.LUMBERJACK_PROFESSION, 5);
	}
	
	@Test
	public void testFindIndexOfName() {
		List<ProfessionEvaluation> professionEvaluations = Arrays.asList(
				new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10), 
				new ProfessionEvaluation(Professions.BLACKSMITH_PROFESSION, 20)
			);
		
		assertEquals(0, ChooseProfessionAction.findIndexOfName(Professions.FARMER_PROFESSION.getDescription(), professionEvaluations));
		assertEquals(1, ChooseProfessionAction.findIndexOfName(Professions.BLACKSMITH_PROFESSION.getDescription(), professionEvaluations));
		assertEquals(-1, ChooseProfessionAction.findIndexOfName(Professions.PRIEST_PROFESSION.getDescription(), professionEvaluations));
	}
	
	@Test
	public void testMerge() {
		List<ProfessionEvaluation> list1 = Arrays.asList(
				new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10), 
				new ProfessionEvaluation(Professions.BLACKSMITH_PROFESSION, 20)
			);
		
		List<ProfessionEvaluation> list2 = Arrays.asList(
				new ProfessionEvaluation(Professions.FARMER_PROFESSION, 10), 
				new ProfessionEvaluation(Professions.PRIEST_PROFESSION, 30)
			);
		
		List<ProfessionEvaluation> professionEvaluations = ChooseProfessionAction.merge(list1, list2);
		assertEquals(3, professionEvaluations.size());
		assertProfessionEvaluation(professionEvaluations.get(0), Professions.FARMER_PROFESSION, 20);
		assertProfessionEvaluation(professionEvaluations.get(1), Professions.BLACKSMITH_PROFESSION, 20);
		assertProfessionEvaluation(professionEvaluations.get(2), Professions.PRIEST_PROFESSION, 30);
	}
	
	private void assertProfessionEvaluation(ProfessionEvaluation actualProfessionEvaluation, Profession expectedProfession, int expectedEvaluation) {
		assertEquals(expectedProfession, actualProfessionEvaluation.getProfession());
		assertEquals(expectedEvaluation, actualProfessionEvaluation.getEvaluation());
	}
}
