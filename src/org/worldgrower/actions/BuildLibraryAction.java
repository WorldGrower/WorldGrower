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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;

public class BuildLibraryAction implements BuildAction {

	private static final int REQUIRED_WOOD = 6;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int libraryId = BuildingGenerator.generateLibrary(x, y, world);
		
		performer.getProperty(Constants.INVENTORY).add(Item.generateKey(libraryId));
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.WOOD, REQUIRED_WOOD);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, 1);
		return CraftUtils.distance(performer, Constants.WOOD, REQUIRED_WOOD) + distanceBetweenPerformerAndTarget;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WOOD, REQUIRED_WOOD);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a library";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return 2;
	}

	@Override
	public int getHeight() {
		return 2;
	}
	

	@Override
	public String getSimpleDescription() {
		return "build library";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.LIBRARY;
	}
}