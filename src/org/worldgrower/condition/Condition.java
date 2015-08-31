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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.DeathReasonPropertyUtils;

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
	public void conditionEnds(WorldObject worldObject);
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world);
	
	public default void decreaseHitPoints(WorldObject worldObject, DeadlyCondition deadlyCondition, int value) {
		worldObject.increment(Constants.HIT_POINTS, -value);
		
		int hitPoints = worldObject.getProperty(Constants.HIT_POINTS);
		if (hitPoints == 0) {
			DeathReasonPropertyUtils.targetDiesByCondition(deadlyCondition, worldObject);
		}
	}
	
	public static final ParalyzedCondition PARALYZED_CONDITION = new ParalyzedCondition();
	public static final CocoonedCondition COCOONED_CONDITION = new CocoonedCondition();
	public static final BurningCondition BURNING_CONDITION = new BurningCondition();
	public static final PoisonedCondition POISONED_CONDITION = new PoisonedCondition();
	public static final VampireBiteCondition VAMPIRE_BITE_CONDITION = new VampireBiteCondition();
	public static final InvisibleCondition INVISIBLE_CONDITION = new InvisibleCondition();
	public static final EnlargedCondition ENLARGED_CONDITION = new EnlargedCondition();
	public static final ReducedCondition REDUCED_CONDITION = new ReducedCondition();
	public static final SleepCondition SLEEP_CONDITION = new SleepCondition();
	public static final WaterWalkCondition WATER_WALK_CONDITION = new WaterWalkCondition();
	public static final UnconsciousCondition UNCONSCIOUS_CONDITION = new UnconsciousCondition();
	
}
