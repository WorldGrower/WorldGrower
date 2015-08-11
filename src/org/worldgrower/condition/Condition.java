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
package org.worldgrower.condition;

import java.io.Serializable;

import org.worldgrower.World;
import org.worldgrower.WorldObject;

/**
 * A Condition is something WorldObjects can have.
 * It lasts several turns and can limit the actions of the WorldObject.
 */
public interface Condition extends Serializable {

	public boolean canTakeAction();
	public boolean canMove();
	public String getDescription();
	public void onTurn(WorldObject worldObject, World world, int startTurns);	
	public boolean isDisease();
	
	public static final ParalyzedCondition PARALYZED_CONDITION = new ParalyzedCondition();
	public static final CocoonedCondition COCOONED_CONDITION = new CocoonedCondition();
	public static final BurningCondition BURNING_CONDITION = new BurningCondition();
	public static final PoisonedCondition POISONED_CONDITION = new PoisonedCondition();
	public static final VampireBiteCondition VAMPIRE_BITE_CONDITION = new VampireBiteCondition();
}
