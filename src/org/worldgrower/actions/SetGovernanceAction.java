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

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.attribute.BooleanProperty;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class SetGovernanceAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		List<LegalAction> legalActionsList = legalActions.toList();
		List<LegalAction> changedLegalActions = new ArrayList<>();
		for(int i=0; i<legalActionsList.size(); i++) {
			LegalAction legalAction = legalActionsList.get(i);
			boolean oldLegalFlag = legalActions.getLegalFlag(legalAction);
			boolean newLegalFlag = args[i] == 1;
			legalActions.setLegalFlag(legalAction, newLegalFlag);
			
			if (oldLegalFlag != newLegalFlag) {
				changedLegalActions.add(legalAction);
			}
		}
		
		List<GovernanceOption> changedGovernanceOptions = new ArrayList<>();
		
		int taxRateOffset = legalActionsList.size();
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		changeGovernanceOption(Constants.SHACK_TAX_RATE, villagersOrganization, args[taxRateOffset], changedGovernanceOptions);
		changeGovernanceOption(Constants.HOUSE_TAX_RATE, villagersOrganization, args[taxRateOffset+1], changedGovernanceOptions);
		
		int wageOffset = taxRateOffset + 2;
		changeGovernanceOption(Constants.SHERIFF_WAGE, villagersOrganization, args[wageOffset], changedGovernanceOptions);
		changeGovernanceOption(Constants.TAX_COLLECTOR_WAGE, villagersOrganization, args[wageOffset+1], changedGovernanceOptions);
		
		int voteOffset = wageOffset + 2;
		changeGovernanceOption(Constants.ONLY_OWNERS_CAN_VOTE, villagersOrganization, args[voteOffset], changedGovernanceOptions);
		changeGovernanceOption(Constants.ONLY_MALES_CAN_VOTE, villagersOrganization, args[voteOffset+1], changedGovernanceOptions);
		changeGovernanceOption(Constants.ONLY_FEMALES_CAN_VOTE, villagersOrganization, args[voteOffset+2], changedGovernanceOptions);
		changeGovernanceOption(Constants.ONLY_UNDEAD_CAN_VOTE, villagersOrganization, args[voteOffset+3], changedGovernanceOptions);
		
		int votingTurnOffset = voteOffset + 4;
		changeGovernanceOption(Constants.VOTING_CANDIDATE_TURNS, villagersOrganization, args[votingTurnOffset], changedGovernanceOptions);
		changeGovernanceOption(Constants.VOTING_TOTAL_TURNS, villagersOrganization, args[votingTurnOffset+1], changedGovernanceOptions);
		
		world.getWorldStateChangedListeners().governanceChanged(changedLegalActions, changedGovernanceOptions, performer);
	}

	private void changeGovernanceOption(IntProperty intProperty, WorldObject villagersOrganization, int newValue, List<GovernanceOption> changedGovernanceOptions) {
		int oldValue = villagersOrganization.getProperty(intProperty);
		villagersOrganization.setProperty(intProperty, newValue);
		
		if (oldValue != newValue) {
			changedGovernanceOptions.add(new GovernanceOption(intProperty, newValue));
		}
	}
	
	private void changeGovernanceOption(BooleanProperty booleanProperty, WorldObject villagersOrganization, int newValue, List<GovernanceOption> changedGovernanceOptions) {
		boolean oldValue = villagersOrganization.getProperty(booleanProperty);
		boolean newBooleanValue = (newValue == 1);
		villagersOrganization.setProperty(booleanProperty, newBooleanValue);
		
		if (oldValue != newBooleanValue) {
			changedGovernanceOptions.add(new GovernanceOption(booleanProperty, newValue));
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world);
	}
	
	@Override
	public String getRequirementsDescription() {
		return "only the village leader can set legal actions";
	}
	
	@Override
	public String getDescription() {
		return "set governancy options like allowed actions, wages and taxes";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "setting legal actions";
	}
	
	@Override
	public String getSimpleDescription() {
		return "set legal actions";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.IRON_CLAYMORE;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.PAPER;
	}	
}