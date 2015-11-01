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

import static org.worldgrower.goal.FacadeUtils.createFacade;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class RelationshipPropertyUtils {

	public static void changeRelationshipValueUsingFacades(WorldObject performer, WorldObject target, int value, ManagedOperation managedOperation, int[] args, World world) {
		WorldObject performerFacade = createFacade(performer, performer, target, world);
		WorldObject targetFacade = createFacade(target, performer, target, world);
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), value);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), value);

		if (target.hasProperty(Constants.BACKGROUND) && value < 0) {
			target.getProperty(Constants.BACKGROUND).addGoalObstructed(performerFacade, target, managedOperation, args, world);
		}
	}
	
	public static void changeRelationshipValue(WorldObject performer, WorldObject target, int value, ManagedOperation managedOperation, int[] args, World world) {
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target.getProperty(Constants.ID), value);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer.getProperty(Constants.ID), value);

		if (target.hasProperty(Constants.BACKGROUND) && value < 0) {
			target.getProperty(Constants.BACKGROUND).addGoalObstructed(performer, target, managedOperation, args, world);
		}
	}
	
	public static void changeRelationshipValue(WorldObject performer, WorldObject target, int performervalue, int targetValue, ManagedOperation managedOperation, int[] args, World world) {
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target.getProperty(Constants.ID), performervalue);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer.getProperty(Constants.ID), targetValue);

		if (target.hasProperty(Constants.BACKGROUND) && targetValue < 0) {
			target.getProperty(Constants.BACKGROUND).addGoalObstructed(performer, target, managedOperation, args, world);
		}
	}
}
