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
package org.worldgrower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.curse.Curse;

public class UTestWorldObjectImpl {

	@Test
	public void testGetProperty() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.FOOD, 500);
		properties.put(Constants.ID, 1);
		WorldObject worldObject = new WorldObjectImpl(properties);
		
		assertEquals(500, worldObject.getProperty(Constants.FOOD).intValue());
	}
	
	@Test
	public void testInvalidProperty() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.FOOD, -1);
		properties.put(Constants.ID, 1);
		try {
			new WorldObjectImpl(properties);
			fail("method should fail");
		} catch(IllegalStateException ex) {
			assertEquals("value -1 is lower than minValue 0 for food", ex.getMessage());
		}
	}
	
	@Test
	public void testIncrement() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.FOOD, 500);
		WorldObject worldObject = new WorldObjectImpl(properties);
		
		worldObject.increment(Constants.FOOD, 1000);
		assertEquals(1000, worldObject.getProperty(Constants.FOOD).intValue());
		
		worldObject.increment(Constants.FOOD, -2000);
		assertEquals(0, worldObject.getProperty(Constants.FOOD).intValue());
	}
	
	@Test
	public void testEquals() {
		WorldObject person1 = TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500);
		WorldObject person2 = TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500);
		
		assertEquals(false, person1.equals(person2));
		assertEquals(true, person1.equals(person1));
		assertEquals(false, person1.equals(new Object()));
	}
	
	@Test
	public void testHasIntelligence() {
		WorldObject person1 = TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500);
		WorldObject person2 = TestUtils.createWorldObject(0, 0, 1, 1);
		
		assertEquals(true, person1.hasIntelligence());
		assertEquals(false, person2.hasIntelligence());
		
		person1.setProperty(Constants.ILLUSION_CREATOR_ID, 5);
		assertEquals(false, person1.hasIntelligence());
	}
	
	@Test
	public void testCanWorldObjectPerformActionParalyzed() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		assertEquals(true, person.canWorldObjectPerformAction(Actions.MOVE_ACTION));
		
		Conditions.add(person, Condition.PARALYZED_CONDITION, 8, world);
		assertEquals(false, person.canWorldObjectPerformAction(Actions.MOVE_ACTION));
	}
	
	@Test
	public void testCanWorldObjectPerformActionUnconscious() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);

		Conditions.add(person, Condition.UNCONSCIOUS_CONDITION, 8, world);
		assertEquals(false, person.canWorldObjectPerformAction(Actions.MOVE_ACTION));
	}
	
	@Test
	public void testCanWorldObjectPerformActionMagicSpell() {
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(false, person.canWorldObjectPerformAction(Actions.FIRE_BOLT_ATTACK_ACTION));
		
		person.getProperty(Constants.KNOWN_SPELLS).add(Actions.FIRE_BOLT_ATTACK_ACTION);
		assertEquals(true, person.canWorldObjectPerformAction(Actions.FIRE_BOLT_ATTACK_ACTION));
	}
	
	@Test
	public void testCanWorldObjectPerformActionSilencedCondition() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWN_SPELLS, new ArrayList<>());
		person.getProperty(Constants.KNOWN_SPELLS).add(Actions.FIRE_BOLT_ATTACK_ACTION);
		assertEquals(true, person.canWorldObjectPerformAction(Actions.FIRE_BOLT_ATTACK_ACTION));
		
		Conditions.add(person, Condition.SILENCED_CONDITION, 8, world);
		assertEquals(false, person.canWorldObjectPerformAction(Actions.FIRE_BOLT_ATTACK_ACTION));
	}
	
	@Test
	public void testCanWorldObjectPerformActionCutWood() {
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWN_SPELLS, new ArrayList<>());
		assertEquals(true, person.canWorldObjectPerformAction(Actions.CUT_WOOD_ACTION));
	}
	
	@Test
	public void testCanWorldObjectPerformTalkWhileCursed() {
		WorldObject person = TestUtils.createIntelligentWorldObject(1, Constants.KNOWN_SPELLS, new ArrayList<>());
		
		person.setProperty(Constants.CURSE, Curse.WEREWOLF_CURSE);
		assertEquals(false, person.canWorldObjectPerformAction(Actions.TALK_ACTION));
		
		person.setProperty(Constants.CURSE, Curse.VAMPIRE_CURSE);
		assertEquals(true, person.canWorldObjectPerformAction(Actions.TALK_ACTION));
		
		person.setProperty(Constants.CURSE, null);
		assertEquals(true, person.canWorldObjectPerformAction(Actions.TALK_ACTION));
	}
}
