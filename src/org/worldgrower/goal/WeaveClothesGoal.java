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

public class WeaveClothesGoal implements Goal {

	public WeaveClothesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Integer weaveryId = BuildingGenerator.getWeaveryId(performer);
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.COTTON) < 10) {
			return Goals.COTTON_GOAL.calculateGoal(performer, world);
		} else if (weaveryId != null){
			int cottonShirtCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, Item.COTTON_SHIRT_NAME).size();
			int cottonPantsCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, Item.COTTON_PANTS_NAME).size();
			int cottonBootsCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, Item.COTTON_BOOTS_NAME).size();
			
			WorldObject weavery = world.findWorldObjectById(weaveryId);
			if (cottonShirtCount == 0){
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_COTTON_SHIRT_ACTION);
			} else if (cottonPantsCount < cottonShirtCount) {
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_COTTON_PANTS_ACTION);
			} else if (cottonBootsCount < cottonShirtCount) {
				return new OperationInfo(performer, weavery, Args.EMPTY, Actions.WEAVE_COTTON_BOOTS_ACTION);
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
		return (inventory.getQuantityFor(Constants.ARMOR) >= 10);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to weave clothes";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return inventory.getQuantityFor(Constants.ARMOR);
	}
}