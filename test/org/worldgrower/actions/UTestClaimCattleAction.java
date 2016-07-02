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
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestClaimCattleAction {

	private ClaimCattleAction action = Actions.CLAIM_CATTLE_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		WorldObject cow = generateCow(world);
		assertEquals(null, cow.getProperty(Constants.CATTLE_OWNER_ID));
		
		action.execute(performer, cow, Args.EMPTY, world);
		
		assertEquals(2, cow.getProperty(Constants.CATTLE_OWNER_ID).intValue());
	}

	private WorldObject generateCow(World world) {
		int cowId = new CreatureGenerator(GroupPropertyUtils.create(null, "TestOrg", world)).generateCow(0, 0, world);
		WorldObject cow = world.findWorldObject(Constants.ID, cowId);
		return cow;
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject cow = generateCow(world);
		
		assertEquals(true, action.isValidTarget(performer, cow, world));
		assertEquals(false, action.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject cow = generateCow(world);
		
		assertEquals(true, action.isActionPossible(performer, cow, Args.EMPTY, world));
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