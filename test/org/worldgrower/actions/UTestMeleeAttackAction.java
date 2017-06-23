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
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestMeleeAttackAction {

	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals(26 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		Actions.MELEE_ATTACK_ACTION.execute(performer, target, Args.EMPTY, world);
		
		assertEquals(24 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testExecuteMinotaur() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		int minotaurId = CreatureGenerator.generateMinotaur(0, 0, world);
		WorldObject performer = world.findWorldObjectById(minotaurId);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		for(int i=0; i<100; i++) {
			Actions.MELEE_ATTACK_ACTION.execute(performer, target, Args.EMPTY, world);
		}
		
		assertEquals(0 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testExecuteRat() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		CreatureGenerator creatureGenerator = new CreatureGenerator(organization);
		int ratId = creatureGenerator.generateRat(0, 0, world);
		WorldObject performer = world.findWorldObjectById(ratId);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals(19 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
		for(int i=0; i<100; i++) {
			Actions.MELEE_ATTACK_ACTION.execute(performer, target, Args.EMPTY, world);
		}
		
		assertEquals(0 * Item.COMBAT_MULTIPLIER, target.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals(true, Actions.MELEE_ATTACK_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.MELEE_ATTACK_ACTION.isValidTarget(performer, TestUtils.createWorldObject(7, "target"), world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals(0, Actions.MELEE_ATTACK_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testGetDeathDescriptionUnarmed() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		assertEquals("pummeled to death", Actions.MELEE_ATTACK_ACTION.getDeathDescription(performer, target));
	}
	
	@Test
	public void testGetDeathDescriptionSword() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		
		assertEquals("slashed to death", Actions.MELEE_ATTACK_ACTION.getDeathDescription(performer, target));
	}
	
	@Test
	public void testGetDeathDescriptionMace() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_MACE.generate(1f));
		
		assertEquals("bludgeoned to death", Actions.MELEE_ATTACK_ACTION.getDeathDescription(performer, target));
	}
	
	@Test
	public void testGetDeathDescriptionKatar() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		WorldObject target = createPerformer(world, organization);
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_KATAR.generate(1f));
		
		assertEquals("having internal organs pierced to death", Actions.MELEE_ATTACK_ACTION.getDeathDescription(performer, target));
	}
	
	private WorldObject createPerformer(World world, WorldObject organization) {
		int performerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(performerId);
		return performer;
	}
}