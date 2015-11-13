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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.ItemGenerator;
import org.worldgrower.gui.ImageIds;

public class BuildHouseAction implements BuildAction {

	private static final int REQUIRED_STONE = 14;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
	
		int id = BuildingGenerator.generateHouse(x, y, world, SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL, world.getWorldStateChangedListeners()));
		
		List<Integer> currentHouseIds = performer.getProperty(Constants.HOUSES).getIds();
		if (currentHouseIds.size() > 0) {
			int currentHouseId = currentHouseIds.get(0);
			List<WorldObject> shacks = world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == currentHouseId && BuildingGenerator.isShack(w));
			if (shacks.size() > 0) {
				world.removeWorldObject(shacks.get(0));
			}
		}
		
		performer.getProperty(Constants.INVENTORY).add(ItemGenerator.generateKey(id));
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.STONE, REQUIRED_STONE);
		performer.getProperty(Constants.HOUSES).add(id);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, 1);
		return CraftUtils.distance(performer, Constants.STONE, REQUIRED_STONE) + distanceBetweenPerformerAndTarget;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a house";
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
		return 4;
	}
	
	@Override
	public String getSimpleDescription() {
		return "build house";
	}
	
	public static boolean hasEnoughStone(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE) >= REQUIRED_STONE;
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.HOUSE;
	}
}
