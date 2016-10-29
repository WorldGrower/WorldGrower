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
import org.worldgrower.creaturetype.CreatureTypeUtils;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class SacredFlameAttackAction implements MagicSpell, DeadlyAction, AnimatedAction {

	private static final int BASE_DAMAGE = 8 * Item.COMBAT_MULTIPLIER;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int damage = BASE_DAMAGE;
		if (ConditionUtils.performerHasCondition(performer, Condition.HADES_BOON_CONDITION)) {
			damage += damage / 10;
		}
		
		AttackUtils.magicAttack(damage, this, performer, target, args, world, SkillUtils.useSkill(performer, getSkill(), world.getWorldStateChangedListeners()));
	
		world.logAction(this, performer, target, args, null);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) 
				&& (target.getProperty(Constants.HIT_POINTS) > 0) 
				&& MagicSpellUtils.canCast(performer, this)
				&& CreatureTypeUtils.isUndead(target));
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
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
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "attacking " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "sacred flame";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 10;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.RESTORATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 0;
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return DamageType.FIRE.getDeathDescription();
	}

	@Override
	public String getDescription() {
		return "fires sacred flame at the target dealing " + BASE_DAMAGE + " damage";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.SACRED_FLAME;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.BLESSING;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.LIGHT4;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
