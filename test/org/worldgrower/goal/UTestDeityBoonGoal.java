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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestDeityBoonGoal {

	private DeityBoonGoal goal = new DeityBoonGoal(Deity.ARES);
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = createVillagersOrganization(world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateWorship() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		BuildingGenerator.generateShrine(0, 0, world, performer);
		
		assertEquals(Actions.WORSHIP_DEITY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalWorshipDifferentDeity() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		BuildingGenerator.generateShrine(0, 0, world, performer);
		
		performer.setProperty(Constants.DEITY, Deity.DEMETER);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		Conditions.add(performer, Condition.ARES_BOON_CONDITION, 8, world);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		Conditions.remove(performer, Condition.ARES_BOON_CONDITION, world);
		performer.setProperty(Constants.DEITY, Deity.DEMETER);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		world.generateUniqueId();world.generateUniqueId();
		return organization;
	}
}