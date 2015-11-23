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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.DefaultGoalObstructedHandler;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.DefaultActionLegalHandler;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.actions.legal.WorshipDeityLegalHandler;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.IdToIntegerMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;

public class GroupPropertyUtils {

	//TODO: in longer run, merge profession and religion code. Code will be compacter with less duplication, but probably less readable.
	
	private static final int TAXES_PERIOD = 500;
	
	public static boolean isWorldObjectPotentialEnemy(WorldObject performer, WorldObject w) {
		IdList performerOrganizationIdList = performer.getProperty(Constants.GROUP);
		IdList otherOrganizationIdList = w.getProperty(Constants.GROUP);
		return w.hasIntelligence() && (w != performer) && (!otherOrganizationIdList.intersects(performerOrganizationIdList));
	}
	
	public static List<WorldObject> findWorldObjectsInSameGroup(WorldObject performer, World world) {
		IdList performerOrganizationIdList = performer.getProperty(Constants.GROUP);
		return world.findWorldObjects(w -> w.hasIntelligence() && (w.getProperty(Constants.GROUP).intersects(performerOrganizationIdList)));
	}
	
	public static void throwPerformerOutGroup(WorldObject performer, WorldObject w) {
		performer.getProperty(Constants.GROUP).removeAll(w.getProperty(Constants.GROUP).getIds());
	}
	
	public static boolean isOrganizationNameInUse(String organizationName, World world) {
		List<WorldObject> organizations = world.findWorldObjects(w -> w.hasProperty(Constants.ORGANIZATION_LEADER_ID) && w.getProperty(Constants.NAME).equals(organizationName));
		return organizations.size() > 0;
	}
	
	public static boolean isPerformerMemberOfProfessionOrganization(WorldObject performer, World world) {
		return (findProfessionOrganization(performer, world) != null);
	}
	
	public static boolean isPerformerMemberOfReligionOrganization(WorldObject performer, World world) {
		return (findReligionOrganization(performer, world) != null);
	}
	
	public static List<WorldObject> findProfessionOrganizationsInWorld(WorldObject performer, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		
		List<WorldObject> organisations = world.findWorldObjects(w -> w.hasProperty(Constants.ORGANIZATION_LEADER_ID) && w.getProperty(Constants.PROFESSION) == performerProfession);
		return organisations;
	}
	
	public static List<WorldObject> findReligionOrganizationsInWorld(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		
		List<WorldObject> organisations = world.findWorldObjects(w -> w.hasProperty(Constants.ORGANIZATION_LEADER_ID) && w.getProperty(Constants.DEITY) == performerDeity);
		return organisations;
	}
	
