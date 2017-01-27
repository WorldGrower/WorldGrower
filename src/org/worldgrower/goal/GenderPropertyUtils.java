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
import org.worldgrower.condition.WorldStateChangedListeners;

public class GenderPropertyUtils {
	private static final String MALE = "male";
	private static final String FEMALE = "female";

	public static boolean hasSameGender(WorldObject performer, WorldObject w) {
		String performerGender = performer.getProperty(Constants.GENDER);
		return performerGender.equals(w.getProperty(Constants.GENDER));
	}
	
	public static boolean isMale(WorldObject performer) {
		return performer.getProperty(Constants.GENDER).equals(MALE);
	}
	
	public static boolean isFemale(WorldObject performer) {
		return performer.getProperty(Constants.GENDER).equals(FEMALE);
	}

	public static void changeGender(WorldObject performer, WorldStateChangedListeners worldStateChangedListeners) {
		String oldGender = performer.getProperty(Constants.GENDER);
		if (isMale(performer)) {
			performer.setProperty(Constants.GENDER, FEMALE);
		}
		if (isFemale(performer)) {
			performer.setProperty(Constants.GENDER, MALE);
		}
		performer.removeProperty(Constants.PREGNANCY);
		
		String newGender = performer.getProperty(Constants.GENDER);
		worldStateChangedListeners.genderChanged(performer, oldGender, newGender);
	}
}
