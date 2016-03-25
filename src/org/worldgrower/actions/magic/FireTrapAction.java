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

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.actions.DeadlyAction;
import org.worldgrower.attribute.Location;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class FireTrapAction implements MagicSpell, DeadlyAction, BuildAction {

	private static final int ENERGY_USE = 400;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int id = TerrainGenerator.generateFireTrap(x, y, world, SkillUtils.getSkillBonus(performer, getSkill()));
		WorldObject fireTrap = world.findWorldObject(Constants.ID, id);
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfProperty(performer, fireTrap, Constants.LOCATION, new Location(x, y), world);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidBuildTarget(this, performer, target, world) && MagicSpellUtils.canCast(performer, this);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, DISTANCE);
		return distanceBetweenPerformerAndTarget 
				+ SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE);
	}
	
	@Override
	public boolean requiresArguments() {
		return true;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating a fire trap";
	}

	@Override
	public String getSimpleDescription() {
		return "fire trap";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 30;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.EVOCATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 3;
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return "burned to death";
	}

	@Override
	public String getDescription() {
		return "creates a firetrap which explodes if anyone steps on it";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.FIRE_TRAP;
	}
	
	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}
}
