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
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestOrganizationRebelsOnTurn {

	@Test
	public void testOnTurnNoLeaderNoRebels() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(null, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID));
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());
		
		new OrganizationRebelsOnTurn().onTurn(world);
		
		assertEquals(null, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID));
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());		
	}
	
	@Test
	public void testOnTurnLeaderNoRebels() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		
		assertEquals(2, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue());
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());
		
		new OrganizationRebelsOnTurn().onTurn(world);
		
		assertEquals(2, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue());
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());		
	}
	
	@Test
	public void testOnTurnNoLeaderRebels() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject member1 = createPerformer(2);
		world.addWorldObject(member1);
		WorldObject member2 = createPerformer(3);
		world.addWorldObject(member2);
		WorldObject member3 = createPerformer(4);
		world.addWorldObject(member3);
		WorldObject member4 = createPerformer(5);
		world.addWorldObject(member4);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, null);
		villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).add(2).add(3);
		
		assertEquals(null, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID));
		assertEquals(2, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());
		
		new OrganizationRebelsOnTurn().onTurn(world);
		
		assertEquals(null, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID));
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());		
	}
	
	@Test
	public void testOnTurnLeaderRebelMajority() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject member1 = createPerformer(2);
		world.addWorldObject(member1);
		WorldObject member2 = createPerformer(3);
		world.addWorldObject(member2);
		WorldObject member3 = createPerformer(4);
		world.addWorldObject(member3);
		WorldObject member4 = createPerformer(5);
		world.addWorldObject(member4);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, 1);
		villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).add(2).add(3).add(4);
		
		assertEquals(1, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue());
		assertEquals(3, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());
		
		new OrganizationRebelsOnTurn().onTurn(world);
		
		assertEquals(null, villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID));
		assertEquals(0, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).size());		
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.ID, 1);
		world.addWorldObject(villagersOrganization);
		return villagersOrganization;
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.CONDITIONS, new Conditions());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.HIT_POINTS, 20);
		return performer;
	}
}