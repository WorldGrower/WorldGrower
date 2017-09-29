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
package org.worldgrower;

import static org.junit.Assert.assertEquals;
import static org.worldgrower.TestUtils.createWorldObject;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Background;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.ProfessionExplanation;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.RevengeGoal;
import org.worldgrower.history.Turn;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class UTestBackgroundImpl {
	
	@Test
	public void testPerformerWasAttacked() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(10, 10, null, null);
		
		WorldObject performer = createWorldObject(0, "Tom");
		WorldObject attacker = createWorldObject(1, "attacker");
		world.addWorldObject(performer);
		world.addWorldObject(attacker);
		world.getHistory().actionPerformed(new OperationInfo(attacker, performer, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		
		background.checkForNewGoals(performer, world);
		
		List<Goal> personalGoals = background.getPersonalGoals(performer, world);
		assertEquals(1, personalGoals.size());
		assertEquals(RevengeGoal.class, personalGoals.get(0).getClass());
		assertEquals(attacker, background.getRevengeTarget(world));
	}
	
	@Test
	public void testGetAngryReasons() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createWorldObject(0, "Tom");
		performer.setProperty(Constants.GENDER, "male");
		WorldObject actionTarget = createWorldObject(1, "actionTarget");
		
		background.addGoalObstructed(Goals.PROTECT_ONE_SELF_GOAL, performer, actionTarget, Actions.MELEE_ATTACK_ACTION, Args.EMPTY, world);
		
		assertEquals(Arrays.asList("You were attacking actionTarget"), background.getAngryReasons(true, 7, performer, world));
		assertEquals(Arrays.asList("He was attacking actionTarget"), background.getAngryReasons(false, 7, performer, world));
		
		assertEquals(Arrays.asList(), background.getAngryReasons(false, 7, actionTarget, world));
	}
	
	@Test
	public void testGetAngryReasonsHidden() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createWorldObject(0, "Tom");
		performer.setProperty(Constants.GENDER, "male");
		WorldObject actionTarget = createWorldObject(1, "actionTarget");
		
		background.addGoalObstructed(Goals.STEAL_GOAL, performer, actionTarget, Actions.MELEE_ATTACK_ACTION, Args.EMPTY, world);
		background.addGoalObstructed(Goals.FOOD_GOAL, performer, actionTarget, Actions.EAT_ACTION, Args.EMPTY, world);
		
		assertEquals(Arrays.asList("You were eating actionTarget"), background.getAngryReasons(true, 7, performer, world));
		assertEquals(Arrays.asList("He was eating actionTarget"), background.getAngryReasons(false, 7, performer, world));
		
		assertEquals(Arrays.asList(), background.getAngryReasons(false, 7, actionTarget, world));
	}
	
	@Test
	public void testChooseProfessionDeadParent() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createWorldObject(0, "Test");
		world.addWorldObject(performer);

		ProfessionExplanation professionExplanation = background.chooseProfession(performer, world);
		assertEquals(Professions.GRAVE_DIGGER_PROFESSION, professionExplanation.getProfession());
		assertEquals("one of my parents died", professionExplanation.getExplanation());
	}
	
	@Test
	public void testChooseProfessionHarmed() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(0, Constants.PERSONALITY, new Personality());
		performer.setProperty(Constants.CHILDREN, new IdList());
		world.addWorldObject(performer);
		
		performer.setProperty(Constants.HIT_POINTS, 9);
		performer.setProperty(Constants.HIT_POINTS_MAX, 10);
		
		performer.setProperty(Constants.WISDOM, 20);
		
		world.addWorldObject(TestUtils.createSkilledWorldObject(1, Constants.CHILDREN, new IdList().add(0)));
		world.addWorldObject(TestUtils.createSkilledWorldObject(2, Constants.CHILDREN, new IdList().add(0)));
	
		ProfessionExplanation professionExplanation = background.chooseProfession(performer, world);
		assertEquals(Professions.PRIEST_PROFESSION, professionExplanation.getProfession());
		assertEquals("I wanted to be able to heal myself", professionExplanation.getExplanation());
		
		performer.setProperty(Constants.WISDOM, 0);
		performer.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.COURAGEOUS, 1000, "");
		
		professionExplanation = background.chooseProfession(performer, world);
		assertEquals(Professions.SHERIFF_PROFESSION, professionExplanation.getProfession());
		assertEquals("I wanted to be able to protect myself", professionExplanation.getExplanation());
	}
	
	@Test
	public void testRemove() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createWorldObject(7, "Tom");
		performer.setProperty(Constants.GENDER, "male");
		WorldObject target = createWorldObject(8, "target");
		
		Background background = performer.getProperty(Constants.BACKGROUND);
		background.addGoalObstructed(Goals.PROTECT_ONE_SELF_GOAL, performer, target, Actions.MELEE_ATTACK_ACTION, Args.EMPTY, world);
		assertEquals(1, background.getAngryReasons(true, 8, performer, world).size());
		background.remove(performer, Constants.BACKGROUND, 7);
		
		assertEquals(0, background.getAngryReasons(true, 8, performer, world).size());
	}
	
	@Test
	public void testRemoveRevengeTargets() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createWorldObject(world.generateUniqueId(), "Tom");
		world.addWorldObject(performer);
		WorldObject target = createWorldObject(world.generateUniqueId(), "target");
		world.addWorldObject(target);
		
		world.getHistory().actionPerformed(new OperationInfo(target, performer, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		Background background = performer.getProperty(Constants.BACKGROUND);
		background.checkForNewGoals(performer, world);
		assertEquals(true, background.hasRevengeTarget(world));
		
		Integer targetId = target.getProperty(Constants.ID);
		background.remove(performer, Constants.BACKGROUND, targetId);
		
		assertEquals(false, background.hasRevengeTarget(world));
	}
}
