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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.gui.ImageIds;

public class MeleeAttackAction implements DeadlyAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.attack(this, performer, target, args, world, SkillUtils.useSkill(performer, AttackUtils.determineSkill(performer), world.getWorldStateChangedListeners()));
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
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
		} else if (leftHandEquipment.getProperty(Constants.DAMAGE_TYPE) == DamageType.SLASHING) {
			return "slashed to death";
		} else if (leftHandEquipment.getProperty(Constants.DAMAGE_TYPE) == DamageType.BLUDGEONING) {
			return "bludgeoned to death";
		} else if (leftHandEquipment.getProperty(Constants.DAMAGE_TYPE) == DamageType.PIERCING) {
			return "having internal organs pierced to death";
		} else {
			throw new IllegalStateException("performer " + performer.toString() + " has unsupported damageType " + leftHandEquipment.getProperty(Constants.DAMAGE_TYPE));
		}
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.IRON_CLAYMORE;
	}
}
