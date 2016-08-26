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
package org.worldgrower.actions.legal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.BuildingGenerator;

public class UTestUnlockUnownedContainerLegalHandler {

	private UnlockUnownedContainerLegalHandler legalHandler = new UnlockUnownedContainerLegalHandler();
	
	@Test
	public void testIsApplicable() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BUILDINGS, new BuildingList());
		int targetId = BuildingGenerator.generateHouse(0, 0, world, performer);
		WorldObject target = world.findWorldObjectById(targetId);
		performer.getProperty(Constants.BUILDINGS).add(targetId, BuildingType.HOUSE);
		
		assertEquals(false, legalHandler.isApplicable(performer, target, Args.EMPTY));
		
		performer.getProperty(Constants.BUILDINGS).remove(targetId);
		assertEquals(true, legalHandler.isApplicable(performer, target, Args.EMPTY));
	}
}
