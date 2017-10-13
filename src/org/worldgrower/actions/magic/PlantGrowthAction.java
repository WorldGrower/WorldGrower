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
package org.worldgrower.actions.magic;

import java.io.ObjectStreamException;
import java.util.List;
import java.util.Random;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.CottonPlantOnTurn;
import org.worldgrower.generator.GrapeVineOnTurn;
import org.worldgrower.generator.NightShadeOnTurn;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TreeOnTurn;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.util.TriConsumer;

public class PlantGrowthAction implements BuildAction, MagicSpell, AnimatedAction {
	private static final int ENERGY_USE = 400;
	private static final int DISTANCE = 5;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		growExistingPlants(target, world);
		addNewPlants(target, world);
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}

	private void growExistingPlants(WorldObject target, World world) {
		List<WorldObject> targets = getAffectedTargets(target, world);
		for(WorldObject spellTarget : targets) {
			if (spellTarget.hasProperty(Constants.WOOD_SOURCE)) {
				TreeOnTurn.increaseWoodAmountToMax(spellTarget, world);
			}
			if (spellTarget.hasProperty(Constants.FOOD_SOURCE)) {
				spellTarget.getProperty(Constants.FOOD_SOURCE).increaseFoodAmountToMax(spellTarget, world);
			}
			if (spellTarget.hasProperty(Constants.COTTON_SOURCE)) {
				CottonPlantOnTurn.increaseCottonAmountToMax(spellTarget, world);
			}
			if (spellTarget.hasProperty(Constants.NIGHT_SHADE_SOURCE)) {
				NightShadeOnTurn.increaseNightShadeAmountToMax(spellTarget, world);
			}
			if (spellTarget.hasProperty(Constants.GRAPE_SOURCE)) {
				GrapeVineOnTurn.increaseGrapeAmountToMax(spellTarget, world);
			}
		}
	}
	
	private void addNewPlants(WorldObject target, World world) {
		int targetX = (Integer)target.getProperty(Constants.X);
		int targetY = (Integer)target.getProperty(Constants.Y);
		
		addNewPlants(targetX, targetY, BuildingDimensions.TREE, PlantGenerator::generateTree, world);
		addNewPlants(targetX, targetY, BuildingDimensions.BERRY_BUSH, PlantGenerator::generateBerryBush, world);
	}
	
	private void addNewPlants(int targetX, int targetY, BuildingDimensions buildingDimensions, TriConsumer<Integer, Integer, World> plantGenerateFunction, World world) {
		Random random = new Random(world.getCurrentTurn().getValue() + buildingDimensions.getRealWidth() + buildingDimensions.getPlacementWidth());
		
		int plantCount = 5;
		for(int i=0; i<plantCount; i++) {
			int x = targetX + random.nextInt(getWidth());
			int y = targetY + random.nextInt(getHeight());
			if (GoalUtils.isOpenSpace(x, y, buildingDimensions, world)) {
				plantGenerateFunction.apply(x, y, world);
			}
		}
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		List<WorldObject> targets = world.findWorldObjects(w -> isInAreaOfEffect(x, y, w));
		return targets;
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return !target.hasProperty(Constants.ID) && MagicSpellUtils.canCast(performer, this);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "free left hand");
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public int getWidth() {
		return 10;
	}

	@Override
	public int getHeight() {
		return 10;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting plant growth";
	}

	@Override
	public String getSimpleDescription() {
		return "plant growth";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 40;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.TRANSMUTATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 4;
	}

	@Override
	public String getDescription() {
		return "all existing plants inside the affected area grow larger and new plants sprout into existence";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.PLANT_GROWTH;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.PLANT_GROWTH;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.PLANT_GROWTH_ANIMATON;
	}
}
