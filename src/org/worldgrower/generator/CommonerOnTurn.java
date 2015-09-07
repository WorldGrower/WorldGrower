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

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Background;
import org.worldgrower.goal.DrownUtils;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.WeightPropertyUtils;

public class CommonerOnTurn implements OnTurn {

	private final CommonerGenerator commonerGenerator;
	private final WorldObject organization;
	
	public CommonerOnTurn(CommonerGenerator commonerGenerator, WorldObject organization) {
		this.commonerGenerator = commonerGenerator;
		this.organization = organization;
	}

	@Override
	public void onTurn(WorldObject worldObject, World world) {
		worldObject.increment(Constants.FOOD, -1);
		worldObject.increment(Constants.WATER, -1);
		worldObject.increment(Constants.SOCIAL, -1);
		worldObject.increment(Constants.ENERGY, -1);
		
		if (worldObject.getProperty(Constants.FOOD) < 100 || worldObject.getProperty(Constants.WATER) < 100) {
			worldObject.increment(Constants.ENERGY, -1);
		}
		
		if (WeightPropertyUtils.isCarryingTooMuch(worldObject)) {
			worldObject.increment(Constants.ENERGY, -1);
		}
		
		worldObject.getProperty(Constants.CONDITIONS).onTurn(worldObject, world);
		Background background = worldObject.getProperty(Constants.BACKGROUND);
		if (background != null) {
			background.checkForNewGoals(worldObject, world);
		}
		
		DrownUtils.checkForDrowning(worldObject, world);
		
		Integer pregnancy = worldObject.getProperty(Constants.PREGNANCY);
		if (pregnancy != null) {
			pregnancy = pregnancy + 1;
			worldObject.setProperty(Constants.PREGNANCY, pregnancy);
			
			if (pregnancy > 200) {
				int performerX = worldObject.getProperty(Constants.X);
				int performerY = worldObject.getProperty(Constants.Y);
				int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
				if (position != null) {
					int id = commonerGenerator.generateCommoner(position[0] + performerX, position[1] + performerY, world, organization);
					worldObject.setProperty(Constants.PREGNANCY, null);
					worldObject.getProperty(Constants.CHILDREN).add(id);
				}
			}
		}
	}
}
