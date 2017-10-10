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
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class MeleeAttackAction implements DeadlyAction, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		SkillProperty skill = AttackUtils.determineSkill(performer);
		double skillBonus;
		if (performer.hasProperty(skill)) {
			skillBonus = SkillUtils.useSkill(performer, skill, world.getWorldStateChangedListeners());
		} else {
			skillBonus = 1.0f;
		}
		AttackUtils.attack(this, performer, target, args, world, skillBonus, AttackType.MELEE);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0));
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "attacks with a melee weapon, or with bare fists if no weapon is used";
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
		return "melee attack";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		WorldObject rightHandEquipment = performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		
		if ((leftHandEquipment == null) && (rightHandEquipment == null)) {
			return "pummeled to death";
		} else {
			return leftHandEquipment.getProperty(Constants.DAMAGE_TYPE).getDeathDescription();
		}
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.IRON_CLAYMORE;
	}
	
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SWING;
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
