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
package org.worldgrower.deity;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldOnTurn;
import org.worldgrower.goal.GroupPropertyUtils;

public class DeityWorldOnTurn implements WorldOnTurn {
	
	@Override
	public void onTurn(World world) {

		DeityAttributes deityAttributes = GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.DEITY_ATTRIBUTES);
		deityAttributes.onTurn(world);
		
		for(Deity deity : Deity.ALL_DEITIES) {
			deity.onTurn(world, world.getWorldStateChangedListeners());
		}
	}
}
