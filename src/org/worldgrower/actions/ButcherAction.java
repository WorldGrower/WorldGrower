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
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.util.SentenceUtils;

public class ButcherAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		double skillBonus = SkillUtils.useSkill(performer, Constants.FARMING_SKILL, world.getWorldStateChangedListeners());
		
		int meatQuantity = addMeatToInventory(performer, target, inventoryPerformer, skillBonus);
		int leatherQuantity = addLeatherToInventory(performer, target, inventoryPerformer, skillBonus);
		
		target.setProperty(Constants.HIT_POINTS, 0);
		
		world.logAction(this, performer, target, args, meatQuantity + " "+ Constants.MEAT_SOURCE + " and " + leatherQuantity + " leather added to inventory");
	}

	private int addMeatToInventory(WorldObject performer, WorldObject target, WorldObjectContainer inventoryPerformer, double skillBonus) {
		int meatSource = target.getProperty(Constants.MEAT_SOURCE);
		WorldObject collectedMeat = Item.MEAT.generate(1f);
		collectedMeat.setProperty(Constants.CREATURE_TYPE, target.getProperty(Constants.CREATURE_TYPE));
		int butcherKnifeBonus = getButcherKnifeBonus(performer);
		int meatQuantity = (int) (meatSource * skillBonus * butcherKnifeBonus);
		
		inventoryPerformer.addQuantity(collectedMeat, meatQuantity);
		return meatQuantity;
	}
	
	private int addLeatherToInventory(WorldObject performer, WorldObject target, WorldObjectContainer inventoryPerformer, double skillBonus) {
		int butcherKnifeBonus = getButcherKnifeBonus(performer);
		int leatherQuantity = (int) (5 * skillBonus * butcherKnifeBonus);
		
		inventoryPerformer.addQuantity(Item.LEATHER.generate(skillBonus), leatherQuantity);
		return leatherQuantity;
	}
	
	private static int getButcherKnifeBonus(WorldObject performer) {
		boolean leftHandContainsButcherKnife = FoodPropertyUtils.leftHandContainsButcherKnife(performer);
		if (leftHandContainsButcherKnife) {
			return 2;
		} else {
			return 1;
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "butchering something kills it while providing meat. Butchering a more mature creature or using a butcher knife provides more meat";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.MEAT_SOURCE)) && (target.getProperty(Constants.MEAT_SOURCE) > 0);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		String targetName = target.getProperty(Constants.NAME);
		String article = SentenceUtils.getArticle(targetName);
		
		return "butchering " + article + " " + targetName;
	}

	@Override
	public String getSimpleDescription() {
		return "butcher";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.MEAT;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.KNIFE_SLICE;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.SLASH1;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}