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
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.WorldObjectContainer;

public class StealAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		int thievery = useSkill(performer);
		//TODO: compare thievery with something to determine success
		WorldObject stolenWorldObject = targetInventory.remove(index);
		performerInventory.add(stolenWorldObject);
	}
	
	private int useSkill(WorldObject performer) {
		Skill thieverySkill = performer.getProperty(Constants.THIEVERY_SKILL);
		int result = thieverySkill.getLevel();
		thieverySkill.use();
		return result;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[1];
		argumentRanges[0] = new ArgumentRange(0, 100);
		return argumentRanges;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.INVENTORY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "stealing from " + target.getProperty(Constants.NAME);
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}