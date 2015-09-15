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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.ArenaPropertyUtils;

public class ArenaFightListener implements ManagedOperationListener {

	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		
		if (ArenaPropertyUtils.peopleAreFightingEachOther(performer, target)) {
			if (target.getProperty(Constants.HIT_POINTS).intValue() <= 1 || target.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION)) {
				
				ArenaPropertyUtils.addPayCheck(performer);
				ArenaPropertyUtils.addPayCheck(target);
				
				performer.setProperty(Constants.ARENA_OPPONENT_ID, null);
				target.setProperty(Constants.ARENA_OPPONENT_ID, null);
			}
		}
	}
}
