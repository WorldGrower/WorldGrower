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
import org.worldgrower.generator.BuildingGenerator;

public class UTestBecomeLeaderCandidateAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(false, target.getProperty(Constants.CANDIDATES).contains(performer));
		Actions.BECOME_LEADER_CANDIDATE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(true, target.getProperty(Constants.CANDIDATES).contains(performer));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		target.setProperty(Constants.ORGANIZATION_ID, 7);
		
		assertEquals(true, Actions.BECOME_LEADER_CANDIDATE_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.BECOME_LEADER_CANDIDATE_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		target.setProperty(Constants.ORGANIZATION_ID, 7);
		target.setProperty(Constants.TURN_COUNTER, 150);
		
		assertEquals(0, Actions.BECOME_LEADER_CANDIDATE_ACTION.distance(performer, target, new int[0], world));
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