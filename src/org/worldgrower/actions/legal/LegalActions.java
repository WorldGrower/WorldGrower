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

import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;

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
	
	public static int[] legalFlagsToArgs(Map<LegalAction, Boolean> legalFlags) {
		List<LegalAction> actions = toList(legalFlags);
		int[] args = new int[actions.size()];
		for(int i=0; i<actions.size(); i++) {
			LegalAction legalAction = actions.get(i);
			args[i] = legalFlags.get(legalAction) ? 1 : 0;
		}
		return args;
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

	public void setLegalFlag(LegalAction legalAction, boolean legalFlag) {
		legalActions.put(legalAction, legalFlag);
		
	}

	public Map<LegalAction, Boolean> getLegalActions() {
		return new HashMap<>(legalActions);
	}
}
