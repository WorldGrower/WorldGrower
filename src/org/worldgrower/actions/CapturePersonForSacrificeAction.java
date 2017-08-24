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
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.goal.SacrificeUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CapturePersonForSacrificeAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		List<WorldObject> sacrificialAltars = SacrificeUtils.getSacrificialAltars(performer, world);
		WorldObject sacrificialAltar = sacrificialAltars.get(0);
		
		int newX = sacrificialAltar.getProperty(Constants.X);
		int newY = sacrificialAltar.getProperty(Constants.Y);
		
		LocationPropertyUtils.updateLocation(target, newX, newY, world);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean isUnconsciousTarget = target.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION);
		boolean hasAltars = SacrificeUtils.getSacrificialAltars(performer, world).size() > 0;
		return isUnconsciousTarget && hasAltars;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "only unconscious people can be captures", "a sacrificial altar needs to exist");
	}
	
	@Override
	public String getDescription() {
		return "capture an unconscious person in order to sacrifice them";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasIntelligence() && target.hasProperty(Constants.CONDITIONS));
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "capturing " + target.getProperty(Constants.NAME) + " for sacrifice";
	}

	@Override
	public String getSimpleDescription() {
		return "capture person for sacrifice";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.CAPTURE;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.METAL_SMALL1;
	}
}