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

import java.io.Serializable;

import org.worldgrower.World;
import org.worldgrower.WorldObject;


public interface WoodSource extends Serializable {

	public WorldObject harvest(WorldObject performer, WorldObject target, World world);
	public boolean hasEnoughWood();
	
	public void increaseWoodAmount(int woodIncrease, WorldObject target, World world);
	public void increaseWoodAmountToMax(WorldObject target, World world);
}
