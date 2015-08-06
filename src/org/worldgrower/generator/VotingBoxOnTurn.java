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
package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.attribute.IdMap;

public class VotingBoxOnTurn implements OnTurn {


	
	@Override
	public void onTurn(WorldObject worldObject, World world) {
		
		worldObject.increment(Constants.TURN_COUNTER, 1);
		
		if (VotingPropertyUtils.isVotingdone(worldObject)) {
			IdMap votes = worldObject.getProperty(Constants.VOTES);
			int newLeaderId = votes.findBestId(w -> true, world);
			int organizationId = worldObject.getProperty(Constants.ORGANIZATION_ID);
			WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
			organization.setProperty(Constants.ORGANIZATION_LEADER_ID, newLeaderId);
			
			world.removeWorldObject(worldObject);
		}
	}
}
