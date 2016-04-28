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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.actions.legal.DefaultActionLegalHandler;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestVampireBiteGoal {

	private VampireBloodLevelGoal goal = Goals.VAMPIRE_BLOOD_LEVEL_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalBite() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		
		assertEquals(Actions.VAMPIRE_BITE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalBiteIllegal() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		
		LegalActionsPropertyUtils.getLegalActions(world).setLegalFlag(new LegalAction(Actions.VAMPIRE_BITE_ACTION, new DefaultActionLegalHandler()), Boolean.FALSE);
		assertEquals(Actions.VAMPIRE_BITE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalBiteLegal() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		
		LegalActionsPropertyUtils.getLegalActions(world).setLegalFlag(new LegalAction(Actions.VAMPIRE_BITE_ACTION, new DefaultActionLegalHandler()), Boolean.TRUE);
		assertEquals(Actions.VAMPIRE_BITE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		createVillagersOrganization(world);
		VampireUtils.vampirizePerson(performer, new WorldStateChangedListeners());
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.VAMPIRE_BLOOD_LEVEL, 1000);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		world.generateUniqueId();world.generateUniqueId();
		return villagersOrganization;
	}
}