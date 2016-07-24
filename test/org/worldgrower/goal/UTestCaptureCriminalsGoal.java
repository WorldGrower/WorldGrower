package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestCaptureCriminalsGoal {

	private CaptureCriminalsGoal goal = Goals.CAPTURE_CRIMINALS_GOAL;
	private CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(Actions.PLANT_TREE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEnemy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.GROUP).removeAll();
		
		BuildingGenerator.generateJail(5, 5, world, 1f);
		
		assertEquals(Actions.NON_LETHAL_MELEE_ATTACK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalUnconsciousEnemy() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.GROUP).removeAll();
		Conditions.add(target, Condition.UNCONSCIOUS_CONDITION, 8, world);
		
		BuildingGenerator.generateJail(5, 5, world, 1f);
		
		assertEquals(Actions.CAPTURE_PERSON_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.GROUP).removeAll();
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(5, 5, world, organization);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}
