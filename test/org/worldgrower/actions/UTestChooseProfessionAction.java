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

import static org.junit.Assert.assertEquals;

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
import org.worldgrower.actions.ChooseProfessionAction.ProfessionEvaluation;
import org.worldgrower.attribute.IdList;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Profession;
import org.worldgrower.profession.Professions;

public class UTestChooseProfessionAction {

	private ChooseProfessionAction action = Actions.CHOOSE_PROFESSION_ACTION;
	
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
		World world = new WorldImpl(10, 10, null, null);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));

		WorldObject performer = TestUtils.createWorldObject(1, "jobseeker");
		List<ProfessionEvaluation> professionEvaluations = ChooseProfessionAction.getProfessionEvaluationsByCompetition(performer, world);
		
		assertEquals(true, professionEvaluations.size() > 5);
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
	
	@Test
	public void testGetRemains() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject target1 = TestUtils.createWorldObject(1, "target1");
		WorldObject target2 = TestUtils.createWorldObject(1, "target2");
		target2.setProperty(Constants.DECEASED_WORLD_OBJECT, Boolean.TRUE);
		world.addWorldObject(target1);
		world.addWorldObject(target2);
		assertEquals(Arrays.asList(target2), ChooseProfessionAction.getRemains(world));
	}
	
	@Test
	public void testGetProfessionEvaluationsByDemand() {
		World world = new WorldImpl(10, 10, null, null);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Constants.PROFESSION, Professions.FARMER_PROFESSION));

		createVillagersOrganization(world);
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Goals.ARENA_GOAL);
		performer.getProperty(Constants.DEMANDS).add(Constants.FOOD, 20);
		world.addWorldObject(performer);
		
		List<ProfessionEvaluation> professionEvaluations = ChooseProfessionAction.getProfessionEvaluationsByDemand(performer, world);
		
		assertEquals(true, professionEvaluations.size() > 0);
		assertProfessionEvaluation(professionEvaluations.get(0), Professions.FARMER_PROFESSION, 23);
	}
	
	@Test
	public void testGetProfessionEvaluationsByPerformer() {
		World world = new WorldImpl(10, 10, null, null);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(1, Constants.PROFESSION, Professions.FARMER_PROFESSION));

		WorldObject performer = TestUtils.createWorldObject(1, "jobseeker");
		performer.setProperty(Constants.STRENGTH, 8);
		performer.setProperty(Constants.CONSTITUTION, 8);
		performer.setProperty(Constants.DEXTERITY, 8);
		performer.setProperty(Constants.INTELLIGENCE, 20);
		performer.setProperty(Constants.WISDOM, 8);
		performer.setProperty(Constants.CHARISMA, 8);
		world.addWorldObject(performer);
		
		List<ProfessionEvaluation> professionEvaluations = ChooseProfessionAction.getProfessionEvaluationsByPerformer(performer);
		
		assertEquals(true, professionEvaluations.size() > 0);
		assertProfessionEvaluation(professionEvaluations.get(0), Professions.PRIEST_PROFESSION, 64);
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(1, "jobseeker");
		WorldObject target = TestUtils.createWorldObject(2, "jobseeker");
		
		assertEquals(true, action.isValidTarget(performer, performer, world));
		assertEquals(false, action.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testGetReasonNoProfessionEvaluation() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		assertEquals("It just seemed like a good idea to become a farmer", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetReasonSkilled() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByPerformer.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		assertEquals("I choose to become a farmer because I'm good at it", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetReasonNoCompetition() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByCompetition.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		assertEquals("I choose to become a farmer because there isn't much competition for it", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetReasonDemand() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByDemand.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		assertEquals("I choose to become a farmer because there is a demand for it", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetReasonSkilledAndNoCompetition() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByPerformer.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		professionEvaluationsByCompetition.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		assertEquals("I choose to become a farmer because there isn't much competition for that profession and I'm good at it", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetReasonBackground() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByBackground.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5, "I was hungry in the past"));
		assertEquals("I choose to become a farmer because I was hungry in the past", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetProfessionEvaluationsByBackground() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(1, "jobseeker");
		
		performer.getProperty(Constants.DEMANDS).add(Constants.FOOD, 10);
		List<ProfessionEvaluation> professionEvaluationsByBackground = ChooseProfessionAction.getProfessionEvaluationsByBackground(performer, world);
		assertEquals(1, professionEvaluationsByBackground.size());
		assertEquals(Professions.FARMER_PROFESSION, professionEvaluationsByBackground.get(0).getProfession());
	}
	
	@Test
	public void testGetReasonParents() {
		List<ProfessionEvaluation> professionEvaluationsByPerformer = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByCompetition = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByDemand = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByBackground = new ArrayList<>();
		List<ProfessionEvaluation> professionEvaluationsByParents = new ArrayList<>();
		
		professionEvaluationsByParents.add(new ProfessionEvaluation(Professions.FARMER_PROFESSION, 5));
		assertEquals("I choose to become a farmer because my parents are farmers", ChooseProfessionAction.getReason(Professions.FARMER_PROFESSION, professionEvaluationsByPerformer, professionEvaluationsByCompetition, professionEvaluationsByDemand, professionEvaluationsByBackground, professionEvaluationsByParents));
	}
	
	@Test
	public void testGetProfessionEvaluationsByParents() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), "jobseeker");
		
		List<ProfessionEvaluation> professionEvaluationsByParents = ChooseProfessionAction.getProfessionEvaluationsByParents(performer, world);
		assertEquals(0, professionEvaluationsByParents.size());
		
		WorldObject parent = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), "parent");
		parent.setProperty(Constants.CHILDREN, new IdList().add(performer));
		parent.setProperty(Constants.PROFESSION, Professions.BLACKSMITH_PROFESSION);
		world.addWorldObject(parent);
		
		professionEvaluationsByParents = ChooseProfessionAction.getProfessionEvaluationsByParents(performer, world);
		assertEquals(1, professionEvaluationsByParents.size());
		assertEquals(Professions.BLACKSMITH_PROFESSION, professionEvaluationsByParents.get(0).getProfession());
	}

	private void createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
	}
}
