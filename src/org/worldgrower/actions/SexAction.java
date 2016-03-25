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

import static org.worldgrower.goal.FacadeUtils.createFacade;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GenderPropertyUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.PregnancyPropertyUtils;
import org.worldgrower.goal.RacePropertyUtils;
import org.worldgrower.gui.ImageIds;

public class SexAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		if (!GenderPropertyUtils.hasSameGender(performer, target)) {
			if (performer.getProperty(Constants.GENDER).equals("female")) {
				PregnancyPropertyUtils.makePregnant(performer);
			}
			
			if (target.getProperty(Constants.GENDER).equals("female")) {
				PregnancyPropertyUtils.makePregnant(target);
			}
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		if (performer.hasProperty(Constants.RELATIONSHIPS) && target.hasProperty(Constants.RELATIONSHIPS)) {
			//TODO: this method indirectly invokes skill:use method, which shoumdn't be the case
			WorldObject performerFacade = createFacade(performer, performer, target, world);
			WorldObject targetFacade = createFacade(target, performer, target, world);
			return (target.hasIntelligence() && (targetFacade.getProperty(Constants.RELATIONSHIPS).getValue(performerFacade) > 750) && !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, targetFacade));
		} else {
			return RacePropertyUtils.hasSameRace(performer, target);
		}
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "having sex with " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "have sex";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.HEART;
	}
}