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
package org.worldgrower.gui.chooseworldobject;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;

public enum WorldObjectMapper {

	WORLD_OBJECT_ID {
		@Override
		public int[] map(WorldObject playerCharacter, WorldObject worldObject) {
			int selectedId = worldObject.getProperty(Constants.ID);
			return new int[] { selectedId };
		}
	},
	INVENTORY_ID {
		@Override
		public int[] map(WorldObject playerCharacter, WorldObject worldObject) {
			WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);
			int inventoryIndex = inventory.getIndexFor(worldObject);
			return new int[] { inventoryIndex };
		}
	};
	
	public abstract int[] map(WorldObject playerCharacter, WorldObject worldObject);
}
