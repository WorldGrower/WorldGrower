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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.ManagedOperation;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class LegalActions implements Serializable {

	private final Map<ManagedOperation, ActionLegalHandler> legalActions = new HashMap<>();
	
	public LegalActions(Map<ManagedOperation, ActionLegalHandler> legalActions) {
		this.legalActions.putAll(legalActions);
	}

	public List<ManagedOperation> toList() {
		List<ManagedOperation> actions = new ArrayList<>(legalActions.keySet());
		Actions.sortActionsByDescription(actions);
		return actions;
	}
	
	private static List<ManagedOperation> toList(Map<ManagedOperation, Boolean> legalFlags) {
		List<ManagedOperation> actions = new ArrayList<>(legalFlags.keySet());
		Actions.sortActionsByDescription(actions);
		return actions;
	}
	
	public static int[] legalFlagsToArgs(Map<ManagedOperation, Boolean> legalFlags) {
		List<ManagedOperation> actions = toList(legalFlags);
		int[] args = new int[actions.size()];
		for(int i=0; i<actions.size(); i++) {
			ManagedOperation action = actions.get(i);
			args[i] = legalFlags.get(action) ? 1 : 0;
		}
		return args;
	}
	
	public Boolean isLegalAction(WorldObject performer, WorldObject actionTarget, int[] args, ManagedOperation managedOperation) {
		ActionLegalHandler actionLegalHandler = legalActions.get(managedOperation);
		Boolean isLegal = actionLegalHandler != null ? actionLegalHandler.isActionLegal(performer, actionTarget, args) : null;
		return isLegal;
	}

	public void setLegalFlag(ManagedOperation action, boolean legalFlag) {
		legalActions.put(action, legalActions.get(action).setLegalFlag(legalFlag));
		
	}

	public Map<ManagedOperation, Boolean> getLegalFlags() {
		Map<ManagedOperation, Boolean> legalFlags = new HashMap<>();
		for(Entry<ManagedOperation, ActionLegalHandler> entry : legalActions.entrySet()) {
			legalFlags.put(entry.getKey(), entry.getValue().getLegalFlag());
		}
		return legalFlags;
	}
}
