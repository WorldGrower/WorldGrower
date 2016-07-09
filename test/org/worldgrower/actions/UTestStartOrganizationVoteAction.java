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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.CommonerImageIds;

public class UTestStartOrganizationVoteAction {

	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		int id = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject performer = world.findWorldObjectById(id);
		WorldObject target = createPerformer(3);
		
		Actions.START_ORGANIZATION_VOTE_ACTION.execute(performer, target, new int[] { 0 }, world);
		assertEquals(3, world.getWorldObjects().size());
		assertEquals("voting box", world.getWorldObjects().get(2).getProperty(Constants.NAME));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		int id = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject performer = world.findWorldObjectById(id);
		WorldObject target = createPerformer(3);
		
		assertEquals(true, Actions.START_ORGANIZATION_VOTE_ACTION.isValidTarget(performer, performer, world));
		assertEquals(false, Actions.START_ORGANIZATION_VOTE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		int id = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject performer = world.findWorldObjectById(id);
		
		assertEquals(true, Actions.START_ORGANIZATION_VOTE_ACTION.isActionPossible(performer, performer, new int[] { 0 }, world));
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