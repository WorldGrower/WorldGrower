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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

public class ThrowOilAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		target.setProperty(Constants.FLAMMABLE, Boolean.TRUE);
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.OIL, 1);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		
		boolean inventoryContainsOil = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.OIL) > 0;
		return (inventoryContainsOil && (target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, 4);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "left hand must be free");
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "throwing oil at " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "throw oil";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.OIL;
	}
}
