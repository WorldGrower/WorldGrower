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
package org.worldgrower.generator;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.CreatureTypeChangedListeners;
import org.worldgrower.generator.WorldGenerator.AddWorldObjectFunction;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.terrain.TerrainType;

public class CowOnTurn implements OnTurn {

	private final AddWorldObjectFunction addWorldObjectFunction;
	
	public CowOnTurn(AddWorldObjectFunction addWorldObjectFunction) {
		this.addWorldObjectFunction = addWorldObjectFunction;
	}
	
	@Override
	public void onTurn(WorldObject worldObject, World world, CreatureTypeChangedListeners creatureTypeChangedListeners) {
		int currentTurn = world.getCurrentTurn().getValue();
		
		if ((currentTurn > 0) && (currentTurn % 100 == 0)) {
			worldObject.increment(Constants.MEAT_SOURCE, 1);
		}
		
		if ((currentTurn > 0) && (currentTurn % 500 == 0)) {
			int performerX = worldObject.getProperty(Constants.X);
			int performerY = worldObject.getProperty(Constants.Y);
			int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
			if (position != null) {
				int x = position[0] + performerX;
				int y = position[1] + performerY;
				if (!LocationUtils.areInvalidCoordinates(x, y, world)) {
					TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
					if (terrainType != TerrainType.WATER) {
						if (getSurroundingWorldObjects(worldObject, world).size() < 2) {
							addWorldObjectFunction.addToWorld(x, y, world);
						}
					}
				}
			}
		}
	}
	
	private List<WorldObject> getSurroundingWorldObjects(WorldObject worldObject, World world) {
		return world.findWorldObjectsByProperty(Constants.FOOD_SOURCE, w -> Reach.evaluateTarget(worldObject, null, w, 1) == 0);
	}
}
