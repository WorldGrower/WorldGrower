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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.CommonerGenerator;

public class UTestCreateGraveAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		addSkeletonToInventory(performer, world);
		
		Actions.CREATE_GRAVE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(1, world.getWorldObjects().size());
		assertEquals("grave", world.getWorldObjects().get(0).getProperty(Constants.NAME));
	}

	private void addSkeletonToInventory(WorldObject performer, World world) {
		WorldObject skeletonWorldObject = createPerformer(8);
		skeletonWorldObject.setProperty(Constants.DEATH_REASON, "");
		int skeletonId = CommonerGenerator.generateSkeletalRemains(skeletonWorldObject, world);
		WorldObject skeleton = world.findWorldObject(Constants.ID, skeletonId);
		performer.getProperty(Constants.INVENTORY).add(skeleton);
		world.removeWorldObject(skeleton);
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		world.addWorldObject(target);
		assertEquals(false, Actions.CREATE_GRAVE_ACTION.isValidTarget(performer, performer, world));
		
		world.removeWorldObject(target);
		assertEquals(true, Actions.CREATE_GRAVE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		addSkeletonToInventory(performer, world);
		
		assertEquals(0, Actions.CREATE_GRAVE_ACTION.distance(performer, performer, new int[0], world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.HOUSES, new IdList());
		performer.setProperty(Constants.GOLD, 0);
		return performer;
	}
}