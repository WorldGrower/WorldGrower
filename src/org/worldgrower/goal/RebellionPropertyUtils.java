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
import org.worldgrower.personality.PersonalityTrait;

public class RebellionPropertyUtils {

	public static boolean wantsToRebel(WorldObject performer, WorldObject leader) {
		int relationshipValue = performer.getProperty(Constants.RELATIONSHIPS).getValue(leader);
		
		if (performer.hasProperty(Constants.PERSONALITY)) {
			boolean isForgiving = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.FORGIVING) > 100;
			if (!isForgiving) {
				relationshipValue = Constants.RELATIONSHIP_VALUE.normalize(relationshipValue - 100);
			}
		}
		
		return Constants.RELATIONSHIP_VALUE.isAtMin(relationshipValue);
	}
}
