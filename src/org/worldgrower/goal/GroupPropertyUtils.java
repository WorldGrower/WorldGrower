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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.IdToIntegerMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;

public class GroupPropertyUtils {

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
	
	public static List<WorldObject> findProfessionOrganizationsInWorld(WorldObject performer, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		
		List<WorldObject> organisations = world.findWorldObjects(w -> w.hasProperty(Constants.ORGANIZATION_LEADER_ID) && w.getProperty(Constants.PROFESSION) == performerProfession);
		return organisations;
	}
	
	public static List<WorldObject> findOrganizationMembers(WorldObject organization, World world) {
		List<WorldObject> members = world.findWorldObjects(w -> w.hasProperty(Constants.GROUP) && w.getProperty(Constants.GROUP).contains(organization));
		return members;
	}
	
	public static WorldObject findProfessionOrganization(WorldObject performer, World world) {
		Profession performerProfession = performer.getProperty(Constants.PROFESSION);
		IdList organizations = performer.getProperty(Constants.GROUP);
		for(int organizationId : organizations.getIds()) {
			WorldObject organisation = world.findWorldObject(Constants.ID, organizationId);
			if (organisation.getProperty(Constants.PROFESSION) == performerProfession) {
				return organisation;
			}
		}
		return null;
	}
	
	public static List<WorldObject> findOrganizationsUsingLeader(WorldObject leader, World world) {
		int leaderId = leader.getProperty(Constants.ID);
		List<WorldObject> organizations = world.findWorldObjects(w -> worldObjectHasLeader(leaderId, w));
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
	
	public static WorldObject create(Integer performerId, String organizationName, Profession profession, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, -100000);
		properties.put(Constants.Y, -100000);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, organizationName);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.BLACK_CROSS);
		properties.put(Constants.ORGANIZATION_LEADER_ID, performerId);
		properties.put(Constants.PROFESSION, profession);
		
		WorldObject organization = new WorldObjectImpl(properties);
		world.addWorldObject(organization);
		
		return organization;
	}
	
	public static WorldObject createVillagersOrganization(World world) {
		WorldObject organization = create(null, "villagers", null, world);
		organization.setProperty(Constants.SHACK_TAX_RATE, 0);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 0);
		organization.setProperty(Constants.TAXES_PAID_TURN, new IdToIntegerMap());
		organization.setProperty(Constants.PAY_CHECK_PAID_TURN, new IdToIntegerMap());
		
		return organization;
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

	private static int getBaseAmountToPay(WorldObject target, World world) {
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
	
	public static boolean isMinionOrganzation(WorldObject organization) {
		return organization.hasProperty(Constants.MINION_ORGANIZATION) && organization.getProperty(Constants.MINION_ORGANIZATION);
	}
	
	public static boolean canJoinOrChangeLeaderOfOrganization(WorldObject organization) {
		return !isMinionOrganzation(organization);
	}
}
