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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.OperationInfo;
import org.worldgrower.TaskCalculator;
import org.worldgrower.TaskCalculatorImpl;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.WorldGenerator.AddWorldObjectFunction;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.terrain.TerrainType;

public class CattleOnTurn implements OnTurn {

	private static final int PREGNANCY_DURATION = 600;
	private static final int MAX_MEAT_SOURCE = 10;
	
	private final AddWorldObjectFunction addWorldObjectFunction;
	
	public CattleOnTurn(AddWorldObjectFunction addWorldObjectFunction) {
		this.addWorldObjectFunction = addWorldObjectFunction;
	}
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		int currentTurn = world.getCurrentTurn().getValue();
		
		if ((currentTurn > 0) && (currentTurn % 100 == 0)) {
			if (worldObject.getProperty(Constants.MEAT_SOURCE).intValue() < MAX_MEAT_SOURCE) {
				worldObject.increment(Constants.MEAT_SOURCE, 1);
			}
		}

		worldObject.getProperty(Constants.CONDITIONS).onTurn(worldObject, world, creatureTypeChangedListeners);
		
		checkPregnancy(worldObject, world, currentTurn);
		checkLeash(worldObject, world);
	}
	
	public static boolean cattleIsFullyGrown(WorldObject cattle) {
		return cattle.getProperty(Constants.MEAT_SOURCE).intValue() == MAX_MEAT_SOURCE;
	}

	private void checkLeash(WorldObject worldObject, World world) {
		Integer leashId = worldObject.getProperty(Constants.LEASH_ID);
		if (leashId != null) {
			WorldObject leashOwner = world.findWorldObjectById(leashId);
			TaskCalculator taskCalculator = new TaskCalculatorImpl(world);
			OperationInfo meleeAttackOperationInfo = new OperationInfo(worldObject, leashOwner, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
			List<OperationInfo> tasks = taskCalculator.calculateTask(worldObject, world, meleeAttackOperationInfo);
			if (tasks.size() > 1) {
				OperationInfo moveOperationInfo = tasks.get(0);
				int newX = worldObject.getProperty(Constants.X) + moveOperationInfo.getArgs()[0];
				int newY = worldObject.getProperty(Constants.Y) + moveOperationInfo.getArgs()[1];
				LocationPropertyUtils.updateLocation(worldObject, newX, newY, world);
			}
		}
		
	}

	private void checkPregnancy(WorldObject worldObject, World world, int currentTurn) {
		Integer pregnancy = worldObject.getProperty(Constants.PREGNANCY);
		if (pregnancy != null) {
			pregnancy = pregnancy + 1;
			worldObject.setProperty(Constants.PREGNANCY, pregnancy);
			
			if (pregnancy > PREGNANCY_DURATION) {
				int performerX = worldObject.getProperty(Constants.X);
				int performerY = worldObject.getProperty(Constants.Y);
				int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
				if (position != null) {
					int x = position[0] + performerX;
					int y = position[1] + performerY;
					if (!LocationUtils.areInvalidCoordinates(x, y, world)) {
						TerrainType terrainType = world.getTerrain().getTerrainType(x, y);
						if (terrainType != TerrainType.WATER) {
							int childId = addWorldObjectFunction.addToWorld(x, y, world);
							WorldObject child = world.findWorldObjectById(childId);
							child.setProperty(Constants.CATTLE_OWNER_ID, worldObject.getProperty(Constants.CATTLE_OWNER_ID));
							
							worldObject.removeProperty(Constants.PREGNANCY);
						}
					}
				}
			}
		}
	}
}
