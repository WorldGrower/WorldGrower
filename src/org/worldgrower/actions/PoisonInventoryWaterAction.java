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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;

public class PoisonInventoryWaterAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		int indexOfWater = inventory.getIndexFor(Constants.WATER);
		int indexOfPoison = inventory.getIndexFor(Constants.POISON_DAMAGE);
		
		int poisonDamage = inventory.get(indexOfPoison).getProperty(Constants.POISON_DAMAGE);
		inventory.get(indexOfWater).setProperty(Constants.POISON_DAMAGE, poisonDamage);
		
		inventory.removeQuantity(Constants.POISON_DAMAGE, 1);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (performer.equals(target) 
				&& (performer.hasProperty(Constants.INVENTORY)) 
				&& (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WATER) > 0)
				&& (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.POISON_DAMAGE) > 0));
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "poisoning inventory water";
	}

	@Override
	public String getSimpleDescription() {
		return "poison inventory water";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}