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
import org.worldgrower.creaturetype.CreatureType;

public class DeityPropertyUtils {

	public static WorldObject findDeity(String name, World world) {
		List<WorldObject> deities = world.findWorldObjects(
				w -> w.getProperty(Constants.NAME).equals(name) 
					&& w.getProperty(Constants.CREATURE_TYPE) == CreatureType.DEITY_CREATURE_TYPE);
		if (deities.size() == 1) {
			return deities.get(0);
		} else {
			throw new IllegalStateException("Deity " + name + " not found");
		}
	}
}
