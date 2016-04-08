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
import org.worldgrower.attribute.IdList;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestProtectOneSelfGoal {

	private ProtectOnseSelfGoal goal = Goals.PROTECT_ONSE_SELF_GOAL;
	private CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNoDanger() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(5, 5, 1, 1, Constants.ID, 3);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneEnemy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization);
		generateEnemy(7, 7, organization, world);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(-1));
	}
	
	private void generateEnemy(int x, int y, WorldObject organization, World world) {
		int id = commonerGenerator.generateCommoner(x, y, world, organization);
		WorldObject enemy = world.findWorldObject(Constants.ID, id);
		enemy.getProperty(Constants.GROUP).removeAll();
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtLongerDistance() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(3, 3, world, organization);
		generateEnemy(7, 7, organization, world);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtEdge() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(3, 3, world, organization);
		generateEnemy(12, 12, organization, world);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization);
		WorldObject performer = world.findWorldObject(Constants.ID, performerId);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		int targetId = commonerGenerator.generateCommoner(7, 7, world, organization);
		WorldObject target = world.findWorldObject(Constants.ID, targetId);
		target.setProperty(Constants.GROUP, new IdList());
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
