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
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.gui.ImageIds;

public class BuildHouseAction implements BuildAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X) + 1;
		int y = (Integer)target.getProperty(Constants.Y) + 1;
		
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 2);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.SLEEP_COMFORT, 5);
		properties.put(Constants.NAME, "house");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.HOUSE);
		properties.put(Constants.HIT_POINTS, 200);
		properties.put(Constants.HIT_POINTS_MAX, 200);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject house = new WorldObjectImpl(properties);
		world.addWorldObject(house);
		
		Integer currentHouseId = performer.getProperty(Constants.HOUSE_ID);
		if (currentHouseId != null) {
			world.removeWorldObject(world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == currentHouseId.intValue()).get(0));
		}
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.STONE, 14);
		performer.setProperty(Constants.HOUSE_ID, house.getProperty(Constants.ID));
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		if (target.hasProperty(Constants.ID)) {
			return false;
		} else {
			int x = (Integer)target.getProperty(Constants.X) + 1;
			int y = (Integer)target.getProperty(Constants.Y) + 1;
			return GoalUtils.isOpenSpace(x, y, 2, 4, world);
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, 1);
		int stone = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE);
		if (stone < 14) {
			return (14 - stone) * 1000 + distanceBetweenPerformerAndTarget;
		} else {
			return 0 + distanceBetweenPerformerAndTarget;
		}
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a house";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return 2;
	}

	@Override
	public int getHeight() {
		return 4;
	}
	
	@Override
	public String getSimpleDescription() {
		return "build house";
	}
}
