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

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Demands;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class TradeGoal implements Goal {

	private static final int DISTANCE = 20;
	private final Demands buyingProperties;
	
	public TradeGoal(Demands buyingProperties) {
		this.buyingProperties = buyingProperties;
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		OperationInfo sellOperationInfo = BuySellUtils.getSellOperationInfo(performer, world, DISTANCE);
		if (sellOperationInfo != null) {
			return sellOperationInfo;
		}
		
		OperationInfo buyOperationInfo = BuySellUtils.getBuyOperationInfo(performer, buyingProperties, world);
		if (buyOperationInfo != null) {
			return buyOperationInfo;
		}
		
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		//TODO: implement count of matching demands and sellable items
		return false;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_TRADE);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}