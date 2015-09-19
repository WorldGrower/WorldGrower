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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GoalUtils;

public class BuildSacrificalAltarAction implements BuildAction {

	private static final int REQUIRED_STONE = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
	
		Deity deity = performer.getProperty(Constants.DEITY);
		BuildingGenerator.generateSacrificialAltar(x, y, world, performer, deity, SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL));
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		if (target.hasProperty(Constants.ID)) {
			return false;
		} else {
			int x = (Integer)target.getProperty(Constants.X);
			int y = (Integer)target.getProperty(Constants.Y);
			return GoalUtils.isOpenSpace(x, y, 1, 2, world);
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, 1);
		int stoneTarget = CraftUtils.distance(performer, Constants.STONE, REQUIRED_STONE);
		int deityDistance = performer.getProperty(Constants.DEITY) != null ? 0 : 1;
		return distanceBetweenPerformerAndTarget + stoneTarget + deityDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.STONE, REQUIRED_STONE);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "building a sacrificial altar";
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
		return 2;
	}
	
	@Override
	public String getSimpleDescription() {
		return "build sacrificial altar";
	}
	
	public static boolean hasEnoughStone(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE) >= REQUIRED_STONE;
	}
}
