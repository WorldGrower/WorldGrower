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
import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.Actions;

public class UTestLegalActionsPropertyUtils {

	@Test
	public void testToList() {
		Map<ManagedOperation, Boolean> legalActions = new HashMap<>();
		legalActions.put(Actions.MELEE_ATTACK_ACTION, Boolean.TRUE);
		legalActions.put(Actions.FIRE_BOLT_ATTACK_ACTION, Boolean.FALSE);
		List<ManagedOperation> legalActionsList = LegalActionsPropertyUtils.toList(legalActions);
		
		assertEquals(2, legalActionsList.size());
		assertEquals(Actions.FIRE_BOLT_ATTACK_ACTION, legalActionsList.get(0));
		assertEquals(Actions.MELEE_ATTACK_ACTION, legalActionsList.get(1));
	}
	
	@Test
	public void testLegalActionsToArgs() {
		Map<ManagedOperation, Boolean> legalActions = new HashMap<>();
		legalActions.put(Actions.MELEE_ATTACK_ACTION, Boolean.TRUE);
		legalActions.put(Actions.FIRE_BOLT_ATTACK_ACTION, Boolean.FALSE);
		int[] legalActionsToArgs = LegalActionsPropertyUtils.legalActionsToArgs(legalActions);
		assertEquals(2, legalActionsToArgs.length);
		assertEquals(0, legalActionsToArgs[0]);
		assertEquals(1, legalActionsToArgs[1]);
	}
}
