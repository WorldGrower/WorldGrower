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
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class LifeStealAction implements DeadlyAction {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.drainAttack(this, performer, target, args, world);
		
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(performer, target, Constants.CREATURE_TYPE, CreatureType.LICH_CREATURE_TYPE, world);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) 
				&& (target.getProperty(Constants.HIT_POINTS) > 0) 
				&& target.hasIntelligence() 
				&& performer.getProperty(Constants.CREATURE_TYPE) == CreatureType.LICH_CREATURE_TYPE)
				&& !performer.equals(target);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "only liches can steal the life of others");
	}
	
	@Override
	public String getDescription() {
		return "drains target of life and restores hit points";
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "lifestealing " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "life steal";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return "drained of life";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.BLOOD;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.MAGIC1;
	}
}
