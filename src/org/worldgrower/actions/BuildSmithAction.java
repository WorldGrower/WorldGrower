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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class BuildSmithAction implements BuildAction {

	private static final int REQUIRED_STONE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int smithId = BuildingGenerator.generateSmith(x, y, world, performer);
		SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL, world.getWorldStateChangedListeners());
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.STONE, REQUIRED_STONE);
		performer.getProperty(Constants.BUILDINGS).add(smithId, BuildingType.SMITH);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.STONE, REQUIRED_STONE);
	}
	
	@Override
	public String getDescription() {
		return "a smithy is used for crafting weapons and armor";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a smithy";
	}

	@Override
	public String getSimpleDescription() {
		return "build smithy";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return BuildingDimensions.SMITH.getPlacementWidth();
	}

	@Override
	public int getHeight() {
		return BuildingDimensions.SMITH.getPlacementHeight();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SMITH;
	}
	
	public static boolean hasEnoughStone(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE) >= REQUIRED_STONE;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.BUILD_STONE_BUILDING;
	}
	
	@Override
	public List<ManagedOperation> getAllowedCraftActions(WorldObject performer, World world) {
		return Actions.getActionsWithTargetProperty(performer, Constants.SMITH_QUALITY, world);
	}
}