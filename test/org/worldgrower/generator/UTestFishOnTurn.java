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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.MockTerrain;
import org.worldgrower.MockWorld;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.terrain.TerrainType;

public class UTestFishOnTurn {

	@Test
	public void testGiveBirth() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());

		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject fish = createFish(world, organization);
		
		assertEquals(2, world.getWorldObjects().size());
		
		world = new MockWorld(new MockTerrain(TerrainType.WATER), world);
		
		for(int i=0; i<300; i++) {
			world.nextTurn();
			fish.onTurn(world, new WorldStateChangedListeners());
		}
		
		assertEquals(3, world.getWorldObjects().size());
		
	}

	private WorldObject createFish(World world, WorldObject organization) {
		int fishId = new CreatureGenerator(organization).generateFish(0, 0, world);
		WorldObject fish = world.findWorldObjectById(fishId);
		return fish;
	}
}