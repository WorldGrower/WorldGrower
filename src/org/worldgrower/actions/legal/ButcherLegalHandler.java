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
package org.worldgrower.actions.legal;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class ButcherLegalHandler implements ActionLegalHandler {

	@Override
	public boolean isApplicable(WorldObject performer, WorldObject target, int[] args) {
		return target.hasProperty(Constants.CATTLE_OWNER_ID) 
				&& target.getProperty(Constants.CATTLE_OWNER_ID) != null 
				&& target.getProperty(Constants.CATTLE_OWNER_ID).intValue() != performer.getProperty(Constants.ID);
	}

	@Override
	public String getSimpleDescription() {
		return "unowned cattle";
	}
	
	@Override
	public int hashCode() {
		return 6;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ButcherLegalHandler) {
			return true;
		} else {
			return false;
		}
	}
}
