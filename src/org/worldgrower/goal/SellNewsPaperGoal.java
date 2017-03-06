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
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class SellNewsPaperGoal extends AbstractSellGoal {

	public SellNewsPaperGoal() {
		super(Constants.TEXT, 20);
	}

	public SellNewsPaperGoal(List<Goal> allGoals) {
		this();
		allGoals.add(this);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_SELL_NEWSPAPER, Item.NEWS_PAPER);
	}
}