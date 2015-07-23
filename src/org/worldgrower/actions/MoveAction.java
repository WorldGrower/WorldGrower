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
import java.util.List;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.CreaturePositionCondition;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;

public class MoveAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		
		performer.setProperty(Constants.X, performerX + args[0]);
		performer.setProperty(Constants.Y, performerY + args[1]);
		
		setLookDirection(performer, args);
	}

	private void setLookDirection(WorldObject performer, int[] args) {
		if (performer.hasProperty(Constants.LOOK_DIRECTION)) {
			if (args[0] == 0) {
				if (args[1] > 0) {
					performer.setProperty(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
				} else {
					performer.setProperty(Constants.LOOK_DIRECTION, LookDirection.NORTH);
				}
			} else if (args[0] > 0) {
				performer.setProperty(Constants.LOOK_DIRECTION, LookDirection.EAST);
			} else if (args[0] < 0) {
				performer.setProperty(Constants.LOOK_DIRECTION, LookDirection.WEST);
			} else {
				performer.setProperty(Constants.LOOK_DIRECTION, LookDirection.SOUTH);
			}
		}
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		
		int newX = performerX + args[0];
		int newY = performerY + args[1];
		
		if ((newX < 0) || (newY < 0) || (newX >= world.getWidth()) || (newY >= world.getHeight())) {
			return 1;
		} else {
			List<WorldObject> actors = world.findWorldObjects(new CreaturePositionCondition(performer.getProperty(Constants.Y) + args[1], performer.getProperty(Constants.X) + args[0]));
			return actors.size();
		}
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[2];
		argumentRanges[0] = new ArgumentRange(-1, 1);
		argumentRanges[1] = new ArgumentRange(-1, 1);
		return argumentRanges;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int performerId = performer.getProperty(Constants.ID);
		int targetId = target.getProperty(Constants.ID);
		return (performerId == targetId);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "moving";
	}

	@Override
	public String getSimpleDescription() {
		return "move";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}
