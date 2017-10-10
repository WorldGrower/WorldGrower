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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CreateGraveAction implements BuildAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		int indexOfRemains = inventory.getIndexFor(Constants.DECEASED_WORLD_OBJECT);
		WorldObject deceasedWorldObject = inventory.get(indexOfRemains);
		inventory.remove(indexOfRemains);
		
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		BuildingGenerator.generateGrave(x, y, world, deceasedWorldObject);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.DECEASED_WORLD_OBJECT) != -1;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return "Requirements: remains in inventory";
	}
	
	@Override
	public String getDescription() {
		return "a grave stores the remains of a dead person";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating grave";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "create grave";
	}

	@Override
	public int getWidth() {
		return getBuildingDimensions().getPlacementWidth();
	}

	@Override
	public int getHeight() {
		return getBuildingDimensions().getPlacementHeight();
	}

	private BuildingDimensions getBuildingDimensions() {
		return BuildingDimensions.GRAVE;
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.GRAVE;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SHOVEL;
	}
}