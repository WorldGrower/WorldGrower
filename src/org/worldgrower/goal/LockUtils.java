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
import org.worldgrower.WorldObject;

public class LockUtils {

	public static int distance(WorldObject performer, WorldObject target) {
		if (target.hasProperty(Constants.LOCKED) && target.getProperty(Constants.LOCKED)) {
			if (performerHasKey(performer, target) || performerIsMagicLockCreator(performer, target)) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	private static boolean performerHasKey(WorldObject performer, WorldObject target) {
		List<WorldObject> keys = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.LOCK_ID, target.getProperty(Constants.ID));
		return keys.size() > 0;
	}
	
	private static boolean performerIsMagicLockCreator(WorldObject performer, WorldObject target) {
		if (target.getProperty(Constants.MAGIC_LOCK_CREATOR_ID) != null) {
			int magicLockCreatorId = target.getProperty(Constants.MAGIC_LOCK_CREATOR_ID);
			if (performer.getProperty(Constants.ID).intValue() == magicLockCreatorId) {
				return true;
			}
		}
		return false;
	}
}
