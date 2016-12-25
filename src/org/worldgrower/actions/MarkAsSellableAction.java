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
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class MarkAsSellableAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		target.setProperty(Constants.SELLABLE, Boolean.TRUE);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return BuildingGenerator.isSellable(target) && performer.getProperty(Constants.BUILDINGS).contains(target);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return !(target.hasProperty(Constants.SELLABLE) && target.getProperty(Constants.SELLABLE));
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "item should not already be sellable");
	}
	
	@Override
	public String getDescription() {
		return "marks a building as sellable, signaling to others that the building is for sale";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "marking a building for sale";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	
	@Override
	public String getSimpleDescription() {
		return "mark for sale";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SILVER_COIN;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.HANDLE_COINS;
	}
}
