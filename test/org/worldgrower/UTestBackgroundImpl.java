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
import org.worldgrower.attribute.ProfessionExplanation;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.RevengeGoal;
import org.worldgrower.history.Turn;
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
		world.getHistory().actionPerformed(new OperationInfo(attacker, performer, new int[0], Actions.MELEE_ATTACK_ACTION), new Turn());
		
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
		
		background.addGoalObstructed(performer, actionTarget, Actions.MELEE_ATTACK_ACTION, new int[0], world);
		
		assertEquals(Arrays.asList("You were attacking actionTarget"), background.getAngryReasons(true, 7, performer, world));
		assertEquals(Arrays.asList("He was attacking actionTarget"), background.getAngryReasons(false, 7, performer, world));
		
		assertEquals(Arrays.asList(), background.getAngryReasons(false, 7, actionTarget, world));
	}
	
	@Test
	public void testChooseProfessionDeadParent() {
		Background background = new BackgroundImpl();
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createWorldObject(0, "Tom");
		world.addWorldObject(performer);

		ProfessionExplanation professionExplanation = background.chooseProfession(performer, world);
		assertEquals(Professions.GRAVE_DIGGER_PROFESSION, professionExplanation.getProfession());
		assertEquals("one of my parents died", professionExplanation.getExplanation());
	}
	
	@Test
	public void testRemove() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createWorldObject(0, "Tom");
		performer.setProperty(Constants.GENDER, "male");
		WorldObject target = createWorldObject(1, "target");
		
		Background background = performer.getProperty(Constants.BACKGROUND);
		background.addGoalObstructed(performer, target, Actions.MELEE_ATTACK_ACTION, new int[0], world);
		assertEquals(1, background.getAngryReasons(true, 1, performer, world).size());
		background.remove(performer, Constants.BACKGROUND, 0);
		
		assertEquals(0, background.getAngryReasons(true, 1, performer, world).size());
	}
	
	@Test
	public void testRemoveRevengeTargets() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createWorldObject(0, "Tom");
		WorldObject target = createWorldObject(1, "target");
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		world.getHistory().actionPerformed(new OperationInfo(target, performer, new int[0], Actions.MELEE_ATTACK_ACTION), new Turn());
		Background background = performer.getProperty(Constants.BACKGROUND);
		background.checkForNewGoals(performer, world);
		assertEquals(true, background.hasRevengeTarget(world));
		background.remove(performer, Constants.BACKGROUND, 1);
		
		assertEquals(false, background.hasRevengeTarget(world));
	}
}
