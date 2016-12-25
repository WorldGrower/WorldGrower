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
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.Prices;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class SetPricesAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		Prices prices = performer.getProperty(Constants.PRICES);
		prices.setPrices(args);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}
	
	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "setting prices";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getSimpleDescription() {
		return "set price";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.GOLD;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.HANDLE_COINS;
	}
}