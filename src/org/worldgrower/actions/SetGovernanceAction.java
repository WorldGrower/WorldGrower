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
		
		int taxRateOffset = legalActionsList.size();
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.SHACK_TAX_RATE, args[taxRateOffset]);
		villagersOrganization.setProperty(Constants.HOUSE_TAX_RATE, args[taxRateOffset+1]);
		
		world.getWorldStateChangedListeners().legalActionsChanged(changedLegalActions, performer);
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
	public ImageIds getImageIds() {
		return ImageIds.IRON_CLAYMORE;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.PAPER;
	}	
}