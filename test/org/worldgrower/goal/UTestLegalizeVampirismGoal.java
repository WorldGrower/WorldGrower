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
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestLegalizeVampirismGoal {

	private LegalizeVampirismGoal goal = Goals.LEGALIZE_VAMPIRISM_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalLegalize() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		WorldObject performer = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		
		assertEquals(Actions.SET_LEGAL_ACTIONS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalBiteLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		WorldObject performer = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		
		WorldObject target = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		assertEquals(Actions.VAMPIRE_BITE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);

		WorldObject performer = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		
		LegalActionsPropertyUtils.getLegalActions(world).setLegalFlag(LegalAction.VAMPIRE_BITE, Boolean.FALSE);
		assertEquals(false, goal.isGoalMet(performer, world));
		
		LegalActionsPropertyUtils.getLegalActions(world).setLegalFlag(LegalAction.VAMPIRE_BITE, Boolean.TRUE);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}