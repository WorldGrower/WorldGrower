package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestSubdueOutsidersGoal {

	private SubdueOutsidersGoal goal = Goals.SUBDUE_OUTSIDERS_GOAL;
	private CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
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
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization);
		int targetId = commonerGenerator.generateCommoner(7, 7, world, organization);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		performer.getProperty(Constants.GROUP).removeAll();
		
		assertEquals(Actions.POISON_ATTACK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
}
