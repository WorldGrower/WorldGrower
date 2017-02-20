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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class WeaveLeatherArmorGoal implements Goal {

	public WeaveLeatherArmorGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Integer weaveryId = BuildingGenerator.getWeaveryId(performer);
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.LEATHER) < 10) {
			return Goals.LEATHER_GOAL.calculateGoal(performer, world);
		} else if (weaveryId != null){
			int leatherShirtCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.LEATHER_SHIRT).size();
			int leatherPantsCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.LEATHER_PANTS).size();
			int leatherBootsCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.LEATHER_BOOTS).size();
			
			WorldObject weavery = world.findWorldObjectById(weaveryId);
			if (leatherShirtCount == 0){
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_LEATHER_SHIRT_ACTION);
			} else if (leatherPantsCount < leatherShirtCount) {
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_LEATHER_PANTS_ACTION);
			} else if (leatherBootsCount < leatherShirtCount) {
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_LEATHER_BOOTS_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return (inventory.getQuantityFor(Constants.ARMOR, Constants.QUANTITY, Item::isLeatherEquipment) >= 10);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_WEAVE_LEATHER_ARMOR);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return inventory.getQuantityFor(Constants.ARMOR);
	}
}