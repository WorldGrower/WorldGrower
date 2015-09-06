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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ArmorType;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.DeathReasonPropertyUtils;

public class AttackUtils {

	public static void attack(DeadlyAction action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		meleeAttack(action, performer, target, args, world, skillBonus, new HitPointsHandler() {
			
			@Override
			public int handleHitPoints(WorldObject performer, WorldObject target, ManagedOperation action, int hitPoints) {
				if (hitPoints <= 0) {
					hitPoints = 0;
					DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, (DeadlyAction) action);
				}
				return hitPoints;
			}
		});
	}
	
	private static interface HitPointsHandler {
		public int handleHitPoints(WorldObject performer, WorldObject target, ManagedOperation action, int hitPoints);
	}
	
	private static void meleeAttack(ManagedOperation action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus, HitPointsHandler hitPointsHandler) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		if (target.getProperty(Constants.DAMAGE_RESIST) == null) { throw new IllegalStateException("DamageResist is null in " + target); }
		float targetDamageResist = (float) target.getProperty(Constants.DAMAGE_RESIST);
		
		int performerDamage = performer.getProperty(Constants.DAMAGE);
		float performerEnergy = (float) performer.getProperty(Constants.ENERGY);
		
		int damage = (int) (performerDamage * skillBonus * (performerEnergy / 1000) * ((1000 - targetDamageResist) / 1000));
		damage = changeForSize(damage, performer, target);
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " attacks " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		targetHP = hitPointsHandler.handleHitPoints(performer, target, action, targetHP);
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		useArmorSkill(target);
		
		world.logAction(action, performer, target, args, message);
	}
	
	private static int changeForSize(int damage, WorldObject performer, WorldObject target) {
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENLARGED_CONDITION)) {
			damage *= 2;
		} else if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.REDUCED_CONDITION)) {
			damage /= 2;
		} else if (targetHasCondition(target, Condition.ENLARGED_CONDITION)) {
			damage /= 2;
		} else if (targetHasCondition(target, Condition.REDUCED_CONDITION)) {
			damage *= 2;
		}
		return damage;
	}
	
	private static boolean targetHasCondition(WorldObject target, Condition condition) {
		Conditions targetConditions = target.getProperty(Constants.CONDITIONS);
		if (targetConditions != null) {
			return targetConditions.hasCondition(condition);
		} else {
			return false;
		}
	}

	private static void useArmorSkill(WorldObject target) {
		List<WorldObject> targetEquipmentList = new ArrayList<>();
		targetEquipmentList.add(target.getProperty(Constants.HEAD_EQUIPMENT));
		targetEquipmentList.add(target.getProperty(Constants.TORSO_EQUIPMENT));
		targetEquipmentList.add(target.getProperty(Constants.ARMS_EQUIPMENT));
		targetEquipmentList.add(target.getProperty(Constants.LEGS_EQUIPMENT));
		targetEquipmentList.add(target.getProperty(Constants.FEET_EQUIPMENT));
		
		boolean targetHasLightArmor = targetEquipmentList.stream().filter(w -> w != null && w.getProperty(Constants.ARMOR_TYPE) == ArmorType.LIGHT).collect(Collectors.toList()).size() > 0;
		boolean targetHasHeavyArmor = targetEquipmentList.stream().filter(w -> w != null && w.getProperty(Constants.ARMOR_TYPE) == ArmorType.HEAVY).collect(Collectors.toList()).size() > 0;
		
		if (targetHasLightArmor && targetHasHeavyArmor) {
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL);
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL);
		} else if(targetHasLightArmor) {
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL);
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL);
		} else if (targetHasHeavyArmor) {
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL);
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL);
		}
	}

	public static void biteAttack(DeadlyAction action, WorldObject performer, WorldObject target, int[] args, World world) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		
		int performerDamage = 10;
		float performerEnergy = (float) performer.getProperty(Constants.ENERGY);
		
		int damage = (int) (performerDamage * (performerEnergy / 1000));
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " bites " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		if (targetHP <= 0) {
			targetHP = 0;
			DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, action);
		}
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		world.logAction(action, performer, target, args, message);
	}
	
	public static void magicAttack(int performerDamage, DeadlyAction action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		
		int damage = (int) (performerDamage * skillBonus);
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " attacks " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		if (targetHP <= 0) {
			targetHP = 0;
			DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, action);
		}
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		world.logAction(action, performer, target, args, message);
	}
	
	public static int distanceWithFreeLeftHand(WorldObject performer, WorldObject target, int range) {
		WorldObject attackWeapon = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		if (attackWeapon == null) {
			return getDistanceInRange(performer, target, range);
		} else {
			return 1;
		}
	}
	
	public static SkillProperty determineSkill(WorldObject performer) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		WorldObject rightHandEquipment = performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		
		if ((leftHandEquipment == null) && (rightHandEquipment == null)) {
			return Constants.HAND_TO_HAND_SKILL;
		} else if ((leftHandEquipment != null) && (rightHandEquipment != null) && (leftHandEquipment == rightHandEquipment)) {
			return Constants.TWO_HANDED_SKILL;
		} else {
			return Constants.ONE_HANDED_SKILL;
		}
	}

	public static void nonLethalAttack(ManagedOperation action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		meleeAttack(action, performer, target, args, world, skillBonus, new HitPointsHandler() {
			
			@Override
			public int handleHitPoints(WorldObject performer, WorldObject target, ManagedOperation action, int hitPoints) {
				if (hitPoints <= 0) {
					hitPoints = 1;
					target.getProperty(Constants.CONDITIONS).addCondition(Condition.UNCONSCIOUS_CONDITION, 50, world);
				}
				return hitPoints;
			}
		});
		
	}

	public static int distanceWithLeftHandByProperty(WorldObject performer, WorldObject target, IntProperty intProperty, int range) {
		WorldObject attackWeapon = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		if (attackWeapon != null) {
			if (attackWeapon.hasProperty(intProperty)) {
				return getDistanceInRange(performer, target, range);
			}
		}
		return 1;
	}

	private static int getDistanceInRange(WorldObject performer, WorldObject target, int range) {
		int distance = Reach.distance(performer, target);
		if (distance <= range) {
			return 0;
		} else {
			return distance - range;
		}
	}
}
