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
import java.util.Arrays;
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
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.DeathReasonPropertyUtils;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;

public class AttackUtils {

	public static void attack(DeadlyAction action, WorldObject performer, WorldObject target, int[] args, World world, double skillBonus) {
		meleeAttack(action, performer, target, args, world, skillBonus, new HitPointsHandler() {
			
			@Override
			public int handleHitPoints(WorldObject performer, WorldObject target, ManagedOperation action, int hitPoints) {
				if (hitPoints <= 0) {
					hitPoints = 0;
					DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, (DeadlyAction) action, world);
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
		
		int damage = (int) (performerDamage * skillBonus * (performerEnergy / 1000) * ((100 - targetDamageResist) / 100));
		damage = changeForSize(damage, performer, target);
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " attacks " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		targetHP = hitPointsHandler.handleHitPoints(performer, target, action, targetHP);
		target.setProperty(Constants.HIT_POINTS, targetHP);	
		
		poisonTarget(performer, target, world);
		decreaseWeaponHealth(performer, damage);
		armorIsUsed(target, damage, world.getWorldStateChangedListeners());
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
		
		world.logAction(action, performer, target, args, message);
	}
	
	public static void decreaseWeaponHealth(WorldObject performer, int damage) {
		damageEquipment(performer, Constants.LEFT_HAND_EQUIPMENT, damage);
		damageEquipment(performer, Constants.RIGHT_HAND_EQUIPMENT, damage);
	}
	
	static void poisonTarget(WorldObject performer, WorldObject target, World world) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		WorldObject rightHandEquipment = performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		boolean leftHandEquipmentIsPoisoned = leftHandEquipment != null && leftHandEquipment.getProperty(Constants.POISON_DAMAGE) != null;
		boolean rightHandEquipmentIsPoisoned = rightHandEquipment != null && rightHandEquipment.getProperty(Constants.POISON_DAMAGE) != null;
		if (leftHandEquipmentIsPoisoned || rightHandEquipmentIsPoisoned) {
			Conditions.add(target, Condition.POISONED_CONDITION, 8, world);
			
			if (leftHandEquipment != null) {
				leftHandEquipment.removeProperty(Constants.POISON_DAMAGE);
			}
			if (rightHandEquipment != null) {
				rightHandEquipment.removeProperty(Constants.POISON_DAMAGE);
			}
		}
	}
	
	static void damageEquipment(WorldObject worldObject, UnCheckedProperty<WorldObject> equipmentProperty, int damage) {
		WorldObject equipment = worldObject.getProperty(equipmentProperty);
		if (equipment != null) {
			equipment.increment(Constants.EQUIPMENT_HEALTH, -damage);
		}
	}

	static int changeForSize(int damage, WorldObject performer, WorldObject target) {
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENLARGED_CONDITION)) {
			damage *= 2;
		} else if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.REDUCED_CONDITION)) {
			damage /= 2;
		}
		if (targetHasCondition(target, Condition.ENLARGED_CONDITION)) {
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

	private static void armorIsUsed(WorldObject target, int damage, WorldStateChangedListeners worldStateChangedListeners) {
		decreaseArmorHealth(target, damage);
		useArmorSkill(target, worldStateChangedListeners);
	}
	
	public static void decreaseArmorHealth(WorldObject target, int damage) {
		for(UnCheckedProperty<WorldObject> equipmentProperty : getEquipmentProperties()) {
			damageEquipment(target, equipmentProperty, damage);
		}
	}

	private static void useArmorSkill(WorldObject target, WorldStateChangedListeners worldStateChangedListeners) {
		List<WorldObject> targetEquipmentList = getEquipmentList(target);
		
		boolean targetHasLightArmor = targetEquipmentList.stream().filter(w -> w != null && w.getProperty(Constants.ARMOR_TYPE) == ArmorType.LIGHT).collect(Collectors.toList()).size() > 0;
		boolean targetHasHeavyArmor = targetEquipmentList.stream().filter(w -> w != null && w.getProperty(Constants.ARMOR_TYPE) == ArmorType.HEAVY).collect(Collectors.toList()).size() > 0;
		
		if (targetHasLightArmor && targetHasHeavyArmor) {
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL, worldStateChangedListeners);
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL, worldStateChangedListeners);
		} else if(targetHasLightArmor) {
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL, worldStateChangedListeners);
			SkillUtils.useSkill(target, Constants.LIGHT_ARMOR_SKILL, worldStateChangedListeners);
		} else if (targetHasHeavyArmor) {
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL, worldStateChangedListeners);
			SkillUtils.useSkill(target, Constants.HEAVY_ARMOR_SKILL, worldStateChangedListeners);
		}
	}

	private static List<WorldObject> getEquipmentList(WorldObject target) {
		List<WorldObject> targetEquipmentList = new ArrayList<>();
		for(UnCheckedProperty<WorldObject> equipmentProperty : getEquipmentProperties()) {
			targetEquipmentList.add(target.getProperty(equipmentProperty));
		}
		return targetEquipmentList;
	}
	
	private static List<UnCheckedProperty<WorldObject>> getEquipmentProperties() {
		return Arrays.asList(
				Constants.HEAD_EQUIPMENT, 
				Constants.TORSO_EQUIPMENT, 
				Constants.ARMS_EQUIPMENT, 
				Constants.LEGS_EQUIPMENT, 
				Constants.FEET_EQUIPMENT
		);
	}

	public static void biteAttack(DeadlyAction action, WorldObject performer, WorldObject target, int[] args, World world) {
		int targetHP = target.getProperty(Constants.HIT_POINTS);
		
		int performerDamage = 10 * Item.COMBAT_MULTIPLIER;
		float performerEnergy = (float) performer.getProperty(Constants.ENERGY);
		
		int damage = (int) (performerDamage * (performerEnergy / 1000));
		targetHP = targetHP - damage;
		String message = performer.getProperty(Constants.NAME) + " bites " + target.getProperty(Constants.NAME) + ": " + damage + " damage";
		
		if (targetHP <= 0) {
			targetHP = 0;
			DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, action, world);
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
			DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, action, world);
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
					Conditions.add(target, Condition.UNCONSCIOUS_CONDITION, 50, world);
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
