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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.generator.BuildingGenerator;

public class UTestJailCache {

	@Test
	public void testAddGetRemove() {
		World world = new WorldImpl(20, 20, null, null);
		assertEquals(0, world.getWorldObjectsCache().getWorldObjectsFor(0, 0).size());
		
		BuildingGenerator.generateJail(0, 0, world, 1f);
		assertEquals(1, world.getWorldObjectsCache().getWorldObjectsFor(0, 0).size());
		
		Integer jailId = world.getWorldObjectsCache().getWorldObjectsFor(0, 0).get(0).getProperty(Constants.ID);
		world.removeWorldObject(world.findWorldObject(Constants.ID, jailId));
		
		assertEquals(0, world.getWorldObjectsCache().getWorldObjectsFor(0, 0).size());
	}
}
