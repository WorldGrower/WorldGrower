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
package org.worldgrower.actions.legal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;

/**
 * LegalActions determine for each LegalAction whether it is legal or not
 */
public class LegalActions implements Serializable {

	private final Map<LegalAction, Boolean> legalActions = new HashMap<>();
	
	public LegalActions(Map<LegalAction, Boolean> legalActions) {
		this.legalActions.putAll(legalActions);
	}

	public List<LegalAction> toList() {
		List<LegalAction> actions = new ArrayList<>(legalActions.keySet());
		sortLegalActionsByDescription(actions);
		return actions;
	}
	
	private static void sortLegalActionsByDescription(List<LegalAction> actions) {
		Collections.sort(actions, new LegalActionComparator());
	}
	
	private static class LegalActionComparator implements Comparator<LegalAction> {

		@Override
		public int compare(LegalAction o1, LegalAction o2) {
			return o1.getAction().getSimpleDescription().compareTo(o2.getAction().getSimpleDescription());
		}
	}
	
	private static List<LegalAction> toList(Map<LegalAction, Boolean> legalFlags) {
		List<LegalAction> actions = new ArrayList<>(legalFlags.keySet());
		sortLegalActionsByDescription(actions);
		return actions;
	}
	
	public static int[] createGovernanceArgs(Map<LegalAction, Boolean> legalFlags, int shackTaxRate, int houseTaxRate) {
		List<LegalAction> actions = toList(legalFlags);
		int[] args = new int[actions.size() + 2];
		for(int i=0; i<actions.size(); i++) {
			LegalAction legalAction = actions.get(i);
			args[i] = legalFlags.get(legalAction) ? 1 : 0;
		}
		int wageOffset = actions.size();
		args[wageOffset] = shackTaxRate;
		args[wageOffset+1] = houseTaxRate;
		
		return args;
	}
	
	public static int[] createGovernanceArgs(Map<LegalAction, Boolean> legalFlags, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		int shackTaxRate = villagersOrganization.getProperty(Constants.SHACK_TAX_RATE);
		int houseTaxRate = villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE);
		return createGovernanceArgs(legalFlags, shackTaxRate, houseTaxRate);
	}
	
	public Boolean isLegalAction(WorldObject performer, WorldObject actionTarget, int[] args, ManagedOperation managedOperation) {
		LegalAction actionLegalHandler = null;
		for(LegalAction legalAction : legalActions.keySet()) {
			if (legalAction.getAction() == managedOperation && legalAction.getActionLegalHandler().isApplicable(performer, actionTarget, args)) {
				actionLegalHandler = legalAction;
			}
		}
		Boolean isLegal = actionLegalHandler != null ? legalActions.get(actionLegalHandler) : Boolean.TRUE;
		return isLegal;
	}

	public boolean getLegalFlag(LegalAction legalAction) {
		return legalActions.get(legalAction);
	}
	
	public void setLegalFlag(LegalAction legalAction, boolean legalFlag) {
		legalActions.put(legalAction, legalFlag);
		
	}

	public Map<LegalAction, Boolean> getLegalActions() {
		return new HashMap<>(legalActions);
	}
}
