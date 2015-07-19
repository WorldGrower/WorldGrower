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
		performer.getProperty(Constants.GROUP).remove(w);
	}
	
	public static WorldObject create(String organizationName, Profession profession, World world) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, -100000);
		properties.put(Constants.Y, -100000);
		properties.put(Constants.WIDTH, 4);
		properties.put(Constants.HEIGHT, 4);
		properties.put(Constants.NAME, organizationName);
		properties.put(Constants.ID, world.generateUniqueId());
		properties.put(Constants.IMAGE_ID, ImageIds.TREE);
		properties.put(Constants.PROFESSION, profession);
		
		WorldObject organization = new WorldObjectImpl(properties);
		world.addWorldObject(organization);
		
		return organization;
	}
}
