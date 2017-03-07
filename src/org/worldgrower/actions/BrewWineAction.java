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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class BrewWineAction implements CraftAction, AnimatedAction {

	private static final int GRAPES_REQUIRED = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.ALCHEMY_SKILL, world.getWorldStateChangedListeners());
		int quantity = target.getProperty(Constants.BREWERY_QUALITY);
		if (ConditionUtils.performerHasCondition(performer, Condition.DIONYSUS_BOON_CONDITION)) {
			quantity++;
		}
		inventory.addQuantity(Item.WINE.generate(skillBonus), quantity);

		inventory.removeQuantity(Constants.GRAPE, GRAPES_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.GRAPE, GRAPES_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.GRAPE, GRAPES_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "drinking wine relieves thirst and may make someone intoxicated";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.BREWERY_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "brewing wine";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public FormattableText getFormattableSimpleDescription() {
		return new FormattableText(TextId.BREW_ITEM, Item.WINE);
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.WINE;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.ALCHEMIST;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.WINE_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}