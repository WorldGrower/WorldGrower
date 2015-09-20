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
import static org.worldgrower.TestUtils.createIntelligentWorldObject;
import static org.worldgrower.goal.GroupPropertyUtils.createProfessionOrganization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.profession.Professions;

public class UTestGroupPropertyUtils {

	@Test
	public void testIsWorldObjectPotentialEnemy() {
		WorldObject performer = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(3));
		WorldObject w1 = createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject w2 = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(4));
		WorldObject w3 = createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(3).add(4));
		
		assertEquals(true, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w1));
		assertEquals(true, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w2));
		assertEquals(false, GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w3));
	}
	
	@Test
	public void testIsOrganizationNameInUse() {
		World world = new WorldImpl(0, 0, null, null);
		createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(true, GroupPropertyUtils.isOrganizationNameInUse("TestOrg", world));
		assertEquals(false, GroupPropertyUtils.isOrganizationNameInUse("TestOrg2", world));
	}
	
	@Test
	public void testFindOrganizationsUsingLeader() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject leader = createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		world.addWorldObject(leader);
		
		IdList leaderGroup = leader.getProperty(Constants.GROUP);
		leaderGroup.add(createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world));
		leaderGroup.add(createProfessionOrganization(2, "TestOrg2", Professions.FARMER_PROFESSION, world));
		
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(leader, world);
		assertEquals(1, organizations.size());
		assertEquals("TestOrg", organizations.get(0).getProperty(Constants.NAME));
	}
	
	@Test
	public void testFindOrganizationMembers() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject member = createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject nonMember = createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		world.addWorldObject(member);
		world.addWorldObject(nonMember);
				
		WorldObject organization = createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		member.getProperty(Constants.GROUP).add(organization);
	
		assertEquals(Arrays.asList(member), GroupPropertyUtils.findOrganizationMembers(organization, world));
	
	}
	
	@Test
	public void testGetMostLikedLeaderId() {
		IdRelationshipMap idRelationshipMap = new IdRelationshipMap();
		idRelationshipMap.incrementValue(3, 100);
		idRelationshipMap.incrementValue(5, 200);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, idRelationshipMap);
		List<WorldObject> organizations = new ArrayList<>();

		organizations.add(TestUtils.createIntelligentWorldObject(2, Constants.ORGANIZATION_LEADER_ID, 3));
		assertEquals(3, GroupPropertyUtils.getMostLikedLeaderId(performer, organizations).intValue());
		
		organizations.add(TestUtils.createIntelligentWorldObject(4, Constants.ORGANIZATION_LEADER_ID, 5));
		assertEquals(5, GroupPropertyUtils.getMostLikedLeaderId(performer, organizations).intValue());
	}
	
	@Test
	public void testGetMostLikedLeaderIdForNoLeader() {
		IdRelationshipMap idRelationshipMap = new IdRelationshipMap();
		idRelationshipMap.incrementValue(3, -100);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, idRelationshipMap);
		List<WorldObject> organizations = new ArrayList<>();

		organizations.add(TestUtils.createIntelligentWorldObject(2, Constants.ORGANIZATION_LEADER_ID, 3));
		organizations.add(TestUtils.createIntelligentWorldObject(4, Constants.ORGANIZATION_LEADER_ID, null));
		assertEquals(null, GroupPropertyUtils.getMostLikedLeaderId(performer, organizations));
	}
	
	@Test
	public void testPerformerIsLeaderOfVillagers() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		assertEquals(false, GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world));
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		assertEquals(true, GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world));
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	@Test
	public void testGetLeaderOfVillagers() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		assertEquals(null, GroupPropertyUtils.getLeaderOfVillagers(world));
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		assertEquals(performer, GroupPropertyUtils.getLeaderOfVillagers(world));
	}
	
	@Test
	public void testCanCollectTaxes() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		assertEquals(false, GroupPropertyUtils.canCollectTaxes(world));
		
		organization.setProperty(Constants.SHACK_TAX_RATE, 1);
		assertEquals(true, GroupPropertyUtils.canCollectTaxes(world));
	}
	
	@Test
	public void testFindProfessionOrganizationsInWorldNotMember() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testFindProfessionOrganizationsInWorldMember() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(2, "TestOrg", Professions.FARMER_PROFESSION, world);
		assertEquals(Arrays.asList(organization), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testGetBaseAmountToPayNoHouses() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.HOUSES, new IdList());
		world.addWorldObject(target);
		
		assertEquals(0, GroupPropertyUtils.getBaseAmountToPay(target, world));
	}
	
	@Test
	public void testGetBaseAmountToPayShackAndHouse() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Constants.HOUSES, new IdList().add(2).add(3));
		world.addWorldObject(target);
		
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.SHACK_TAX_RATE, 1);
		villagersOrganization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		
		int shackId = BuildingGenerator.generateShack(0, 0, world, 0f);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, 0f);
		
		assertEquals(3, GroupPropertyUtils.getBaseAmountToPay(target, world));
	}
	
	@Test
	public void testCanJoinOrganization() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject personJoining = TestUtils.createIntelligentWorldObject(0, Constants.DEITY, null);
		world.addWorldObject(personJoining);
		
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(8, "TestOrg", Deity.HADES, null, world);
		
		assertEquals(false, GroupPropertyUtils.canJoinOrganization(personJoining, organization));
		
		personJoining.setProperty(Constants.DEITY, Deity.HADES);
		assertEquals(true, GroupPropertyUtils.canJoinOrganization(personJoining, organization));
	}
	
	@Test
	public void testFindProfessionOrganization() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(7, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(null, GroupPropertyUtils.findProfessionOrganization(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).add(organization);
		assertEquals(organization, GroupPropertyUtils.findProfessionOrganization(performer, world));
	}
}
