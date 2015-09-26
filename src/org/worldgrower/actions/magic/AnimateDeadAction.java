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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.GroupPropertyUtils;

public class AnimateDeadAction implements MagicSpell {

	private static final int ENERGY_USE = 200;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		double skillBonus = SkillUtils.useSkill(performer, getSkill());
		
		final WorldObject minionOrganization;
		List<WorldObject> minionOrganizations = world.findWorldObjects(w -> GroupPropertyUtils.isMinionOrganization(w) && GroupPropertyUtils.performerIsLeaderOfOrganization(performer, w, world));
		if (minionOrganizations.size() > 0) {
			minionOrganization = minionOrganizations.get(0);
		} else {
			minionOrganization = GroupPropertyUtils.create(performer.getProperty(Constants.ID), "minions of " + performer.getProperty(Constants.NAME), world);
			minionOrganization.setProperty(Constants.MINION_ORGANIZATION, Boolean.TRUE);
			performer.getProperty(Constants.GROUP).add(minionOrganization);
		}
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(minionOrganization);
		Integer targetX = target.getProperty(Constants.X);
		Integer targetY = target.getProperty(Constants.Y);
		int skeletonId = creatureGenerator.generateSkeleton(targetX, targetY, world, performer);
		WorldObject skeleton = world.findWorldObject(Constants.ID, skeletonId);
		skeleton.getProperty(Constants.GROUP).addAll(performer.getProperty(Constants.GROUP));
		
		world.removeWorldObject(target);
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.DECEASED_WORLD_OBJECT)) && target.getProperty(Constants.DECEASED_WORLD_OBJECT));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, DISTANCE)
				+ SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE);
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "animating " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "animate";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 20;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.NECROMANCY_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "animates a corpse and turns it into a skelton which you control";
	}
}
