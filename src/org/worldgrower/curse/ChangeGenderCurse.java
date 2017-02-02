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
package org.worldgrower.curse;

import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GenderPropertyUtils;
import org.worldgrower.goal.Goal;
import org.worldgrower.gui.ImageIds;

public class ChangeGenderCurse implements Curse {

	@Override
	public List<Goal> getCurseGoals(List<Goal> normalGoals) {
		return normalGoals;
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
	}
	
	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public String getExplanation() {
		return "I've been cursed with change gender.";
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public String getName() {
		return "change gender";
	}
	
	@Override
	public final String getDescription() {
		return "A character cursed with change gender changes its gender";
	}
	
	@Override
	public final ImageIds getImageId() {
		return ImageIds.CHANGE_GENDER_CURSE;
	}
	
	@Override
	public void curseStarts(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners) {
		GenderPropertyUtils.changeGender(worldObject, worldStateChangedListeners);
	}

	@Override
	public void curseEnds(WorldObject worldObject, WorldStateChangedListeners worldStateChangedListeners) {
		GenderPropertyUtils.changeGender(worldObject, worldStateChangedListeners);
	}
	
	@Override
	public boolean performerWantsCurseRemoved(WorldObject performer, World world) {
		return false;
	}
}
