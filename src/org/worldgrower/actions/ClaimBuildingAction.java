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
import org.worldgrower.generator.Item;
import org.worldgrower.goal.HousePropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.util.SentenceUtils;

public class ClaimBuildingAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		BuildingType buildingType = target.getProperty(Constants.BUILDING_TYPE);
		performer.getProperty(Constants.BUILDINGS).add(target, buildingType);
		
		if (buildingType == BuildingType.HOUSE) {
			HousePropertyUtils.removeShack(performer, world);
		}
		
		changeKeyNames(performer, target, world);
		addKeyToInventory(performer, target, world);
	}
	
	void changeKeyNames(WorldObject performer, WorldObject target, World world) {
		List<WorldObject> targetsWithKeys = world.findWorldObjectsByProperty(Constants.INVENTORY, w -> w.getProperty(Constants.INVENTORY).getIndexFor(Constants.LOCK_ID) != -1);

		int targetId = target.getProperty(Constants.ID).intValue();
		for(WorldObject targetsWithKey : targetsWithKeys) {
			List<WorldObject> keysForTarget = targetsWithKey.getProperty(Constants.INVENTORY).getWorldObjectsByFunction(Constants.LOCK_ID, w -> w.getProperty(Constants.LOCK_ID) == targetId);
			for(WorldObject keyForTarget : keysForTarget) {
				keyForTarget.setProperty(Constants.NAME, Item.getKeyName(target, performer));
			}
		}
	}
	
	private void addKeyToInventory(WorldObject performer, WorldObject target, World world) {
		int targetId = target.getProperty(Constants.ID).intValue();
		performer.getProperty(Constants.INVENTORY).addUniqueQuantity(Item.generateKey(targetId, performer, world));
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.BUILDING_TYPE));
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		List<WorldObject> owners = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.BUILDINGS) && w.getProperty(Constants.BUILDINGS).contains(target));
		return owners.size() == 0;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription("unowned building");
	}
	
	@Override
	public String getDescription() {
		return "claiming an unowned building results in becoming its owner";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		String targetName = target.getProperty(Constants.NAME);
		String article = SentenceUtils.getArticle(targetName);
		return "claiming " + article + " " + targetName;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "claim building";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.CLAIM_CATTLE;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.DOOR_OPEN;
	}	
}
