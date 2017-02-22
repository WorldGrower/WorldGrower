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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class BrewPoisonAction implements CraftAction, AnimatedAction {
	private static final int DISTANCE = 1;
	private static final int NIGHT_SHADE_REQUIRED = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.ALCHEMY_SKILL, world.getWorldStateChangedListeners());
		int quantity = target.getProperty(Constants.APOTHECARY_QUALITY);
		inventory.addQuantity(Item.POISON.generate(skillBonus), quantity);

		inventory.removeQuantity(Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.NIGHT_SHADE, NIGHT_SHADE_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "Poison deals damage if ingested or if dealt damage by poisoned weapons";
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
	public FormattableText getFormattableText() {
		return new FormattableText(TextId.BREW_ITEM, Item.POISON);
	}

	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.POISON;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.ALCHEMIST;
	}

	public boolean hasEnoughNightShade(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.NIGHT_SHADE) >= NIGHT_SHADE_REQUIRED;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.POISON_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}