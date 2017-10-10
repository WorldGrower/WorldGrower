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
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CapturePersonAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		List<WorldObject> jails = BuildingGenerator.getJails(world);
		WorldObject jail = jails.get(0);
		BuildingGenerator.addJailDoorIfNotPresent(jail, world);
		
		int newX = jail.getProperty(Constants.X) + 1;
		int newY = jail.getProperty(Constants.Y) + 1;
		LocationPropertyUtils.updateLocation(target, newX, newY, world);
		
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		villagersOrganization.getProperty(Constants.TURNS_IN_JAIL).incrementValue(target, world.getCurrentTurn().getValue());
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return target.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION)
				&& BuildingGenerator.getJails(world).size() > 0;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "only unconscious people can be captures", "a jail must exist");
	}
	
	@Override
	public String getDescription() {
		return "capture an unconscious person in order to throw them in jail";
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
		return "capturing " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "capture";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.CAPTURE;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.METAL_SMALL1;
	}	
}