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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.BuildAction;
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

public class TurnUndeadAction implements BuildAction, MagicSpell, DeadlyAction, AnimatedAction {
	private static final int ENERGY_USE = 300;
	private static final int BASE_DAMAGE = 8 * Item.COMBAT_MULTIPLIER;
	private static final int DISTANCE = 2;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int damage = BASE_DAMAGE;
		if (ConditionUtils.performerHasCondition(performer, Condition.HADES_BOON_CONDITION)) {
			damage += damage / 10;
		}
		
		List<WorldObject> targets = getAffectedTargets(target, world);
		for(WorldObject spellTarget : targets) {
			AttackUtils.magicAttack(damage, this, performer, spellTarget, args, world, SkillUtils.getSkillBonus(performer, getSkill()), DamageType.FIRE);
		}
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isInAreaOfEffect(x, y, w) && CreatureTypeUtils.isUndead(w));
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
		return 2;
	}

	@Override
	public int getHeight() {
		return 2;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "turning undead";
	}

	@Override
	public String getSimpleDescription() {
		return "turn undead";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 60;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.RESTORATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 5;
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return DamageType.FIRE.getDeathDescription();
	}

	@Override
	public String getDescription() {
		return "turns all undead creatures around the caster, dealing " + BASE_DAMAGE + " damage";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.TURN_UNDEAD;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.BLESSING;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.LIGHT4;
	}
}
