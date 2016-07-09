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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestFiretrapOnTurn {

	@Test
	public void testOnTurnExplosion() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		
		WorldObject rat = createRat(world, organization);
		int id = TerrainGenerator.generateFireTrap(rat.getProperty(Constants.X), rat.getProperty(Constants.Y), world, 1);
		
	
		assertEquals(2 * Item.COMBAT_MULTIPLIER, rat.getProperty(Constants.HIT_POINTS).intValue());
		
		WorldObject firetrap = world.findWorldObjectById(id);
		firetrap.onTurn(world, new WorldStateChangedListeners());
		
		assertEquals(0, rat.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals(false, world.exists(firetrap));
	}
	
	private WorldObject createRat(World world, WorldObject organization) {
		int ratId = new CreatureGenerator(organization).generateRat(0, 0, world);
		return world.findWorldObjectById(ratId);
	}
}