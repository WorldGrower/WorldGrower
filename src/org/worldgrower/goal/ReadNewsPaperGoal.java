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
import java.util.Iterator;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class ReadNewsPaperGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 1;
	
	public ReadNewsPaperGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		List<WorldObject> unreadNewsPapers = getUnreadNewsPapers(performer);
		if (unreadNewsPapers.size() > 0) {
			//TODO: should use index of unreadNewsPapers.get(0)
			return new OperationInfo(performer, performer, new int[] { performerInventory.getIndexFor(Constants.KNOWLEDGE_MAP) }, Actions.READ_ITEM_IN_INVENTORY_ACTION);
		} else {
			List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Item.NEWS_PAPER, QUANTITY_TO_BUY, world);
			targets = filterTargetsOnNewInformationInNewsPaper(performer, targets);
			if (targets.size() > 0) {
				return BuySellUtils.create(performer, targets.get(0), Item.NEWS_PAPER, QUANTITY_TO_BUY, world);
			} else {
				return null;
			}
		}
	}

	private List<WorldObject> filterTargetsOnNewInformationInNewsPaper(WorldObject performer, List<WorldObject> targets) {
		List<WorldObject> filteredTargets = new ArrayList<>(targets);
		Iterator<WorldObject> targetIterator = filteredTargets.iterator();
		while(targetIterator.hasNext()) {
			WorldObject target = targetIterator.next(); 
			List<WorldObject> unreadNewsPapers = getUnreadNewsPapers(performer, target);
			if (unreadNewsPapers.size() == 0) {
				targetIterator.remove();
			}
		}
		
		return filteredTargets;
	}

	List<WorldObject> getUnreadNewsPapers(WorldObject performer) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		KnowledgeMap performerKnowledge = performer.getProperty(Constants.KNOWLEDGE_MAP);
		return performerInventory.getWorldObjectsByFunction(Constants.KNOWLEDGE_MAP, w -> !performerKnowledge.hasAllKnowledge(w.getProperty(Constants.KNOWLEDGE_MAP)));
	}
	
	List<WorldObject> getUnreadNewsPapers(WorldObject performer, WorldObject target) {
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		KnowledgeMap performerKnowledge = performer.getProperty(Constants.KNOWLEDGE_MAP);
		return targetInventory.getWorldObjectsByFunction(Constants.KNOWLEDGE_MAP, w -> !performerKnowledge.hasAllKnowledge(w.getProperty(Constants.KNOWLEDGE_MAP)));
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int performerGold = performer.getProperty(Constants.GOLD);
		Integer newspaperReadTurnInt = performer.getProperty(Constants.NEWSPAPER_READ_TURN);
		int newspaperReadTurn = (newspaperReadTurnInt != null ? newspaperReadTurnInt.intValue() : 0);
		int currentTurn = world.getCurrentTurn().getValue();
		return (performerGold < 100) || (currentTurn - newspaperReadTurn < 500);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for a newspaper";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
