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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.DamageType;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestAttackUtils {

	@Test
	public void testAttack() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.attack(Actions.MELEE_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1.0f, AttackType.MELEE);
		assertEquals(17 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testAttackDamageResist() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		performer.setProperty(Constants.DAMAGE, 6 * Item.COMBAT_MULTIPLIER);
		target.setProperty(Constants.DAMAGE_RESIST, 50);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.attack(Actions.MELEE_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1.0f, AttackType.MELEE);
		assertEquals(16 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testKill() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		target.setProperty(Constants.HIT_POINTS, 1);
		AttackUtils.attack(Actions.MELEE_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1.0f, AttackType.MELEE);
		assertEquals(0, target.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals("Test was pummeled to death", target.getProperty(Constants.DEATH_REASON));
	}
	
	@Test
	public void testDamageEquipment() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		AttackUtils.damageEquipment(performer, Constants.TORSO_EQUIPMENT, 5);
		assertEquals(null, performer.getProperty(Constants.TORSO_EQUIPMENT));
		
		performer.setProperty(Constants.TORSO_EQUIPMENT, Item.IRON_CUIRASS.generate(1f));
		AttackUtils.damageEquipment(performer, Constants.TORSO_EQUIPMENT, 5);
		assertEquals(995, performer.getProperty(Constants.TORSO_EQUIPMENT).getProperty(Constants.EQUIPMENT_HEALTH).intValue());
	}
	
	@Test
	public void testChangeForSize() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CONDITIONS, new Conditions());
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.CONDITIONS, new Conditions());
		
		assertEquals(5, AttackUtils.changeForSize(5, performer, target));
		
		Conditions.add(performer, Condition.ENLARGED_CONDITION, 8, world);
		assertEquals(10, AttackUtils.changeForSize(5, performer, target));
		
		Conditions.add(target, Condition.REDUCED_CONDITION, 8, world);
		assertEquals(20, AttackUtils.changeForSize(5, performer, target));
		
		target.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(target, Condition.ENLARGED_CONDITION, 8, world);
		assertEquals(5, AttackUtils.changeForSize(5, performer, target));
		
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(performer, Condition.REDUCED_CONDITION, 8, world);
		target.setProperty(Constants.CONDITIONS, new Conditions());
		assertEquals(2, AttackUtils.changeForSize(5, performer, target));
	}
	
	@Test
	public void testDistanceWithLeftHandByProperty() {
		WorldObject performer = TestUtils.createWorldObject(1, 1, 1, 1);
		WorldObject target = TestUtils.createWorldObject(2, 2, 1, 1);
		
		assertEquals(1, AttackUtils.distanceWithLeftHandByProperty(performer, target, Constants.FISHING_POLE_QUALITY, 5));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.FISHING_POLE.generate(1f));
		assertEquals(0, AttackUtils.distanceWithLeftHandByProperty(performer, target, Constants.FISHING_POLE_QUALITY, 5));
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		assertEquals(4, AttackUtils.distanceWithLeftHandByProperty(performer, target, Constants.FISHING_POLE_QUALITY, 5));
	}
	
	@Test
	public void testDistanceWithFreeLeftHand() {
		WorldObject performer = TestUtils.createWorldObject(1, 1, 1, 1);
		WorldObject target = TestUtils.createWorldObject(2, 2, 1, 1);
		
		assertEquals(0, AttackUtils.distanceWithFreeLeftHand(performer, target, 5));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		assertEquals(1, AttackUtils.distanceWithFreeLeftHand(performer, target, 5));
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		assertEquals(1, AttackUtils.distanceWithFreeLeftHand(performer, target, 5));
	}
	
	@Test
	public void testDetermineSkill() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		
		assertEquals(Constants.HAND_TO_HAND_SKILL, AttackUtils.determineSkill(performer));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		assertEquals(Constants.ONE_HANDED_SKILL, AttackUtils.determineSkill(performer));
		
		WorldObject ironGreatSword = Item.IRON_GREATSWORD.generate(1f);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, ironGreatSword);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, ironGreatSword);
		assertEquals(Constants.TWO_HANDED_SKILL, AttackUtils.determineSkill(performer));
	}
	
	@Test
	public void testMagicAttack() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.magicAttack(5 * Item.COMBAT_MULTIPLIER, Actions.FIRE_BOLT_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1f, DamageType.FIRE);
		assertEquals(14 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testBiteAttack() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.biteAttack(Actions.VAMPIRE_BITE_ACTION, performer, target, Args.EMPTY, world);
		assertEquals(9 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		
		AttackUtils.biteAttack(Actions.VAMPIRE_BITE_ACTION, performer, target, Args.EMPTY, world);
		AttackUtils.biteAttack(Actions.VAMPIRE_BITE_ACTION, performer, target, Args.EMPTY, world);
		AttackUtils.biteAttack(Actions.VAMPIRE_BITE_ACTION, performer, target, Args.EMPTY, world);
		
		assertEquals(0, target.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals("Test was drained of blood", target.getProperty(Constants.DEATH_REASON));
	}
	
	@Test
	public void testNonLethalAttack() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		
		target.setProperty(Constants.HIT_POINTS, 4 * Item.COMBAT_MULTIPLIER);
		assertEquals(4 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		AttackUtils.nonLethalAttack(Actions.NON_LETHAL_MELEE_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1f, AttackType.MELEE);
		assertEquals(2 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		
		AttackUtils.nonLethalAttack(Actions.NON_LETHAL_MELEE_ATTACK_ACTION, performer, target, Args.EMPTY, world, 1f, AttackType.MELEE);
		assertEquals(1, target.getProperty(Constants.HIT_POINTS).intValue());
		assertEquals(true, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION));
	}
	
	@Test
	public void testPoisonTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		WorldObject ironClaymore = Item.IRON_CLAYMORE.generate(1f);
		ironClaymore.setProperty(Constants.POISON_DAMAGE, 5);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, ironClaymore);
		
		AttackUtils.poisonTarget(performer, target, world);
		assertEquals(true, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
		assertEquals(null, ironClaymore.getProperty(Constants.POISON_DAMAGE));
	}
	
	@Test
	public void testPoisonTargetDualWield() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		WorldObject ironClaymore1 = Item.IRON_CLAYMORE.generate(1f);
		ironClaymore1.setProperty(Constants.POISON_DAMAGE, 5);
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, ironClaymore1);

		WorldObject ironClaymore2 = Item.IRON_CLAYMORE.generate(1f);
		ironClaymore2.setProperty(Constants.POISON_DAMAGE, 5);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, ironClaymore2);
		
		AttackUtils.poisonTarget(performer, target, world);
		assertEquals(true, target.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
		assertEquals(null, ironClaymore1.getProperty(Constants.POISON_DAMAGE));
		assertEquals(null, ironClaymore2.getProperty(Constants.POISON_DAMAGE));
	}
	
	@Test
	public void testChangeForDamageTypeFire() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(10, AttackUtils.changeForDamageType(10, DamageType.FIRE, performer, target));
		
		Conditions.add(target, Condition.PROTECTION_FROM_FIRE_CONDITION, 8, world);
		assertEquals(5, AttackUtils.changeForDamageType(10, DamageType.FIRE, performer, target));
	}
	
	@Test
	public void testChangeForDamageTypeIce() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(10, AttackUtils.changeForDamageType(10, DamageType.ICE, performer, target));
		
		Conditions.add(target, Condition.PROTECTION_FROM_ICE_CONDITION, 8, world);
		assertEquals(5, AttackUtils.changeForDamageType(10, DamageType.ICE, performer, target));
	}
	
	@Test
	public void testChangeForDamageTypeLightning() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createCommoner(world);
		WorldObject target = createCommoner(world);
		
		assertEquals(10, AttackUtils.changeForDamageType(10, DamageType.LIGHTNING, performer, target));
		
		Conditions.add(target, Condition.PROTECTION_FROM_LIGHTNING_CONDITION, 8, world);
		assertEquals(5, AttackUtils.changeForDamageType(10, DamageType.LIGHTNING, performer, target));
	}

	private WorldObject createCommoner(World world) {
		CommonerGenerator commonerGenerator = new MockCommonerGenerator();
		WorldObject villagersOrganization = GroupPropertyUtils.createVillagersOrganization(world);
		int performerId = commonerGenerator.generateCommoner(0, 0, world, villagersOrganization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(performerId);
		return performer;
	}
	
}
