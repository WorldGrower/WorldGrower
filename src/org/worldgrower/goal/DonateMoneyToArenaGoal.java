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

public class DonateMoneyToArenaGoal implements Goal {

	private static final int NUMBER_OF_TURNS_BETWEEN_DONATIONS = 200;
	
	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> ArenaPropertyUtils.worldObjectOwnsArena(w));
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int performerGold = performer.getProperty(Constants.GOLD);
			if (performerGold > 50) {
				return new OperationInfo(performer, target, new int[] { 5 }, Actions.DONATE_MONEY_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int money = performer.getProperty(Constants.GOLD);
		boolean hasInsufficientMoney = (money < 50);
		boolean donatedRecently = ArenaPropertyUtils.getTurnsSinceLastDonation(performer, world) < NUMBER_OF_TURNS_BETWEEN_DONATIONS;
		return hasInsufficientMoney || donatedRecently;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "donating money to the arena";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}

}