	public static List<WorldObject> findOrganizationMembers(WorldObject organization, World world) {
		List<WorldObject> members = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> worldObjectIsMemberOfOrganization(organization, w));
		return members;
	}

	private static boolean worldObjectIsMemberOfOrganization(WorldObject organization, WorldObject w) {
		IdList group = w.getProperty(Constants.GROUP);
		return ((group != null) && (group.contains(organization)));
	}
	
	public static WorldObject findProfessionOrganization(WorldObject performer, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		if (performerProfession != null) {
			IdList organizations = performer.getProperty(Constants.GROUP);
			for(int organizationId : organizations.getIds()) {
				WorldObject organisation = world.findWorldObject(Constants.ID, organizationId);
				if (organisation.getProperty(Constants.PROFESSION) == performerProfession) {
					return organisation;
				}
			}
		}
		return null;
	}
	
	public static WorldObject findReligionOrganization(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		IdList organizations = performer.getProperty(Constants.GROUP);
		for(int organizationId : organizations.getIds()) {
			WorldObject organisation = world.findWorldObject(Constants.ID, organizationId);
			if (organisation.getProperty(Constants.DEITY) == performerDeity) {
				return organisation;
			}
		}
		return null;
	}
	
	public static List<WorldObject> findOrganizationsUsingLeader(WorldObject leader, World world) {
		int leaderId = leader.getProperty(Constants.ID);
		List<WorldObject> organizations = new ArrayList<>();
		List<Integer> memberOrganizationIds = leader.getProperty(Constants.GROUP).getIds();
		for(int memberOrganizationId : memberOrganizationIds) {
			WorldObject memberOrganization = world.findWorldObject(Constants.ID, memberOrganizationId);
			if (worldObjectHasLeader(leaderId, memberOrganization)) {
				organizations.add(memberOrganization);
			}
		}
		return organizations;
	}

	private static boolean worldObjectHasLeader(int leaderId, WorldObject w) {
		Integer organizationLeaderId = w.getProperty(Constants.ORGANIZATION_LEADER_ID);
		return (organizationLeaderId != null) && organizationLeaderId.intValue() == leaderId;
	}
	
	public static List<WorldObject> getOrganizations(WorldObject performer, World world) {
		List<WorldObject> organizations = new ArrayList<>();
		for(int organizationId : performer.getProperty(Constants.GROUP).getIds()) {
			organizations.add(world.findWorldObject(Constants.ID, organizationId));
		}
		
		return organizations;
	}
	
	public static WorldObject createProfessionOrganization(Integer performerId, String organizationName, Profession profession, World world) {
		WorldObject organization = create(performerId, organizationName, world);
		organization.setProperty(Constants.PROFESSION, profession);
		
		return organization;
	}

	public static WorldObject createReligionOrganization(Integer performerId, String organizationName, Deity deity, Goal organizationGoal, World world) {
		WorldObject organization = create(performerId, organizationName, world);
		organization.setProperty(Constants.DEITY, deity);
		organization.setProperty(Constants.ORGANIZATION_GOAL, organizationGoal);
		
		return organization;
	}
	
	public static WorldObject create(Integer performerId, String organizationName, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, -100000);
		properties.put(Constants.Y, -100000);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, organizationName);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.BLACK_CROSS);
		properties.put(Constants.ORGANIZATION_LEADER_ID, performerId);
		
		WorldObject organization = new WorldObjectImpl(properties);
		world.addWorldObject(organization);
		
		return organization;
	}
	
	public static WorldObject createVillagersOrganization(World world) {
		WorldObject organization = create(null, "villagers", world);
		organization.setProperty(Constants.SHACK_TAX_RATE, 0);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 0);
		organization.setProperty(Constants.TAXES_PAID_TURN, new IdToIntegerMap());
		organization.setProperty(Constants.PAY_CHECK_PAID_TURN, new IdToIntegerMap());
		organization.setProperty(Constants.TURNS_IN_JAIL, new IdToIntegerMap());
		setLegalActions(organization);
		
		return organization;
	}

	private static void setLegalActions(WorldObject organization) {
		Map<LegalAction, Boolean> legalActions = new HashMap<>();
		
		List<ManagedOperation> defaultIllegalActions = new ArrayList<>();
		defaultIllegalActions.addAll(Actions.ALL_ACTIONS.stream().filter(a -> DefaultGoalObstructedHandler.performerAttacked(a)).collect(Collectors.toList()));
		defaultIllegalActions.addAll(DefaultGoalObstructedHandler.getNonAttackingIllegalActions());
		for(ManagedOperation action : defaultIllegalActions) {
			LegalAction legalAction = new LegalAction(action, new DefaultActionLegalHandler());
			legalActions.put(legalAction, Boolean.FALSE);
		}
		
		for(Deity deity : Deity.ALL_DEITIES) {
			LegalAction legalAction = new LegalAction(Actions.WORSHIP_DEITY_ACTION, new WorshipDeityLegalHandler(deity));
			legalActions.put(legalAction, Boolean.TRUE);
		}
		
		organization.setProperty(Constants.LEGAL_ACTIONS, new LegalActions(legalActions));
	}

	public static WorldObject getVillagersOrganization(World world) {
		return world.findWorldObject(Constants.ID, 1);
	}
	
	public static boolean performerIsLeaderOfVillagers(WorldObject performer, World world) {
		WorldObject villagersOrganization = getVillagersOrganization(world);
		return ((villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID) != null) && (villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue() == performer.getProperty(Constants.ID).intValue()));
	}
	
	public static boolean performerIsLeaderOfOrganization(WorldObject performer, WorldObject organization, World world) {
		return ((organization.getProperty(Constants.ORGANIZATION_LEADER_ID) != null) && (organization.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue() == performer.getProperty(Constants.ID).intValue()));
	}
	
	public static WorldObject getLeaderOfVillagers(World world) {
		WorldObject villagersOrganization = getVillagersOrganization(world);
		Integer leaderId = villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID);
		if (leaderId != null) {
			return world.findWorldObject(Constants.ID, leaderId.intValue());
		} else {
			return null;
		}
	}
	
	public static boolean canCollectTaxes(World world) {
		WorldObject villagersOrganization = getVillagersOrganization(world);
		return villagersOrganization.getProperty(Constants.SHACK_TAX_RATE) > 0 || villagersOrganization.getProperty(Constants.HOUSE_TAX_RATE) > 0;
	}
	
	public static int getAmountToCollect(WorldObject target, World world) {
		WorldObject villagersOrganization = getVillagersOrganization(world);
		IdMap taxesPaidTurn = villagersOrganization.getProperty(Constants.TAXES_PAID_TURN);
		int currentTurn = world.getCurrentTurn().getValue();
		
		final int numberOfTaxPeriods;
		int turnsNotPaid = currentTurn - taxesPaidTurn.getValue(target);
		if (turnsNotPaid < TAXES_PERIOD) {
			numberOfTaxPeriods = 0;
		} else {
			numberOfTaxPeriods = turnsNotPaid / TAXES_PERIOD;
		}
		
		int amountToCollect = getBaseAmountToPay(target, world) * numberOfTaxPeriods;
		return amountToCollect;
	}

	static int getBaseAmountToPay(WorldObject target, World world) {
		int amountToCollect = 0;
		List<Integer> houseIds = target.getProperty(Constants.HOUSES).getIds();
		if (houseIds.size() > 0) {
			for(int houseId : houseIds) {
				WorldObject house = world.findWorldObject(Constants.ID, houseId);
				if (BuildingGenerator.isShack(house)) {
					amountToCollect += GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.SHACK_TAX_RATE);
				} else if (BuildingGenerator.isHouse(house)) {
					amountToCollect += GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.HOUSE_TAX_RATE);
				}
			}
		}
		return amountToCollect;
	}
	
	
	public static int getPayCheckAmount(WorldObject target, World world) {
		WorldObject villagersOrganization = getVillagersOrganization(world);
		IdMap payCheckPaidTurn = villagersOrganization.getProperty(Constants.PAY_CHECK_PAID_TURN);
		int currentTurn = world.getCurrentTurn().getValue();
		
		final int numberOfPayCheckPeriods;
		int turnsNotPaid = currentTurn - payCheckPaidTurn.getValue(target);
		if (turnsNotPaid < TAXES_PERIOD) {
			numberOfPayCheckPeriods = 0;
		} else {
			numberOfPayCheckPeriods = turnsNotPaid / TAXES_PERIOD;
		}
		
		int amountToCollect = 5 * numberOfPayCheckPeriods;
		return amountToCollect;
	}
	
	public static boolean isMinionOrganization(WorldObject organization) {
		return organization.hasProperty(Constants.MINION_ORGANIZATION) && organization.getProperty(Constants.MINION_ORGANIZATION);
	}
	
	public static boolean canJoinOrganization(WorldObject personJoining, WorldObject organization) {
		Deity organizationDeity = organization.getProperty(Constants.DEITY);
		if (organizationDeity != null) {
			Deity personJoiningDeity = personJoining.getProperty(Constants.DEITY);
			if (organizationDeity == personJoiningDeity) {
				return true;
			} else {
				return false;
			}
		}
		return canChangeLeaderOfOrganization(organization);
	}
	
	public static boolean canChangeLeaderOfOrganization(WorldObject organization) {
		return !isMinionOrganization(organization);
	}
	
	public static int getRandomOrganizationIndex(WorldObject performer, List<String> organizationNames) {
		if (organizationNames.size() == 1) {
			return 0;
		} else if (organizationNames.size() == 0) {
			throw new IllegalStateException("No organization names found for " + performer.toString());
		} else {
			/*Random r = new Random();
			int low = 0;
			int high = organizationNames.size() - 1;
			int organizationIndex = r.nextInt(high-low) + low;
			return organizationIndex;*/
			// for now, return 0
			return 0;
		}
	}
	
	public static Integer getMostLikedLeaderId(WorldObject performer, List<WorldObject> organizations) {
		Collections.sort(organizations, new OrganizationComparator(performer));
		Collections.reverse(organizations);
		Integer leaderId = organizations.get(0).getProperty(Constants.ORGANIZATION_LEADER_ID);
		return leaderId;
	}

	private static class OrganizationComparator implements  Comparator<WorldObject> {

		private final WorldObject performer;
		
		public OrganizationComparator(WorldObject performer) {
			this.performer = performer;
		}

		@Override
		public int compare(WorldObject organization1, WorldObject organization2) {
			Integer leaderId1 = organization1.getProperty(Constants.ORGANIZATION_LEADER_ID);
			Integer leaderId2 = organization2.getProperty(Constants.ORGANIZATION_LEADER_ID);
			
			int relationshipValue1 = leaderId1 != null ? performer.getProperty(Constants.RELATIONSHIPS).getValue(leaderId1) : 0;
			int relationshipValue2 = leaderId2 != null ? performer.getProperty(Constants.RELATIONSHIPS).getValue(leaderId2) : 0;
			
			return Integer.compare(relationshipValue1, relationshipValue2);
		}
		
	}
	
	public static WorldObject createMinionOrganization(WorldObject performer, World world) {
		final WorldObject minionOrganization;
		List<WorldObject> minionOrganizations = world.findWorldObjects(w -> GroupPropertyUtils.isMinionOrganization(w) && GroupPropertyUtils.performerIsLeaderOfOrganization(performer, w, world));
		if (minionOrganizations.size() > 0) {
			minionOrganization = minionOrganizations.get(0);
		} else {
			minionOrganization = GroupPropertyUtils.create(performer.getProperty(Constants.ID), "minions of " + performer.getProperty(Constants.NAME), world);
			minionOrganization.setProperty(Constants.MINION_ORGANIZATION, Boolean.TRUE);
			performer.getProperty(Constants.GROUP).add(minionOrganization);
		}
		return minionOrganization;
	}

	public static List<WorldObject> getMatchingOrganizationsUsingLeader(WorldObject performer, WorldObject target, World world) {
		List<WorldObject> performerOrganizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		List<WorldObject> targetOrganizations = GroupPropertyUtils.findOrganizationsUsingLeader(target, world);
		List<WorldObject> organizations = new ArrayList<>();
		
		for(WorldObject performerOrganization : performerOrganizations) {
			for(WorldObject targetOrganization : targetOrganizations) {
				if (organizationsMatch(performerOrganization, targetOrganization)) {
					organizations.add(performerOrganization);
				}
			}
		}
		return organizations;
	}
	
	static boolean organizationsMatch(WorldObject performerOrganization, WorldObject targetOrganization) {
		if (performerOrganization.getProperty(Constants.PROFESSION) != null) {
			return performerOrganization.getProperty(Constants.PROFESSION) == targetOrganization.getProperty(Constants.PROFESSION);
		}
		if (performerOrganization.getProperty(Constants.DEITY) != null) {
			return performerOrganization.getProperty(Constants.DEITY) == targetOrganization.getProperty(Constants.DEITY);
		}
		return false;
	}

	public static WorldObject findMatchingOrganization(WorldObject target, WorldObject performerOrganization, World world) {
		IdList organizations = target.getProperty(Constants.GROUP);
		for(int organizationId : organizations.getIds()) {
			WorldObject targetOrganization = world.findWorldObject(Constants.ID, organizationId);
			if (organizationsMatch(performerOrganization, targetOrganization) && !performerOrganization.equals(targetOrganization)) {
				return targetOrganization;
			}
		}
		return null;
	}
}
