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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListener;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;

public class UTestSetGovernanceAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(3);
		world.addWorldObject(performer);
		createVillagersOrganization(world);
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Map<LegalAction, Boolean> legalFlags = legalActions.getLegalActions();
		int[] args = LegalActions.createGovernanceArgs(legalFlags, 0, 0, 5, 5);
			
		Actions.SET_GOVERNANCE_ACTION.execute(performer, performer, args, world);
		
		legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		assertEquals(false, legalActions.getLegalFlag(LegalAction.MELEE_ATTACK));
	}
	
	@Test
	public void testExecuteSetTaxesAndWages() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(3);
		world.addWorldObject(performer);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Map<LegalAction, Boolean> legalFlags = legalActions.getLegalActions();
		int[] args = LegalActions.createGovernanceArgs(legalFlags, 2, 3, 6, 7);
			
		Actions.SET_GOVERNANCE_ACTION.execute(performer, performer, args, world);
		
		legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		assertEquals(2, villagersOrganization.getProperty(Constants.SHACK_TAX_RATE).intValue());
		assertEquals(3, villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE).intValue());
		assertEquals(6, villagersOrganization.getProperty(Constants.SHERIFF_WAGE).intValue());
		assertEquals(7, villagersOrganization.getProperty(Constants.TAX_COLLECTOR_WAGE).intValue());
	}
	
	@Test
	public void testExecuteGovernanceChanged() {
		World world = new WorldImpl(1, 1, null, null);
		world.addWorldStateChangedListener(new WorldStateListenerImpl());
		WorldObject performer = createPerformer(3);
		world.addWorldObject(performer);
		createVillagersOrganization(world);
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		
		Map<LegalAction, Boolean> legalFlags = legalActions.getLegalActions();
		legalFlags.put(LegalAction.MELEE_ATTACK, Boolean.TRUE);
		int[] args = LegalActions.createGovernanceArgs(legalFlags, 0, 1, 5, 5);
			
		Actions.SET_GOVERNANCE_ACTION.execute(performer, performer, args, world);
	}
	
	private static class WorldStateListenerImpl implements WorldStateChangedListener {

		@Override
		public void creatureTypeChange(WorldObject worldObject, CreatureType newCreatureType, String description) {
		}

		@Override
		public void electionFinished(WorldObject winner, WorldObject organization, IdList candidates, int electionWonPercentage) {
		}

		@Override
		public void governanceChanged(List<LegalAction> changedLegalActions, List<GovernanceOption> changedGovernanceOptions, WorldObject villagerLeader) {
			assertEquals(Arrays.asList(LegalAction.MELEE_ATTACK), changedLegalActions);
			assertEquals(3, changedGovernanceOptions.size());
			assertEquals(Constants.HOUSE_TAX_RATE, changedGovernanceOptions.get(0).getIntProperty());
			assertEquals(1, changedGovernanceOptions.get(0).getNewValue());
			assertEquals(Constants.SHERIFF_WAGE, changedGovernanceOptions.get(1).getIntProperty());
			assertEquals(5, changedGovernanceOptions.get(1).getNewValue());
			assertEquals(Constants.TAX_COLLECTOR_WAGE, changedGovernanceOptions.get(2).getIntProperty());
			assertEquals(5, changedGovernanceOptions.get(2).getNewValue());
		}

		@Override
		public void thrownOutOfGroup(WorldObject worldObject, WorldObject target, int[] args, ManagedOperation action, IdList oldGroup, IdList newGroup) {
		}

		@Override
		public void skillIncreased(WorldObject worldObject, SkillProperty skillProperty, int oldValue, int newValue) {
		}

		@Override
		public void conditionGained(WorldObject worldObject, Condition condition) {
		}

		@Override
		public void conditionLost(WorldObject worldObject, Condition condition) {
		}

		@Override
		public void levelIncreased(WorldObject worldObject, int newValue) {
		}

		@Override
		public void lostLeadership(WorldObject worldObject, WorldObject organization) {
		}

		@Override
		public void electionStarted(WorldObject organization) {
		}

		@Override
		public void skillsDeteriorated(WorldObject worldObject) {
		}

		@Override
		public void fireAssetsSeized(WorldObject worldObject, List<Integer> buildingIds) {
		}		
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(performer);
		createVillagersOrganization(world);
			
		assertEquals(true, Actions.SET_GOVERNANCE_ACTION.isValidTarget(performer, performer, world));
		assertEquals(false, Actions.SET_GOVERNANCE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
			
		assertEquals(true, Actions.SET_GOVERNANCE_ACTION.isActionPossible(performer, performer, Args.EMPTY, world));
	}


	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		return performer;
	}
}