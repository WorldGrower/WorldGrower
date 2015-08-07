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

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;

public class SetTaxRateAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		GroupPropertyUtils.getVillagersOrganization(world).setProperty(Constants.SHACK_TAX_RATE, args[0]);
		GroupPropertyUtils.getVillagersOrganization(world).setProperty(Constants.HOUSE_TAX_RATE, args[1]);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean performerIsLeader = GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world);
		return performerIsLeader ? 0 : 1;
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return new ArgumentRange[] {new ArgumentRange(0, 100), new ArgumentRange(0, 100) };
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "setting tax rate";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "set tax rate";
	}
}