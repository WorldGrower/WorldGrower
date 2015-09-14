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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;

public class ArenaFightGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> ArenaPropertyUtils.worldObjectOwnsArena(w));
		if (targets.size() > 0) {
			if (ArenaPropertyUtils.personIsArenaFighter(performer, targets.get(0))) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.START_ARENA_FIGHT_CONVERSATION), Actions.TALK_ACTION);
			} else {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.BECOME_ARENA_FIGHTER_CONVERSATION), Actions.TALK_ACTION);
			}
		} else {
			return null;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return ArenaPropertyUtils.personIsScheduledToFight(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to fight in an arena";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return ArenaPropertyUtils.personIsScheduledToFight(performer) ? 1 : 0;
	}

}
