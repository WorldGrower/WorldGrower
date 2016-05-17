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
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.GhoulUtils;
import org.worldgrower.generator.CommonerGenerator;

public class UTestEatRemainsAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.GOLD, 100);
		GhoulUtils.ghoulifyPerson(performer, world);
		
		WorldObject victim = createPerformer(3);
		victim.setProperty(Constants.GOLD, 5);
		victim.setProperty(Constants.ORGANIZATION_GOLD, 15);
		int id = CommonerGenerator.generateSkeletalRemains(victim, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals("skeletal remains of null", target.getProperty(Constants.NAME));
		
		Actions.EAT_REMAINS_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(0, world.getWorldObjects().size());
		assertEquals(120, performer.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		GhoulUtils.ghoulifyPerson(performer, world);
		
		int id = CommonerGenerator.generateSkeletalRemains(createPerformer(3), world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(true, Actions.EAT_REMAINS_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.EAT_REMAINS_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		int id = CommonerGenerator.generateSkeletalRemains(createPerformer(3), world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(0, Actions.EAT_REMAINS_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.GROUP, new IdList());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 1000);
		performer.setProperty(Constants.ORGANIZATION_GOLD, 0);
		performer.setProperty(Constants.DEATH_REASON, "");
		return performer;
	}
}