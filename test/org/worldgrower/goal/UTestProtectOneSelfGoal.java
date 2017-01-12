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
import org.worldgrower.attribute.IdList;
import org.worldgrower.generator.CommonerGenerator;

public class UTestProtectOneSelfGoal {

	private ProtectOneSelfGoal goal = Goals.PROTECT_ONE_SELF_GOAL;
	private CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
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
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(7, 7, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(-1));
	}
	
	@Test
	public void testCalculateGoalCorneredByEnemy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(1, 1, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MELEE_ATTACK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	private void generateEnemy(int x, int y, WorldObject organization, World world) {
		int id = commonerGenerator.generateCommoner(x, y, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject enemy = world.findWorldObjectById(id);
		enemy.getProperty(Constants.GROUP).removeAll();
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtLongerDistance() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(3, 3, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(7, 7, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtMediumDistance() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(1, 9, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(1, 1, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtShortDistance() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(0, 1, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(2, 0, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		//TODO: wouldn't it be better if moveArgs were 0,1?
		assertEquals(Actions.REST_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtShortDistance2() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(1, 2, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(3, 1, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateGoalOneEnemyAtEdge() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(3, 3, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(12, 12, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(Actions.MOVE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(-1, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateGoalMultipleTurnsAtMapEdge() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(3, 3, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(7, 7, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(2, performer.getProperty(Constants.X).intValue());
		assertEquals(2, performer.getProperty(Constants.Y).intValue());
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(1, performer.getProperty(Constants.X).intValue());
		assertEquals(1, performer.getProperty(Constants.Y).intValue());
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(0, performer.getProperty(Constants.X).intValue());
		assertEquals(0, performer.getProperty(Constants.Y).intValue());
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(0, performer.getProperty(Constants.X).intValue());
		assertEquals(0, performer.getProperty(Constants.Y).intValue());
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(0, performer.getProperty(Constants.X).intValue());
		assertEquals(0, performer.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testCalculateGoalMultipleTurnsAtLongerDistance() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(8, 8, world, organization, CommonerGenerator.NO_PARENT);
		generateEnemy(0, 0, organization, world);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(7, performer.getProperty(Constants.X).intValue());
		assertEquals(9, performer.getProperty(Constants.Y).intValue());
		
		goal.calculateGoal(performer, world).perform(world);
		assertEquals(6, performer.getProperty(Constants.X).intValue());
		assertEquals(10, performer.getProperty(Constants.Y).intValue());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(5, 5, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(performerId);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		int targetId = commonerGenerator.generateCommoner(7, 7, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject target = world.findWorldObjectById(targetId);
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
