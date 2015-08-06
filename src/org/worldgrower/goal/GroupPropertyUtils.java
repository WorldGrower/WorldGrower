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
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.profession.Profession;

public class GroupPropertyUtils {

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
		List<WorldObject> organizations = world.findWorldObjects(w -> w.hasProperty(Constants.ORGANIZATION_LEADER_ID) && (w.getProperty(Constants.ORGANIZATION_LEADER_ID) != null) && w.getProperty(Constants.ORGANIZATION_LEADER_ID).intValue() == leaderId);
		return organizations;
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

	public static WorldObject getVillagersOrganization(WorldObject performer, World world) {
		return world.findWorldObject(Constants.ID, 1);
	}
}
