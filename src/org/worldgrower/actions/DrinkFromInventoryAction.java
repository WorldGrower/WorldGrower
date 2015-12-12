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

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.AlcoholLevelPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class DrinkFromInventoryAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		
		performer.increment(Constants.WATER, 100);
		
		WorldObject waterTarget = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
		if (waterTarget.hasProperty(Constants.POISON_DAMAGE) && waterTarget.getProperty(Constants.POISON_DAMAGE) > 0) {
			Conditions.add(performer, Condition.POISONED_CONDITION, 20, world);
		}
		
		if (waterTarget.hasProperty(Constants.ALCOHOL_LEVEL)) {
			performer.increment(Constants.ALCOHOL_LEVEL, waterTarget.getProperty(Constants.ALCOHOL_LEVEL));
			if (performer.getProperty(Constants.ALCOHOL_LEVEL) > AlcoholLevelPropertyUtils.getIntoxicatedLimit(performer)) {
				Conditions.add(performer, Condition.INTOXICATED_CONDITION, Integer.MAX_VALUE, world);
			}
		}
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.WATER, 1);
	}

	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return inventoryItem.hasProperty(Constants.WATER);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WATER, 1);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "drinking from " + target.getProperty(Constants.NAME);
	}
	
	@Override
	public String getSimpleDescription() {
		return "drink from inventory";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.WATER;
	}
}