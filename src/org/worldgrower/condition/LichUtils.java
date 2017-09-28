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
package org.worldgrower.condition;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;
import org.worldgrower.gui.ImageIds;

public class LichUtils {

	private static final ImageIds LICH_IMAGE_ID = ImageIds.LICH;

	public static void lichifyPerson(WorldObject worldObject, WorldStateChangedListeners creatureTypeChangedListeners) {
		worldObject.setProperty(Constants.IMAGE_ID, LICH_IMAGE_ID);
		worldObject.setProperty(Constants.CREATURE_TYPE, CreatureType.LICH_CREATURE_TYPE);
		worldObject.setProperty(Constants.CURSE, Curse.LICH_CURSE);
		
		creatureTypeChangedListeners.fireCreatureTypeChanged(worldObject, CreatureType.LICH_CREATURE_TYPE, "You have become a lich");
	}
	
	public static boolean isLich(WorldObject performer) {
		return performer.getProperty(Constants.CURSE) == Curse.LICH_CURSE;
	}
	
	public static ImageIds getImageId() {
		return LICH_IMAGE_ID;
	}
}
