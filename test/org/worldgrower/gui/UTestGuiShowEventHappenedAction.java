package org.worldgrower.gui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.GovernanceOption;
import org.worldgrower.actions.legal.LegalAction;

public class UTestGuiShowEventHappenedAction {

	@Test
	public void testCreateGovernanceDescription() {
		WorldObject villagerLeader = TestUtils.createIntelligentWorldObject(2, "leader");
		List<LegalAction> changedLegalActions = new ArrayList<>();
		List<GovernanceOption> changedGovernanceOptions = new ArrayList<>();
		
		changedLegalActions.add(LegalAction.MELEE_ATTACK);
		assertEquals("leader changed governance: changed legality of melee attack ", GuiShowEventHappenedAction.createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader));
		
		changedGovernanceOptions.add(new GovernanceOption(Constants.SHACK_TAX_RATE, 4));
		assertEquals("leader changed governance: changed legality of melee attack , changed shackTaxRate to 4", GuiShowEventHappenedAction.createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader));
	
		changedLegalActions.clear();
		assertEquals("leader changed governance: changed shackTaxRate to 4", GuiShowEventHappenedAction.createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader));
	
		changedGovernanceOptions.clear();
		changedLegalActions.add(LegalAction.MELEE_ATTACK);
		changedLegalActions.add(LegalAction.BUTCHER);
		assertEquals("leader changed governance: changed legality of melee attack , changed legality of butcher unowned cattle", GuiShowEventHappenedAction.createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader));
	}
	
	@Test
	public void testCreateGovernanceDescriptionForVoting() {
		WorldObject villagerLeader = TestUtils.createIntelligentWorldObject(2, "leader");
		List<LegalAction> changedLegalActions = new ArrayList<>();
		List<GovernanceOption> changedGovernanceOptions = new ArrayList<>();
		
		changedGovernanceOptions.add(new GovernanceOption(Constants.ONLY_OWNERS_CAN_VOTE, 1));
		assertEquals("leader changed governance: changed 'only owners can vote' to true", GuiShowEventHappenedAction.createGovernanceDescription(changedLegalActions, changedGovernanceOptions, villagerLeader));
	}
}
