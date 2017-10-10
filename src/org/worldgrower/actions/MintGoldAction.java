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
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class MintGoldAction implements CraftAction {

	private static final int GOLD_REQUIRED = 5;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.SMITHING_SKILL, world.getWorldStateChangedListeners());
		int quantityBonus = target.getProperty(Constants.SMITH_QUALITY);
		int goldMinted = (int) (20 * skillBonus * quantityBonus);
		performer.increment(Constants.GOLD, goldMinted);

		inventory.removeQuantity(Constants.GOLD, GOLD_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.GOLD, GOLD_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.GOLD, GOLD_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "mints gold from raw gold ore";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.SMITH_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "minting gold";
	}
	
	@Override
	public FormattableText getFormattableSimpleDescription() {
		return new FormattableText(TextId.MINT_GOLD, Item.GOLD);
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.GOLD;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SMITH;
	}
}