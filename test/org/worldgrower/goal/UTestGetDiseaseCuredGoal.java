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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.Item;

public class UTestGetDiseaseCuredGoal {

	private GetDiseaseCuredGoal goal = Goals.GET_DISEASE_CURED_GOAL;
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateGoalPerformerKnowsCureDiseaseSpell() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_DISEASE_ACTION);
		
		assertEquals(Actions.CURE_DISEASE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(performer, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalPerformerKnowsCureDiseaseSpellNotEnoughEnergy() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_DISEASE_ACTION);
		performer.setProperty(Constants.ENERGY, 0);
		
		assertEquals(Actions.REST_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(performer, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalTargetKnowsCureDiseaseSpell() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		Conditions.add(performer, Condition.ATAXIA_CONDITION, 8, world);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_DISEASE_ACTION);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.CURE_DISEASE_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalDrinkCureDiseasePotion() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		Conditions.add(performer, Condition.ATAXIA_CONDITION, 8, world);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.CURE_DISEASE_POTION.generate(1f));
		
		assertEquals(Actions.DRINK_FROM_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(performer, goal.calculateGoal(performer, world).getTarget());
		assertEquals(0, goal.calculateGoal(performer, world).getArgs()[0]);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		Conditions.add(performer, Condition.VAMPIRE_BITE_CONDITION, 8, world);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(commonerId);
		return performer;
	}
}