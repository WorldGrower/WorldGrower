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
package org.worldgrower.goal;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectFacade;

public class FacadeUtils {

	public static WorldObject createFacade(WorldObject worldObject, WorldObject performer, WorldObject target) {
		if (worldObject != null) {
			WorldObject facade = worldObject.getProperty(Constants.FACADE);
						
			if ((facade != null) && (facadeIsBelieved(performer, target))) {
				performer.getProperty(Constants.BLUFF_SKILL).use();
				return new WorldObjectFacade(worldObject, facade);
			} else {
				return worldObject;
			}
		} else {
			return null;
		}
	}
	
	public static WorldObject createFacadeForSelf(WorldObject performer) {
		final WorldObject performerFacade;
		if (performer.getProperty(Constants.FACADE) != null) {
			performerFacade = new WorldObjectFacade(performer, performer.getProperty(Constants.FACADE));
		} else {
			performerFacade = performer;
		}
		return performerFacade;
	}

	private static boolean facadeIsBelieved(WorldObject performer, WorldObject target) {
		int bluffSkill = performer.getProperty(Constants.BLUFF_SKILL).getLevel();
		int insightSkill = target.getProperty(Constants.INSIGHT_SKILL).getLevel();
		
		return bluffSkill > insightSkill;
	}
}
