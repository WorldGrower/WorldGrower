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
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class WorshipDeityAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		performer.increment(Constants.WORSHIP_COUNTER, 1);
		
		performer.getProperty(Constants.DEITY).worship(performer, target, performer.getProperty(Constants.WORSHIP_COUNTER), world);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean hasSameDeity = (performer.getProperty(Constants.DEITY) == target.getProperty(Constants.DEITY));
		return hasSameDeity;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "must worship deity");
	}
	
	@Override
	public String getDescription() {
		return "worship can lead to bonuses and boons granted depending on which deity is worshipped";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CAN_BE_WORSHIPPED) && (target.getProperty(Constants.CAN_BE_WORSHIPPED)));
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "worshipping my deity " + performer.getProperty(Constants.DEITY);
	}

	@Override
	public String getSimpleDescription() {
		return "worship";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.WORSHIP;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.RELIGIOUS;
	}	
}
