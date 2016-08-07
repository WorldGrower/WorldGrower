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
import org.worldgrower.generator.BerryBushImageCalculator;
import org.worldgrower.gui.AnimationId;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class EatAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int foodInTarget = target.getProperty(Constants.FOOD_SOURCE);
		int foodIncrease = 100 * FoodPropertyUtils.calculateFarmingQuantity(performer);
		performer.increment(Constants.FOOD, foodIncrease);
		target.setProperty(Constants.FOOD_SOURCE, foodInTarget - 100);

		target.setProperty(Constants.IMAGE_ID, BerryBushImageCalculator.getImageId(target, world));
		
		FoodPropertyUtils.checkFoodSourceExhausted(target);
		SkillUtils.useSkill(performer, Constants.FARMING_SKILL, world.getWorldStateChangedListeners());
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
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
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return FoodPropertyUtils.foodSourceHasEnoughFood(target);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "eating " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "eat";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BERRY;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.EAT;
	}
	
	@Override
	public AnimationId getAnimationId() {
		return AnimationId.BLACK_CRESCENT_SLASH;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}