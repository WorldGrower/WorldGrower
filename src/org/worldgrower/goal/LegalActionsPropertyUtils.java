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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.ActionLegalHandler;

public class LegalActionsPropertyUtils {

	public static List<ManagedOperation> getLegalActionsList(World world) {
		Map<ManagedOperation, ActionLegalHandler> legalActions = getLegalActions(world);
		
		return toList(legalActions);
	}

	static List<ManagedOperation> toList(Map<ManagedOperation, ActionLegalHandler> legalActions) {
		List<ManagedOperation> actions = new ArrayList<>(legalActions.keySet());
		Actions.sortActionsByDescription(actions);
		return actions;
	}

	public static Map<ManagedOperation, ActionLegalHandler> getLegalActions(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		Map<ManagedOperation, ActionLegalHandler> legalActions = villagersOrganization.getProperty(Constants.LEGAL_ACTIONS);
		return legalActions;
	}
	
	public static int[] legalActionsToArgs(Map<ManagedOperation, ActionLegalHandler> legalActions) {
		List<ManagedOperation> actions = toList(legalActions);
		int[] args = new int[actions.size()];
		for(int i=0; i<actions.size(); i++) {
			ManagedOperation action = actions.get(i);
			args[i] = legalActions.get(action).getLegalFlag() ? 1 : 0;
		}
		return args;
	}
}
