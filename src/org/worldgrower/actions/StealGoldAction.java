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
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.ThieveryPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class StealGoldAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int amount = args[0];
		
		int targetGold = target.getProperty(Constants.GOLD);
		
		if (amount > targetGold) {
			amount = targetGold;
		}
		
		boolean isSuccess = ThieveryPropertyUtils.isThieverySuccess(performer, world, amount);
		if (isSuccess) {
			target.increment(Constants.GOLD, -amount);
			performer.increment(Constants.GOLD, amount);
			world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " succesfully steals " + amount + " gold from " + target.getProperty(Constants.NAME));
		} else {
			ThieveryPropertyUtils.addThievingKnowledge(performer, target, world);
			GroupPropertyUtils.throwPerformerOutGroup(performer, target);
			world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " was caught stealing gold from " + target.getProperty(Constants.NAME));
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.INVENTORY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "stealing gold from " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "steal gold";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.IRON_GAUNTLETS;
	}
}