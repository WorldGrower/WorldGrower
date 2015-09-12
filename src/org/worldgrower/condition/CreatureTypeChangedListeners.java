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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.WorldObject;
import org.worldgrower.creaturetype.CreatureType;

public class CreatureTypeChangedListeners {

	private final List<CreatureTypeChangedListener> creatureTypeChangedListeners = new ArrayList<>();
	
	public void addCreatureTypeChangedListener(CreatureTypeChangedListener listener) {
		creatureTypeChangedListeners.add(listener);
	}
	
	public void fireCreatureTypeChanged(WorldObject worldObject, CreatureType newCreatureType, String description) {
		for(CreatureTypeChangedListener creatureTypeChangedListener : creatureTypeChangedListeners) {
			creatureTypeChangedListener.creatureTypeChange(worldObject, newCreatureType, description);
		}
	}
}
