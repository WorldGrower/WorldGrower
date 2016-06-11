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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;

public class UTestLegalActionsPropertyUtils {

	@Test
	public void testToList() {
		Map<LegalAction, Boolean> legalActionsMap = new HashMap<>();
		legalActionsMap.put(LegalAction.MELEE_ATTACK, Boolean.TRUE);
		legalActionsMap.put(LegalAction.FIRE_BOLT, Boolean.FALSE);
		LegalActions legalActions = new LegalActions(legalActionsMap);
		List<LegalAction> legalActionsList = legalActions.toList();
		
		assertEquals(2, legalActionsList.size());
		assertEquals(Actions.FIRE_BOLT_ATTACK_ACTION, legalActionsList.get(0).getAction());
		assertEquals(Actions.MELEE_ATTACK_ACTION, legalActionsList.get(1).getAction());
	}
	
	@Test
	public void testLegalActionsToArgs() {
		Map<LegalAction, Boolean> legalFlagsMap = new HashMap<>();
		legalFlagsMap.put(LegalAction.MELEE_ATTACK, Boolean.TRUE);
		legalFlagsMap.put(LegalAction.FIRE_BOLT, Boolean.FALSE);
		int[] legalActionsToArgs = LegalActions.legalFlagsToArgs(legalFlagsMap);
		assertEquals(2, legalActionsToArgs.length);
		assertEquals(0, legalActionsToArgs[0]);
		assertEquals(1, legalActionsToArgs[1]);
	}
}
