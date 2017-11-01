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
package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.WorldGenerator.AddWorldObjectFunction;

public class EggOnTurn implements OnTurn {

	private static final int PREGNANCY_DURATION = 150;
	
	private final AddWorldObjectFunction addWorldObjectFunction;
	
	public EggOnTurn(AddWorldObjectFunction addWorldObjectFunction) {
		this.addWorldObjectFunction = addWorldObjectFunction;
	}
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		int currentTurn = world.getCurrentTurn().getValue();
		
		worldObject.getProperty(Constants.CONDITIONS).onTurn(worldObject, world, creatureTypeChangedListeners);
		
		boolean eggHatched = PregnancyPropertyUtils.checkPregnancy(worldObject, world, currentTurn, PREGNANCY_DURATION, addWorldObjectFunction);
		if (eggHatched) {
			worldObject.setProperty(Constants.HIT_POINTS, 0);
		}
	}
}
