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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class UTestConditions {

	@Test
	public void testaddCondition() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.COCOONED_CONDITION, 8, world);
		
		assertEquals(true, conditions.hasCondition(Condition.COCOONED_CONDITION));
	}
	
	@Test
	public void testOnTurn() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.COCOONED_CONDITION, 2, world);
		
		conditions.onTurn(null, world, null);
		assertEquals(true, conditions.hasCondition(Condition.COCOONED_CONDITION));
		
		conditions.onTurn(null, world, null);
		assertEquals(false, conditions.hasCondition(Condition.COCOONED_CONDITION));
	}
	
	@Test
	public void testCanTakeAction() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.COCOONED_CONDITION, 2, world);
		
		assertEquals(false, conditions.canTakeAction());
	}
	
	@Test
	public void testCanMove() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.COCOONED_CONDITION, 2, world);
		
		assertEquals(false, conditions.canMove());
	}
	
	@Test
	public void testHasDiseaseCondition() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.VAMPIRE_BITE_CONDITION, 2, world);
		
		assertEquals(true, conditions.hasDiseaseCondition());
	}
	
	@Test
	public void testGetDiseaseConditions() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.COCOONED_CONDITION, 2, world);
		conditions.addCondition(Condition.VAMPIRE_BITE_CONDITION, 2, world);
		
		assertEquals(Arrays.asList(Condition.VAMPIRE_BITE_CONDITION), conditions.getDiseaseConditions());
	}
	
	@Test
	public void testRemoveAllDiseases() {
		Conditions conditions = new Conditions();
		World world = new WorldImpl(0, 0, null, null);
		conditions.addCondition(Condition.VAMPIRE_BITE_CONDITION, 2, world);
	
		assertEquals(true, conditions.hasDiseaseCondition());
		
		conditions.removeAllDiseases();
		assertEquals(false, conditions.hasDiseaseCondition());
	}
	
	@Test
	public void testPerform() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "Test");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "Test");
		
		Conditions targetConditions = target.getProperty(Constants.CONDITIONS);
		targetConditions.addCondition(Condition.SLEEP_CONDITION, 2, world);
		
		targetConditions.perform(performer, target, null, Actions.MELEE_ATTACK_ACTION, world);
		assertEquals(false, targetConditions.hasCondition(Condition.SLEEP_CONDITION));
	}
}
