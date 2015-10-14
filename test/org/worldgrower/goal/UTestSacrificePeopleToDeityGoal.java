package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;

public class UTestSacrificePeopleToDeityGoal {

	private SacrificePeopleToDeityGoal goal = Goals.SACRIFICE_PEOPLE_TO_DEITY_GOAL;
	
	@Test
	public void testCalculateGoalNoAltar() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		
		assertEquals(Actions.BUILD_SACRIFICAL_ALTAR_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOneAltarNoSacrifices() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		BuildingGenerator.generateSacrificialAltar(0, 0, world, performer, Deity.HADES, 1f);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneAltarOneSacrifice() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		WorldObject target = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 4);
		performer.setProperty(Constants.DEITY, Deity.HADES);
		target.setProperty(Constants.DEITY, Deity.APHRODITE);
		target.setProperty(Constants.STRENGTH, 20);
		target.setProperty(Constants.CONDITIONS, new Conditions());
		world.addWorldObject(target);
		BuildingGenerator.generateSacrificialAltar(0, 0, world, performer, Deity.HADES, 1f);
		
		assertEquals(Actions.NON_LETHAL_MELEE_ATTACK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOneAltarOneCapture() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		WorldObject target = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 4);
		performer.setProperty(Constants.DEITY, Deity.HADES);
		target.setProperty(Constants.DEITY, Deity.APHRODITE);
		target.setProperty(Constants.STRENGTH, 20);
		target.setProperty(Constants.CONDITIONS, new Conditions());
		target.getProperty(Constants.CONDITIONS).addCondition(Condition.UNCONSCIOUS_CONDITION, 8, world);
		world.addWorldObject(target);
		BuildingGenerator.generateSacrificialAltar(0, 0, world, performer, Deity.HADES, 1f);
		
		assertEquals(Actions.CAPTURE_PERSON_FOR_SACRIFICE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
}
