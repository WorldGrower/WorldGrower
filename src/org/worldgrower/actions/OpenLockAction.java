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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.ThieveryPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class OpenLockAction implements ManagedOperation, AnimatedAction {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean openLockIsSuccesfull = ThieveryPropertyUtils.isOpenLockSuccess(performer, target, world);
		if (openLockIsSuccesfull) {
			target.setProperty(Constants.LOCKED, Boolean.FALSE);
			world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " unlocks " + target.getProperty(Constants.NAME));
		} else {
			world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " fails to unlock " + target.getProperty(Constants.NAME));
		}
		
		SkillUtils.useSkill(performer, Constants.THIEVERY_SKILL, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.LOCKED)) && (target.getProperty(Constants.LOCKED)));
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithLeftHandByProperty(performer, target, Constants.LOCKPICK_QUALITY, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, Constants.LOCKPICK_QUALITY, 1);
	}
	
	@Override
	public String getDescription() {
		return "use a lockpick to open a locked container";
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "opening lock on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "open lock";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.LOCKPICK;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.PICKLOCK;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.LOCKPICK_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
