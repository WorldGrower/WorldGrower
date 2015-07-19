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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class GroupPropertyUtils {

	public static boolean isWorldObjectPotentialEnemy(WorldObject performer, WorldObject w) {
		String performerGroup = performer.getProperty(Constants.GROUP);
		return w.hasIntelligence() && (w != performer) && (w.getProperty(Constants.GROUP) == null || !w.getProperty(Constants.GROUP).equals(performerGroup));
	}
	
	public static List<WorldObject> findWorldObjectsInSameGroup(WorldObject performer, World world) {
		String performerGroup = performer.getProperty(Constants.GROUP);
		return world.findWorldObjects(w -> w.hasIntelligence() && (w.getProperty(Constants.GROUP) != null) && w.getProperty(Constants.GROUP).equals(performerGroup));
	}
	
	public static void throwPerformerOutGroup(WorldObject performer, WorldObject w) {
		performer.setProperty(Constants.GROUP, null);
	}
}
