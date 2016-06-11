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
import org.worldgrower.creaturetype.CreatureTypeUtils;

class AttackActionLegalHandler implements ActionLegalHandler {
	
	@Override
	public boolean isApplicable(WorldObject performer, WorldObject target, int[] args) {
		return target.hasIntelligence() && target.hasProperty(Constants.GROUP) && CreatureTypeUtils.isHumanoid(target);
	}

	@Override
	public String getSimpleDescription() {
		return "";
	}
	
	@Override
	public int hashCode() {
		return 6;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof AttackActionLegalHandler);
	}
}
