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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.DefaultGoalObstructedHandler;
import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;

public class LegalActionFactory {

	public static LegalActions create() {
		Map<LegalAction, Boolean> legalActions = new HashMap<>();
		
		List<ManagedOperation> defaultIllegalActions = new ArrayList<>();
		defaultIllegalActions.addAll(Actions.ALL_ACTIONS.stream().filter(a -> DefaultGoalObstructedHandler.performerAttacked(a)).collect(Collectors.toList()));
		defaultIllegalActions.addAll(DefaultGoalObstructedHandler.getNonAttackingIllegalActions());
		for(ManagedOperation action : defaultIllegalActions) {
			LegalAction legalAction = new LegalAction(action, new AttackActionLegalHandler());
			legalActions.put(legalAction, Boolean.FALSE);
		}
		
		for(Deity deity : Deity.ALL_DEITIES) {
			LegalAction legalAction = new LegalAction(Actions.WORSHIP_DEITY_ACTION, new WorshipDeityLegalHandler(deity));
			legalActions.put(legalAction, Boolean.TRUE);
		}
		
		LegalAction legalAction = new LegalAction(Actions.BUTCHER_ACTION, new ButcherLegalHandler());
		legalActions.put(legalAction, Boolean.FALSE);
		
		LegalAction unlockUnownedContainerLegalAction = new LegalAction(Actions.UNLOCK_MAGIC_SPELL_ACTION, new UnlockUnownedContainerLegalHandler());
		legalActions.put(unlockUnownedContainerLegalAction, Boolean.FALSE);
		
		unlockUnownedContainerLegalAction = new LegalAction(Actions.OPEN_LOCK_ACTION, new UnlockUnownedContainerLegalHandler());
		legalActions.put(unlockUnownedContainerLegalAction, Boolean.FALSE);
		
		return new LegalActions(legalActions);
	}
}
