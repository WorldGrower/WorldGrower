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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.IllusionOnTurn;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class MinorIllusionAction implements BuildAction, MagicSpell {

	private static final int ENERGY_USE = 400;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		int sourceId = args[0];
		WorldObject sourceWorldObject = world.findWorldObject(Constants.ID, sourceId);
		
		WorldObjectImpl illusionWorldObject = (WorldObjectImpl) sourceWorldObject.deepCopy(new IllusionOnTurn());
		illusionWorldObject.setProperty(Constants.ID, world.generateUniqueId());
		illusionWorldObject.setProperty(Constants.ILLUSION_CREATOR_ID, performer.getProperty(Constants.ID));
		illusionWorldObject.setProperty(Constants.X, x);
		illusionWorldObject.setProperty(Constants.Y, y);
		illusionWorldObject.setProperty(Constants.TURNS_TO_LIVE, (int)(10 * SkillUtils.getSkillBonus(performer, getSkill())));
		illusionWorldObject.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		
		world.addWorldObject(illusionWorldObject);
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		return GoalUtils.isOpenSpace(x, y, 1, 1, world) && !target.hasProperty(Constants.ID) && MagicSpellUtils.canCast(performer, this);
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
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[1];
		argumentRanges[0] = new ArgumentRange(0, 100);
		return argumentRanges;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating a minor illusion";
	}

	@Override
	public String getSimpleDescription() {
		return "create minor illusion";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public int getResearchCost() {
		return 50;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.ILLUSION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "creates a small illusion of an existing person or item";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.MINOR_ILLUSION_MAGIC_SPELL;
	}
}
