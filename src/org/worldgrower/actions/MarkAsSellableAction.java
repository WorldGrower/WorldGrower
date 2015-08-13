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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.BuildingGenerator;

public class MarkAsSellableAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		target.setProperty(Constants.SELLABLE, Boolean.TRUE);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return BuildingGenerator.isSellable(target) && performer.getProperty(Constants.HOUSES).contains(target);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, 1);
		int sellableDistance = target.hasProperty(Constants.SELLABLE) && target.getProperty(Constants.SELLABLE) ? 1 : 0;
		return distanceBetweenPerformerAndTarget + sellableDistance;
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "marking a house for sale";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	
	@Override
	public String getSimpleDescription() {
		return "mark for sale";
	}
}
