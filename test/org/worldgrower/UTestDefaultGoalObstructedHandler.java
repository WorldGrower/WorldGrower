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

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.generator.ItemGenerator;

public class UTestDefaultGoalObstructedHandler {

	@Test
	public void testPerformerAttacked() {
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.MELEE_ATTACK_ACTION));
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.FIRE_BOLT_ATTACK_ACTION));
		assertEquals(false, DefaultGoalObstructedHandler.performerAttacked(Actions.CURE_POISON_ACTION));
	}
	
	@Test
	public void testAreBrawling() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, 2);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, 1);
		
		assertEquals(true, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testAreBrawlingNoBrawlOpponentId() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, null);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, null);
		
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testAreBrawlingItemEquiped() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, 2);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, 1);
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, ItemGenerator.getIronClaymore(1f));
		
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
	}
}
