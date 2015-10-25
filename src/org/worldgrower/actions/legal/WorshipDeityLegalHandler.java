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
import org.worldgrower.deity.Deity;

public class WorshipDeityLegalHandler implements ActionLegalHandler {
	
	private final Deity deity;
	
	public WorshipDeityLegalHandler(Deity deity) {
		this.deity = deity;
	}

	@Override
	public boolean isApplicable(WorldObject performer, WorldObject target, int[] args) {
		return performer.getProperty(Constants.DEITY) == deity;
	}

	@Override
	public String getSimpleDescription() {
		return deity.getName();
	}
}
