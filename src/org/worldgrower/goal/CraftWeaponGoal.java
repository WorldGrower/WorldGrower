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

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.ItemGenerator;

public class CraftWeaponGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 10) {
			return new WoodGoal().calculateGoal(performer, world);
		} else if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.ORE) < 7) {
			return new OreGoal().calculateGoal(performer, world);
		} else {
			int ironClaymoreCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.IRON_CLAYMORE_NAME).size();
			int ironCuirassCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.IRON_CUIRASS_NAME).size();
			int ironHelmetCount = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.IRON_HELMET_NAME).size();
			
			if (ironClaymoreCount == 0){
				return new OperationInfo(performer, performer, new int[0], Actions.CRAFT_IRON_CLAYMORE_ACTION);
			} else if (ironClaymoreCount < ironCuirassCount) {
				return new OperationInfo(performer, performer, new int[0], Actions.CRAFT_IRON_CLAYMORE_ACTION);
			} else if (ironHelmetCount < ironCuirassCount) {
				return new OperationInfo(performer, performer, new int[0], Actions.CRAFT_IRON_HELMET_ACTION);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return (inventory.getQuantityFor(Constants.DAMAGE) >= 10);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "crafting weapons";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return inventory.getQuantityFor(Constants.DAMAGE);
	}
}