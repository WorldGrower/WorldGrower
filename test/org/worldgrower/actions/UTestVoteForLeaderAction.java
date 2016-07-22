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

public class UTestVoteForLeaderAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createVotingBox(world);
		
		Actions.VOTE_FOR_LEADER_ACTION.execute(performer, target, new int[] { 7 }, world);
		
		assertEquals(1, target.getProperty(Constants.VOTES).getValue(7));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createVotingBox(world);
		target.setProperty(Constants.ORGANIZATION_ID, 7);
		
		assertEquals(true, Actions.VOTE_FOR_LEADER_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.VOTE_FOR_LEADER_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createVotingBox(world);
		target.setProperty(Constants.ORGANIZATION_ID, 7);
		target.setProperty(Constants.TURN_COUNTER, 450);
		
		assertEquals(true, Actions.VOTE_FOR_LEADER_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createVotingBox(world);
		
		assertEquals(0, Actions.VOTE_FOR_LEADER_ACTION.distance(performer, target, Args.EMPTY, world));
	}

	private WorldObject createVotingBox(World world) {
		int id = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject target = world.findWorldObjectById(id);
		return target;
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