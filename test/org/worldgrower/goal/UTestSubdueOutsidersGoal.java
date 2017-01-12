package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.generator.CommonerGenerator;

public class UTestSubdueOutsidersGoal {

	private SubdueOutsidersGoal goal = Goals.SUBDUE_OUTSIDERS_GOAL;
	private CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneEnemy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization, CommonerGenerator.NO_PARENT);
		int targetId = commonerGenerator.generateCommoner(7, 7, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(performerId);
		performer.getProperty(Constants.GROUP).removeAll();
		
		assertEquals(Actions.POISON_ATTACK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
}
