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
import org.worldgrower.attribute.Prices;
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class AdjustPricesGoal implements Goal {

	public AdjustPricesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Prices prices = performer.getProperty(Constants.PRICES);
		Prices newPrices = calculatePrices(performer, world);
		if (!prices.equals(newPrices)) {
			return new OperationInfo(performer, performer, newPrices.toArgs(), Actions.SET_PRICES_ACTION);
		} else {
			return null;
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Prices prices = performer.getProperty(Constants.PRICES);
		Prices newPrices = calculatePrices(performer, world);
		return prices.equals(newPrices);
	}
	
	private Prices calculatePrices(WorldObject performer, World world) {
		Prices prices = performer.getProperty(Constants.PRICES);
		Prices newPrices = prices.copy();
		OperationInfo lastOperationInfo = world.getHistory().getLastPerformedOperation(performer);
		if (lastOperationInfo != null && lastOperationInfo.getManagedOperation() == Actions.BUY_ACTION) {
			int itemIndex = lastOperationInfo.getArgs()[3];
			Item item = Item.value(itemIndex);
			
			int buyPrice = lastOperationInfo.getArgs()[1];
			newPrices.setPrice(item, buyPrice+1);
		}
		return newPrices;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_ADJUST_PRICES);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}