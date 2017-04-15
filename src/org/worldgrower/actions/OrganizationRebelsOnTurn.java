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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldOnTurn;
import org.worldgrower.attribute.IdList;
import org.worldgrower.goal.GroupPropertyUtils;

public class OrganizationRebelsOnTurn implements WorldOnTurn {

	@Override
	public void onTurn(World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		Integer leaderId = villagersOrganization.getProperty(Constants.ORGANIZATION_LEADER_ID);
		IdList rebelIds = villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS);
		
		if (leaderId == null && rebelIds.size() > 0) {
			villagersOrganization.setProperty(Constants.ORGANIZATION_REBEL_IDS, new IdList());
		}
		
		if (leaderId != null && rebelIds.size() > 0) {
			List<WorldObject> members = GroupPropertyUtils.findOrganizationMembers(villagersOrganization, world);
			if (rebelIds.size() > members.size() / 2) {
				startRebellion(villagersOrganization, world);
			}
		}
	}

	private void startRebellion(WorldObject villagersOrganization, World world) {
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, null);
		villagersOrganization.setProperty(Constants.ORGANIZATION_REBEL_IDS, new IdList());
		
		world.getWorldStateChangedListeners().fireRebellionStarted(villagersOrganization);
	}
}
