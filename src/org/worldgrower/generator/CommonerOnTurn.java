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

import org.worldgrower.CommonerNameGenerator;
import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.gui.CommonerImageIds;

public class CommonerOnTurn implements OnTurn {

	private final CommonerImageIds commonerImageIds;
	private final CommonerNameGenerator commonerNameGenerator;
	private final WorldObject organization;
	
	public CommonerOnTurn(CommonerImageIds commonerImageIds, CommonerNameGenerator commonerNameGenerator, WorldObject organization) {
		this.commonerImageIds = commonerImageIds;
		this.commonerNameGenerator = commonerNameGenerator;
		this.organization = organization;
	}

	@Override
	public void onTurn(WorldObject worldObject, World world) {
		worldObject.increment(Constants.FOOD, -1);
		worldObject.increment(Constants.WATER, -1);
		worldObject.increment(Constants.SOCIAL, -1);
		worldObject.increment(Constants.ENERGY, -1);
		
		Integer pregnancy = worldObject.getProperty(Constants.PREGNANCY);
		if (pregnancy != null) {
			pregnancy = pregnancy + 1;
			worldObject.setProperty(Constants.PREGNANCY, pregnancy);
			
			if (pregnancy > 200) {
				int performerX = worldObject.getProperty(Constants.X);
				int performerY = worldObject.getProperty(Constants.Y);
				int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
				if (position != null) {
					int id = CommonerGenerator.generateCommoner(position[0] + performerX, position[1] + performerY, world, commonerImageIds, commonerNameGenerator, organization);
					worldObject.setProperty(Constants.PREGNANCY, null);
					worldObject.getProperty(Constants.CHILDREN).add(id);
				}
			}
		}
	}
}
