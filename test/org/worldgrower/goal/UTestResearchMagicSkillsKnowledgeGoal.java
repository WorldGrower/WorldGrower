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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.PlantGenerator;

public class UTestResearchMagicSkillsKnowledgeGoal {

	private ResearchMagicSkillsKnowledgeGoal goal = Goals.RESEARCH_MAGIC_SKILLS_KNOWLEDGE_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalCutWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		PlantGenerator.generateTree(5, 5, world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalBuildLibrary() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Constants.WOOD, 20, null);
		
		assertEquals(Actions.BUILD_LIBRARY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalResearch() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		BuildingGenerator.generateLibrary(5, 5, world);
		
		assertEquals(Actions.RESEARCH_EVOCATION_SKILL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalResearchSecondSkill() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		BuildingGenerator.generateLibrary(5, 5, world);
		performer.getProperty(Constants.EVOCATION_SKILL).use(5000, performer, Constants.EVOCATION_SKILL, new WorldStateChangedListeners());
		
		assertEquals(Actions.RESEARCH_RESTORATION_SKILL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		SkillUtils.useSkill(performer, Constants.EVOCATION_SKILL, 5000, new WorldStateChangedListeners());
		SkillUtils.useSkill(performer, Constants.RESTORATION_SKILL, 5000, new WorldStateChangedListeners());
		SkillUtils.useSkill(performer, Constants.NECROMANCY_SKILL, 5000, new WorldStateChangedListeners());
		SkillUtils.useSkill(performer, Constants.ILLUSION_SKILL, 5000, new WorldStateChangedListeners());
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}