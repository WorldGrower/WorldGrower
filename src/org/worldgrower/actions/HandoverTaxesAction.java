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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class HandoverTaxesAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int collectedTaxes = performer.getProperty(Constants.ORGANIZATION_GOLD);
		
		performer.setProperty(Constants.ORGANIZATION_GOLD, 0);
		target.increment(Constants.ORGANIZATION_GOLD, collectedTaxes);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceOrganizationGold = performer.getProperty(Constants.ORGANIZATION_GOLD) > 0 ? 0 : 1;
		return Reach.evaluateTarget(performer, args, target, DISTANCE) + distanceOrganizationGold;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "target owes taxes");
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (GroupPropertyUtils.performerIsLeaderOfVillagers(target, world));
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "handing over collected taxes";
	}

	@Override
	public String getSimpleDescription() {
		return "handover collected taxes";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.SILVER_COIN;
	}
}