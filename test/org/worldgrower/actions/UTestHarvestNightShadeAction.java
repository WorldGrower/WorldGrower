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
import org.worldgrower.generator.PlantGenerator;

public class UTestHarvestNightShadeAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		int id = PlantGenerator.generateNightShade(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		target.setProperty(Constants.NIGHT_SHADE_SOURCE, 100);
		Actions.HARVEST_NIGHT_SHADE_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.NIGHT_SHADE));
		assertEquals(90, target.getProperty(Constants.NIGHT_SHADE_SOURCE).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		int id = PlantGenerator.generateNightShade(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		target.setProperty(Constants.NIGHT_SHADE_SOURCE, 100);
		
		assertEquals(true, Actions.HARVEST_NIGHT_SHADE_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.HARVEST_NIGHT_SHADE_ACTION.isValidTarget(performer, performer, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		return performer;
	}
}