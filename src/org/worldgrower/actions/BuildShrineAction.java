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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class BuildShrineAction implements BuildAction {

	private static final int REQUIRED_STONE = 8;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int shrineId = BuildingGenerator.generateShrine(x, y, world, performer);
		SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL, world.getWorldStateChangedListeners());
		
		performer.setProperty(Constants.PLACE_OF_WORSHIP_ID, shrineId);
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean hasDeity = performer.getProperty(Constants.DEITY) != null;
		return hasDeity && CraftUtils.hasEnoughResources(performer, Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.STONE, REQUIRED_STONE, "must worship a deity");
	}
	
	@Override
	public String getDescription() {
		return "a shrine is used for deity worship";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a shrine";
	}
	
	@Override
	public String getSimpleDescription() {
		return "build shrine";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return BuildingDimensions.SHRINE.getPlacementWidth();
	}

	@Override
	public int getHeight() {
		return BuildingDimensions.SHRINE.getPlacementHeight();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		Deity deity = performer.getProperty(Constants.DEITY);
		if (deity != null) {
			return deity.getStatueImageId();
		} else {
			return ImageIds.STATUE_OF_DEITY;
		}
	}
	
	public SoundIds getSoundId() {
		return SoundIds.BUILD_STONE_BUILDING;
	}
}
