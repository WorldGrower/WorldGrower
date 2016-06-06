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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.goal.IllusionPropertyUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class MajorIllusionAction implements BuildAction, MagicSpell, IllusionSpell {

	private static final int ENERGY_USE = 600;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int sourceId = args[0];
		IllusionPropertyUtils.createIllusion(performer, sourceId, world, x, y);
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
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
		return "creating a major illusion";
	}

	@Override
	public String getSimpleDescription() {
		return "create major illusion";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
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
	public int getResearchCost() {
		return 60;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.ILLUSION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "creates an illusion of an existing item";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.MAJOR_ILLUSION_MAGIC_SPELL;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.MAGIC7;
	}
	
	@Override
	public List<WorldObject> getIllusionSources(World world) {
		return world.findWorldObjects(w -> w.getProperty(Constants.WIDTH) == 2 && w.getProperty(Constants.HEIGHT) == 2);
	}
}
