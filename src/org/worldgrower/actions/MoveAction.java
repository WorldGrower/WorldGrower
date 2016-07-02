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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.terrain.TerrainType;

public class MoveAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		
		int newX = performerX + args[0];
		int newY = performerY + args[1];
		LocationPropertyUtils.updateLocation(performer, newX, newY, world);
		
		setLookDirection(performer, args);
	}

	void setLookDirection(WorldObject performer, int[] args) {
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
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		
		int newX = performerX + args[0];
		int newY = performerY + args[1];
		
		if (LocationUtils.areInvalidCoordinates(newX, newY, world)) {
			return 1;
		} else {
			TerrainType terrainType = world.getTerrain().getTerrainInfo(newX, newY).getTerrainType();
			boolean hasWaterWalkCondition = performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.WATER_WALK_CONDITION);
			if (terrainType == TerrainType.WATER) {
				if (hasWaterWalkCondition) {
					return 0;
				} else {
					return 1;
				}
			} else {
				List<WorldObject> obstacles = calculateObstacles(performer, world, performerX, performerY, newX, newY);
				
				if (obstacles.size() == 1) {
					if (obstacles.get(0).equals(performer)) {
						return 0;
					} else {
						return 1;
					}
				} else {
					return obstacles.size();
				}
			}
		}
	}

	private List<WorldObject> calculateObstacles(WorldObject performer, World world, int performerX, int performerY, int newX, int newY) {
		int performerWidth = performer.getProperty(Constants.WIDTH);
		int performerHeight = performer.getProperty(Constants.HEIGHT);
		List<WorldObject> obstacles = LocationPropertyUtils.getWorldObjects(newX, newY, world);
		if (performerWidth > 1 && performerY != newY) {
			obstacles.addAll(LocationPropertyUtils.getWorldObjects(newX+1, newY, world));
		}
		if (performerHeight > 1  && performerX != newX) {
			obstacles.addAll(LocationPropertyUtils.getWorldObjects(newX, newY+1, world));
		}
		obstacles = new ArrayList<>(new HashSet<>(obstacles)); // remove doubles
		return obstacles;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
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
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.COTTON_BOOTS;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.MOVE;
	}	
}
