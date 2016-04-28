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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;

public class UTestReleaseCapturedCriminalsGoal {

	private ReleaseCapturedCriminalsGoal goal = Goals.RELEASE_CAPTURED_CRIMINALS_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalReleaseTarget() {
		World world = new WorldImpl(20, 20, null, new DoNothingWorldOnTurn());
		WorldObject performer = createPerformer(7);
		WorldObject target = createPerformer(8);
		world.addWorldObject(target);
		
		BuildingGenerator.generateJail(0, 0, world, 1f);
		
		target.setProperty(Constants.X, 1);
		target.setProperty(Constants.Y, 1);
		
		WorldObject organization = createVillagersOrganization(world);
		organization.getProperty(Constants.TURNS_IN_JAIL).incrementValue(target, 0);
		
		for(int i=0; i<1000; i++) { world.nextTurn(); }
		
		assertEquals(Actions.UNLOCK_JAIL_DOOR_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.NAME, "name");
		return performer;
	}
}