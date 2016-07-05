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
package org.worldgrower;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

/**
 * A ManagedOperation described an action that a WorldObject performs on another WorldObject.
 * A performer performs the action on a target with additional arguments args, and a World instance to lookup data.
 */
public interface ManagedOperation extends Serializable {

	public void execute(WorldObject performer, WorldObject target, int[] args, World world);
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world);
	public boolean requiresArguments();
	
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world);
	
	public int distance(WorldObject performer, WorldObject target, int[] args, World world);
	
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world);
	public String getSimpleDescription();
	public String getRequirementsDescription();
	public ImageIds getImageIds();
	
	public default boolean canExecuteIgnoringDistance(WorldObject performer, WorldObject target, int[] args, World world) {
		return new OperationInfo(performer, target, args, this).canExecuteIgnoringDistance(performer, world);
	}

	public default Object readResolveImpl() throws ObjectStreamException {
		Class<?> clazz = getClass();
		List<ManagedOperation> allActions = Actions.ALL_ACTIONS;
		
		for(ManagedOperation action : allActions) {
			if (action.getClass() == clazz) {
				return action;
			}
		}
		if (Actions.DO_NOTHING_ACTION.getClass() == clazz) {
			return Actions.DO_NOTHING_ACTION;
		}
		
		throw new IllegalStateException("ManagedProperty with class " + clazz + " not found");
	}
	public default SoundIds getSoundId() {
		return null;
	}
}
