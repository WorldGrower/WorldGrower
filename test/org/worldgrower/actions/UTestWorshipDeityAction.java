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
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;

public class UTestWorshipDeityAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.WORSHIP_COUNTER, 0);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		
		Actions.WORSHIP_DEITY_ACTION.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(1, performer.getProperty(Constants.WORSHIP_COUNTER).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		int id = BuildingGenerator.generateShrine(0, 0, world, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(true, Actions.WORSHIP_DEITY_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.WORSHIP_DEITY_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		int id = BuildingGenerator.generateShrine(0, 0, world, performer);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(0, Actions.WORSHIP_DEITY_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}