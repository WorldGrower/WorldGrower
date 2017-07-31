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
import static org.junit.Assert.fail;
import static org.worldgrower.TestUtils.createIntelligentWorldObject;
import static org.worldgrower.goal.GroupPropertyUtils.createProfessionOrganization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
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
		World world = new WorldImpl(1, 1, null, null);
		createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(true, GroupPropertyUtils.isOrganizationNameInUse("TestOrg", world));
		assertEquals(false, GroupPropertyUtils.isOrganizationNameInUse("TestOrg2", world));
	}
	
	@Test
	public void testFindOrganizationsUsingLeader() {
		World world = new WorldImpl(1, 1, null, null);
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
		World world = new WorldImpl(1, 1, null, null);
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
		World world = new WorldImpl(1, 1, null, null);
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
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		assertEquals(null, GroupPropertyUtils.getLeaderOfVillagers(world));
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, 2);
		assertEquals(performer, GroupPropertyUtils.getLeaderOfVillagers(world));
	}
	
	@Test
	public void testCanCollectTaxes() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		
		assertEquals(false, GroupPropertyUtils.canStartCollectingTaxes(world));
		
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		assertEquals(true, GroupPropertyUtils.canStartCollectingTaxes(world));
	}
	
	@Test
	public void testFindProfessionOrganizationsInWorldNotMember() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testFindReligionOrganizationsInWorldNotMember() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "Test");
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findReligionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testFindProfessionOrganizationsInWorldMember() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.PROFESSION, Professions.FARMER_PROFESSION);
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(2, "TestOrg", Professions.FARMER_PROFESSION, world);
		assertEquals(Arrays.asList(organization), GroupPropertyUtils.findProfessionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testFindReligionOrganizationsInWorldMember() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		world.addWorldObject(performer);
		
		assertEquals(new ArrayList<>(), GroupPropertyUtils.findReligionOrganizationsInWorld(performer, world));
		
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(2, "TestOrg", Deity.HADES, Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL, world);
		assertEquals(Arrays.asList(organization), GroupPropertyUtils.findReligionOrganizationsInWorld(performer, world));
	}
	
	@Test
	public void testGetBaseAmountToPayNoHouses() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.BUILDINGS, new BuildingList());
		world.addWorldObject(target);
		
		assertEquals(0, GroupPropertyUtils.getBaseAmountToPay(target, world));
	}
	
	@Test
	public void testGetBaseAmountToPayShackAndHouse() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Constants.BUILDINGS, new BuildingList().add(2, BuildingType.HOUSE).add(3, BuildingType.HOUSE));
		world.addWorldObject(target);
		
		createVillagersOrganizationWithTaxRates(world);
		BuildingGenerator.generateShack(0, 0, world, target);
		BuildingGenerator.generateHouse(0, 0, world, target);
		
		assertEquals(3, GroupPropertyUtils.getBaseAmountToPay(target, world));
	}

	private WorldObject createVillagersOrganizationWithTaxRates(World world) {
		WorldObject villagersOrganization = createVillagersOrganization(world);
		villagersOrganization.setProperty(Constants.SHACK_TAX_RATE, 1);
		villagersOrganization.setProperty(Constants.HOUSE_TAX_RATE, 2);
		
		return villagersOrganization;
	}
	
	@Test
	public void testGetAmountToCollect() {
		World world = new WorldImpl(10, 10, null, new DoNothingWorldOnTurn());
		WorldObject target = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Constants.BUILDINGS, new BuildingList().add(2, BuildingType.HOUSE).add(3, BuildingType.HOUSE));
		world.addWorldObject(target);
		
		createVillagersOrganizationWithTaxRates(world);
		BuildingGenerator.generateShack(0, 0, world, target);
		BuildingGenerator.generateHouse(0, 0, world, target);
		
		assertEquals(0, GroupPropertyUtils.getAmountToCollect(target, world));
		
		for(int i=0; i<1000; i++) {
			world.nextTurn();
		}
		
		assertEquals(6, GroupPropertyUtils.getAmountToCollect(target, world));
	}
	
	@Test
	public void testGetPayCheckAmount() {
		World world = new WorldImpl(1, 1, null, new DoNothingWorldOnTurn());
		WorldObject target = TestUtils.createIntelligentWorldObject(world.generateUniqueId(), Constants.BUILDINGS, new BuildingList().add(2, BuildingType.HOUSE).add(3, BuildingType.HOUSE));
		target.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		world.addWorldObject(target);
		
		WorldObject villagersOrganization = createVillagersOrganizationWithTaxRates(world);
		villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN).incrementValue(target, 0);
		
		assertEquals(0, GroupPropertyUtils.getPayCheckAmount(target, world));
		
		for(int i=0; i<1000; i++) {
			world.nextTurn();
		}
		
		
		assertEquals(20, GroupPropertyUtils.getPayCheckAmount(target, world));
	}
	
	@Test
	public void testCanJoinOrganization() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject personJoining = TestUtils.createIntelligentWorldObject(0, Constants.DEITY, null);
		world.addWorldObject(personJoining);
		
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(8, "TestOrg", Deity.HADES, null, world);
		
		assertEquals(false, GroupPropertyUtils.canJoinOrganization(personJoining, organization));
		
		personJoining.setProperty(Constants.DEITY, Deity.HADES);
		assertEquals(true, GroupPropertyUtils.canJoinOrganization(personJoining, organization));
	}
	
	@Test
	public void testFindProfessionOrganization() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(7, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(null, GroupPropertyUtils.findProfessionOrganization(performer, world));
		
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).add(organization);
		assertEquals(organization, GroupPropertyUtils.findProfessionOrganization(performer, world));
	}
	
	@Test
	public void testFindReligionOrganization() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(7, "TestOrg", Deity.HADES, Goals.DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL, world);
		
		assertEquals(null, GroupPropertyUtils.findReligionOrganization(performer, world));
		
		performer.setProperty(Constants.DEITY, Deity.HADES);
		performer.getProperty(Constants.GROUP).add(organization);
		assertEquals(organization, GroupPropertyUtils.findReligionOrganization(performer, world));
	}
	
	@Test
	public void testGetOrganizations() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.GROUP, new IdList().add(7));
		world.addWorldObject(performer);
		
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(7, "TestOrg", Professions.FARMER_PROFESSION, world);
		organization.setProperty(Constants.ID, 7);
		world.addWorldObject(organization);
		
		List<WorldObject> organizations = GroupPropertyUtils.getOrganizations(performer, world);
		assertEquals(1, organizations.size());
		assertEquals(organization, organizations.get(0));
	}
	
	@Test
	public void testCreateMinionOrganization() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		
		WorldObject organization = GroupPropertyUtils.createMinionOrganization(performer, world);
		assertEquals(true, organization.getProperty(Constants.MINION_ORGANIZATION));
		assertEquals(true, performer.getProperty(Constants.GROUP).contains(organization));
		assertEquals(0, organization.getProperty(Constants.ID).intValue());
		
		organization = GroupPropertyUtils.createMinionOrganization(performer, world);
		assertEquals(0, organization.getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindMatchingOrganizationNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(performerOrganization);
		world.addWorldObject(performer);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.GROUP, new IdList());
		assertEquals(null, GroupPropertyUtils.findMatchingOrganization(target, performerOrganization, world));
	}
	
	@Test
	public void testFindMatchingOrganizationInSelf() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject targetOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrgTarget", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(performerOrganization).add(targetOrganization);
		world.addWorldObject(performer);
		
		assertEquals(targetOrganization, GroupPropertyUtils.findMatchingOrganization(performer, performerOrganization, world));
	}
	
	@Test
	public void testFindMatchingOrganizationInTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(performerOrganization);
		world.addWorldObject(performer);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.GROUP, new IdList());
		WorldObject targetOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		target.getProperty(Constants.GROUP).add(targetOrganization);
		assertEquals(targetOrganization, GroupPropertyUtils.findMatchingOrganization(target, performerOrganization, world));
	}
	
	@Test
	public void testOrganizationsMatchSameProfession() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject targetOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		assertEquals(true, GroupPropertyUtils.organizationsMatch(performerOrganization, targetOrganization));
	}
	
	@Test
	public void testOrganizationsMatchDifferentProfession() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject targetOrganization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FISHER_PROFESSION, world);
		assertEquals(false, GroupPropertyUtils.organizationsMatch(performerOrganization, targetOrganization));
	}
	
	@Test
	public void testOrganizationsMatchSameDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performerOrganization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.IDLE_GOAL, world);
		WorldObject targetOrganization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.IDLE_GOAL, world);
		assertEquals(true, GroupPropertyUtils.organizationsMatch(performerOrganization, targetOrganization));
	}
	
	@Test
	public void testOrganizationsMatchDifferentDeity() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performerOrganization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.IDLE_GOAL, world);
		WorldObject targetOrganization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.HERMES, Goals.IDLE_GOAL, world);
		assertEquals(false, GroupPropertyUtils.organizationsMatch(performerOrganization, targetOrganization));
	}
	
	@Test
	public void testOrganizationsMatchFalse() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performerOrganization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject targetOrganization = GroupPropertyUtils.create(null, "TestOrg", world);
		assertEquals(false, GroupPropertyUtils.organizationsMatch(performerOrganization, targetOrganization));
	}
	
	@Test
	public void testGetMatchingOrganizationsUsingLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(performer.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(performerOrganization);
		world.addWorldObject(performer);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.GROUP, new IdList());
		WorldObject targetOrganization = GroupPropertyUtils.createProfessionOrganization(target.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		target.getProperty(Constants.GROUP).add(targetOrganization);
		assertEquals(Arrays.asList(performerOrganization), GroupPropertyUtils.getMatchingOrganizationsUsingLeader(performer, target, world));
	}
	
	@Test
	public void testThrowPerformerOutGroup() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.GROUP, new IdList());
		createVillagersOrganization(world);
		WorldObject performerOrganization = GroupPropertyUtils.createProfessionOrganization(performer.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		performer.getProperty(Constants.GROUP).add(performerOrganization);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.GROUP, new IdList());
		target.getProperty(Constants.GROUP).add(performerOrganization);
		
		GroupPropertyUtils.throwPerformerOutGroup(performer, target, world);
		assertEquals(0, performer.getProperty(Constants.GROUP).size());
	}
	
	@Test
	public void testGetRandomOrganizationIndex() {
		WorldObject performer = TestUtils.createWorldObject(2, "performer");
		assertEquals(0, GroupPropertyUtils.getRandomOrganizationIndex(performer, Arrays.asList("Org1"), "farmer"));
		
		performer.setProperty(Constants.NAME, "Y");
		assertEquals(1, GroupPropertyUtils.getRandomOrganizationIndex(performer, Arrays.asList("Org1", "Org2"), "farmer"));
		
		performer.setProperty(Constants.NAME, "");
		assertEquals(0, GroupPropertyUtils.getRandomOrganizationIndex(performer, Arrays.asList("Org1", "Org2"), "farmer"));
	}
	
	@Test
	public void testGetRandomOrganizationIndexError() {
		try {
			WorldObject performer = TestUtils.createWorldObject(2, "performer");
			GroupPropertyUtils.getRandomOrganizationIndex(performer, new ArrayList<>(), "farmer");
			fail("method should fail");
		} catch(IllegalStateException ex) {
			assertEquals(true, ex.getMessage().startsWith("No organization names found for"));
		}
	}
	
	@Test
	public void testHasAuthorityToAddMembersLeader() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject decisionMaker = TestUtils.createWorldObject(2, "decisionMaker");
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(decisionMaker.getProperty(Constants.ID), "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(true, GroupPropertyUtils.hasAuthorityToAddMembers(decisionMaker, organization, world));
	}
	
	@Test
	public void testHasAuthorityToAddMembersVillagerSheriff() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject decisionMaker = TestUtils.createWorldObject(2, "decisionMaker");
		decisionMaker.setProperty(Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		WorldObject organization = createVillagersOrganization(world);
		
		assertEquals(true, GroupPropertyUtils.hasAuthorityToAddMembers(decisionMaker, organization, world));
	}
	
	@Test
	public void testHasAuthorityToAddMembersRegularMember() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject decisionMaker = TestUtils.createWorldObject(2, "decisionMaker");
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(7, "TestOrg", Professions.FARMER_PROFESSION, world);
		createVillagersOrganization(world);
		
		assertEquals(false, GroupPropertyUtils.hasAuthorityToAddMembers(decisionMaker, organization, world));
	}
	
	@Test
	public void testGetAllOrganizations() {
		World world = new WorldImpl(0, 0, null, null);
		createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		createProfessionOrganization(2, "TestOrg2", Professions.FARMER_PROFESSION, world);
		
		assertEquals(2, GroupPropertyUtils.getAllOrganizations(world).size());
		assertEquals("TestOrg", GroupPropertyUtils.getAllOrganizations(world).get(0).getProperty(Constants.NAME));
		assertEquals("TestOrg2", GroupPropertyUtils.getAllOrganizations(world).get(1).getProperty(Constants.NAME));
	}
}
