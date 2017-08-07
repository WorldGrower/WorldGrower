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
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class UTestStopSellingGoal {

	private StopSellingGoal goal = Goals.STOP_SELLING_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalAskStopSelling() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(7);
		WorldObject target = createPerformer(8);
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		createVillagersOrganization(world);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(performer.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(organization).add(1);
		target.setProperty(Constants.ITEMS_SOLD, new ItemCountMap());
		target.getProperty(Constants.ITEMS_SOLD).add(Item.BERRIES, 1);
		target.getProperty(Constants.GROUP).add(1);
		world.addWorldObject(target);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(5, goal.calculateGoal(performer, world).getArgs().length);
		assertEquals(0/*Item.BERRIES.ordinal()*/, goal.calculateGoal(performer, world).getArgs()[3]);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		return organization;
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.GROUP, new IdList());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		return performer;
	}
}