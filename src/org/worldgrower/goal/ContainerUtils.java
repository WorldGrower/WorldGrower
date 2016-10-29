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
package org.worldgrower.goal;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.condition.Condition;

public class ContainerUtils {

	public static void accessContainer(WorldObject performer, WorldObject target, World world) {
		boolean isTrappedContainer = target.getProperty(Constants.CONDITIONS).hasCondition(Condition.TRAPPED_CONTAINER_CONDITION);
		if (isTrappedContainer) {
			int damage = target.getProperty(Constants.TRAPPED_CONTAINER_DAMAGE);
			AttackUtils.magicAttack(damage, Actions.TRAP_CONTAINER_MAGIC_SPELL_ACTION, target, performer, Args.EMPTY, world, 1, DamageType.SLASHING);
		}
	}
	
	public static OperationInfo avoidTrappedContainer(WorldObject performer, WorldObject target, World world) {
		boolean isTrappedContainer = performer.getProperty(Constants.KNOWLEDGE_MAP).hasProperty(target, Constants.TRAPPED_CONTAINER_DAMAGE);
		if (isTrappedContainer) {
			if (MagicSpellUtils.canCast(performer, Actions.DISPEL_MAGIC_ACTION)) {
				if (Actions.DISPEL_MAGIC_ACTION.hasRequiredEnergy(performer)) {
					return new OperationInfo(performer, target, Args.EMPTY, Actions.DISPEL_MAGIC_ACTION);
				} else {
					return Goals.REST_GOAL.calculateGoal(performer, world);
				}
			} else {
				//TODO: ask someone to dispel
				return null;
			}
		}
		return null;
	}
	
	public static boolean canAccessContainer(WorldObject performer, WorldObject target, World world) {
		boolean targetIsSummon = SummonPropertyUtils.targetIsSummonOfPerformer(performer, target, world);
		boolean targetIsUnintelligentContainer = !target.hasIntelligence();
		return ((targetIsSummon || targetIsUnintelligentContainer) && target.hasProperty(Constants.INVENTORY));
	}
}
