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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;

public class UTestBerryBushOnTurn {

	@Test
	public void testOnTurn() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());

		WorldObject berryBush = createBerryBush(world);
		
		assertEquals(1, berryBush.getProperty(Constants.FOOD_SOURCE).intValue());
		
		berryBush.onTurn(world, new WorldStateChangedListeners());
		assertEquals(6, berryBush.getProperty(Constants.FOOD_SOURCE).intValue());
	}

	private WorldObject createBerryBush(World world) {
		int berryBushId = PlantGenerator.generateBerryBush(5, 5, world);
		WorldObject berryBush = world.findWorldObject(Constants.ID, berryBushId);
		return berryBush;
	}
}