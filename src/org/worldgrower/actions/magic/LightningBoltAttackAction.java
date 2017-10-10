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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.actions.DeadlyAction;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.terrain.TerrainType;

public class LightningBoltAttackAction implements MagicSpell, DeadlyAction, AnimatedAction {

	private static final int BASE_DAMAGE = 5 * Item.COMBAT_MULTIPLIER;
	private static final int ENERGY_USE = 600;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int damage = BASE_DAMAGE;
		if (ConditionUtils.performerHasCondition(performer, Condition.ZEUS_BOON_CONDITION)) {
			damage += damage / 10;
		}
		
		AttackUtils.magicAttack(damage, this, performer, target, args, world, SkillUtils.getSkillBonus(performer, getSkill()), DamageType.LIGHTNING);
	
		int targetX = target.getProperty(Constants.X);
		int targetY = target.getProperty(Constants.Y);
		TerrainType terrainType = world.getTerrain().getTerrainType(targetX, targetY);
		
		if (terrainType == TerrainType.WATER) {
			List<WorldObject> worldObjectsInSurroundingWater = LocationUtils.findWorldObjectsInSurroundingWater(targetX, targetY, world);
			for(WorldObject worldObjectInSurroundingWater : worldObjectsInSurroundingWater) {
				AttackUtils.magicAttack(5, this, performer, worldObjectInSurroundingWater, args, world, SkillUtils.getSkillBonus(performer, getSkill()), DamageType.LIGHTNING);
			}
		}
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0) && MagicSpellUtils.canCast(performer, this));
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
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE, "free left hand");
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "attacking " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "lightning bolt";
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
		return Constants.EVOCATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return DamageType.LIGHTNING.getDeathDescription();
	}
	
	public boolean hasRequiredEnergy(WorldObject performer) {
		return performer.getProperty(Constants.ENERGY) >= ENERGY_USE;
	}

	@Override
	public String getDescription() {
		return "deals " + BASE_DAMAGE + " damage to target and other targets if something conducts the electricity";
	}

	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.LIGHTNING_BOLT;
	}
	
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SHOCK;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.THUNDER1;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
