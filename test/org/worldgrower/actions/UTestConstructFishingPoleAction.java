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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestConstructFishingPoleAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = createWorkbench(world, performer);
		Actions.CONSTRUCT_FISHING_POLE_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FISHING_POLE_QUALITY));
	}
	
	private WorldObject createWorkbench(World world, WorldObject performer) {
		int workbenchId = BuildingGenerator.generateWorkbench(0, 0, world, performer);
		WorldObject workbench = world.findWorldObjectById(workbenchId);
		return workbench;
	}
}