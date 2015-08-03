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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.gui.ImageIds;

public class CreateGraveAction implements BuildAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		int indexOfRemains = inventory.getIndexFor(Constants.DECEASED_WORLD_OBJECT);
		WorldObject deceasedWorldObject = inventory.get(indexOfRemains);
		inventory.remove(indexOfRemains);
		
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, 1);
		properties.put(Constants.HEIGHT, 1);
		properties.put(Constants.NAME, "grave");
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.GRAVE);
		properties.put(Constants.TEXT, "Here are the " + deceasedWorldObject.getProperty(Constants.NAME) + " buried");
		properties.put(Constants.HIT_POINTS, 50);
		properties.put(Constants.HIT_POINTS_MAX, 50);
		properties.put(Constants.ARMOR, 0);
		properties.put(Constants.DAMAGE_RESIST, 0);
		
		WorldObject house = new WorldObjectImpl(properties);
		world.addWorldObject(house);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int remains = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.DECEASED_WORLD_OBJECT) != -1 ? 0 : 1;
		return Reach.evaluateTarget(performer, args, target, 1) + remains;
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		if (target.hasProperty(Constants.ID)) {
			return false;
		} else {
			int x = (Integer)target.getProperty(Constants.X);
			int y = (Integer)target.getProperty(Constants.Y);
			return GoalUtils.isOpenSpace(x, y, 1, 1, world);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating grave";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "create grave";
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}
}