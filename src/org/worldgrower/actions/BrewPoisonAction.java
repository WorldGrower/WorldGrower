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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class BrewPoisonAction implements CraftAction {

	private static final int NIGHT_SHADE_REQUIRED = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.ALCHEMY_SKILL, world.getWorldStateChangedListeners());
		inventory.addQuantity(Item.POISON.generate(skillBonus));

		inventory.removeQuantity(Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.distance(performer, Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.APOTHECARY_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "brewing poison";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "brew poison";
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.POISON;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.ALCHEMIST;
	}
}