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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.UnpassableCreaturePositionCondition;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.condition.WorldStateChangedListeners;

public class FireTrapOnTurn implements OnTurn {

	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		
		List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, new UnpassableCreaturePositionCondition(y, x));
		
		if (targets.size() > 0) {
			int damage = worldObject.getProperty(Constants.DAMAGE);
			AttackUtils.magicAttack(damage, Actions.FIRE_TRAP_ACTION, worldObject, targets.get(0), new int[0], world, 1.0f);
			world.removeWorldObject(worldObject);
		}
	}
}
