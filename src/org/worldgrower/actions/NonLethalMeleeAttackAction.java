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
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class NonLethalMeleeAttackAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		double skillBonus = SkillUtils.useSkill(performer, AttackUtils.determineSkill(performer), world.getWorldStateChangedListeners());
		AttackUtils.nonLethalAttack(this, performer, target, args, world, skillBonus, AttackType.MELEE);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0) && (target.hasIntelligence()));
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
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
		return "attacking " + target.getProperty(Constants.NAME) + " in a non lethal manner";
	}

	@Override
	public String getSimpleDescription() {
		return "non lethal melee attack";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.IRON_CLAYMORE;
	}
	
	public SoundIds getSoundId() {
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
