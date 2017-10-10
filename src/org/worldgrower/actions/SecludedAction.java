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

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.FindSecludedLocationGoal;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class SecludedAction implements ManagedOperation {

	private final ManagedOperation action;

	public SecludedAction(ManagedOperation action) {
		this.action = action;
	}

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		action.execute(performer, target, args, world);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return action.distance(performer, target, args, world) + FindSecludedLocationGoal.getTargetsWithinRange(performer, world).size();
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return action.isActionPossible(performer, target, args, world);
	}
	
	@Override
	public String getRequirementsDescription() {
		return action.getRequirementsDescription();
	}

	@Override
	public String getDescription() {
		return action.getDescription();
	}

	@Override
	public boolean requiresArguments() {
		return action.requiresArguments();
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return action.isValidTarget(performer, target, world);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return action.getDescription(performer, target, args, world);
	}

	@Override
	public String getSimpleDescription() {
		return action.getSimpleDescription();
	}
	
	// readResolve shouldn't be called because this action isn't a constant
	// and isn't found in list of actions
	/*
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	*/
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return action.getImageIds(performer);
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return action.getSoundId(target);
	}

	@Override
	public int hashCode() {
		return action.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SecludedAction) {
			SecludedAction other = (SecludedAction) obj;
			return (action.equals(other.action));	
		} else {
			return false;
		}
	}
}
