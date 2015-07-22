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
import org.worldgrower.generator.ItemGenerator;

public class MintGoldAction implements ManagedOperation {

	private static final int GOLD_REQUIRED = 5;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = CraftUtils.useSmithingSkill(performer);
		inventory.addQuantity(ItemGenerator.getIronBoots(skillBonus));
		int goldMinted = (int) (20 * skillBonus);
		performer.increment(Constants.GOLD, goldMinted);

		inventory.removeQuantity(Constants.GOLD, GOLD_REQUIRED);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.distance(performer, Constants.GOLD, GOLD_REQUIRED);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "minting gold";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}