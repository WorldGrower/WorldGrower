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
package org.worldgrower.condition;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class BurningCondition implements Condition {

	@Override
	public boolean canTakeAction() {
		return true;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public String getDescription() {
		return "burning";
	}

	@Override
	public void onTurn(WorldObject worldObject, World world, int startTurn) {
		worldObject.increment(Constants.HIT_POINTS, -5);
		
		List<WorldObject> flammableAdjacentWorldObjects = world.findWorldObjects(w -> Reach.distance(worldObject, w) <= 2 && w.hasProperty(Constants.FLAMMABLE) && w.getProperty(Constants.FLAMMABLE));
		for(WorldObject flammableAdjacentWorldObject : flammableAdjacentWorldObjects) {
			if (!flammableAdjacentWorldObject.getProperty(Constants.CONDITIONS).hasCondition(BURNING_CONDITION)) {
				flammableAdjacentWorldObject.getProperty(Constants.CONDITIONS).addCondition(BURNING_CONDITION, 100, world);
			}
		}
	}

	@Override
	public boolean isDisease() {
		return false;
	}

	@Override
	public void conditionEnds(WorldObject worldObject) {
	}
}
