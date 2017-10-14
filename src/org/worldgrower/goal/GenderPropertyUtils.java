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
import org.worldgrower.attribute.Gender;
import org.worldgrower.condition.WorldStateChangedListeners;

public class GenderPropertyUtils {

	public static boolean hasSameGender(WorldObject performer, WorldObject w) {
		Gender performerGender = performer.getProperty(Constants.GENDER);
		return performerGender == w.getProperty(Constants.GENDER);
	}
	
	public static boolean isMale(WorldObject performer) {
		return performer.getProperty(Constants.GENDER) == Gender.MALE;
	}
	
	public static boolean isFemale(WorldObject performer) {
		return performer.getProperty(Constants.GENDER) == Gender.FEMALE;
	}

	public static void changeGender(WorldObject performer, WorldStateChangedListeners worldStateChangedListeners) {
		Gender oldGender = performer.getProperty(Constants.GENDER);
		if (isMale(performer)) {
			performer.setProperty(Constants.GENDER, Gender.FEMALE);
		} else if (isFemale(performer)) {
			performer.setProperty(Constants.GENDER, Gender.MALE);
		}
		performer.removeProperty(Constants.PREGNANCY);
		
		Gender newGender = performer.getProperty(Constants.GENDER);
		worldStateChangedListeners.genderChanged(performer, oldGender, newGender);
	}
}
