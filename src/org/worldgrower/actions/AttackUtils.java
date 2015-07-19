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

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class AttackUtils {

	public static void attack(ManagedOperation action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		if (target.getProperty(Constants.DAMAGE_RESIST) == null) { throw new IllegalStateException("DamageResist is null in " + target); }
		float targetDamageResist = (float) target.getProperty(Constants.DAMAGE_RESIST);
		
		int performerDamage = performer.getProperty(Constants.DAMAGE);
		float performerEnergy = (float) performer.getProperty(Constants.ENERGY);
		
		int damage = (int) (performerDamage * skillBonus * (performerEnergy / 1000) * ((1000 - targetDamageResist) / 1000));
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " attacks " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		if (targetHP < 0) {
			targetHP = 0;
		}
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		world.logAction(action, performer, target, args, message);
	}
	
	public static void magicAttack(int performerDamage, ManagedOperation action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		
		int damage = (int) (performerDamage * skillBonus);
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " attacks " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		if (targetHP < 0) {
			targetHP = 0;
		}
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		world.logAction(action, performer, target, args, message);
	}
}
