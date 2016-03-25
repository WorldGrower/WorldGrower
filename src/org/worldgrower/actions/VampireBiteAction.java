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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class VampireBiteAction implements DeadlyAction {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.biteAttack(this, performer, target, args, world);
		performer.increment(Constants.VAMPIRE_BLOOD_LEVEL, 500);
		Conditions.add(target, Condition.VAMPIRE_BITE_CONDITION, Integer.MAX_VALUE, world);
		
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) 
				&& (target.getProperty(Constants.HIT_POINTS) > 0) 
				&& target.hasIntelligence() 
				&& target.getProperty(Constants.CREATURE_TYPE).hasBlood())
				&& !performer.equals(target);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerIsVampireDistance = performer.hasProperty(Constants.VAMPIRE_BLOOD_LEVEL) ? 0 : 1;
		return Reach.evaluateTarget(performer, args, target, DISTANCE)
				+ performerIsVampireDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "only vampires can bite others");
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "biting " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "bite";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return "drained of blood";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BLOOD;
	}
}
