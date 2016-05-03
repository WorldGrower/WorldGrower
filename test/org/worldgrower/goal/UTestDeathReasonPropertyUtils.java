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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;

public class UTestDeathReasonPropertyUtils {

	@Test
	public void testDrowning() {
		World world = new WorldImpl(0, 0,  null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.NAME, "performer");
		
		DeathReasonPropertyUtils.targetDiesByDrowning(performer, world);
		assertEquals("performer was killed by drowning", performer.getProperty(Constants.DEATH_REASON));
	}
	
	@Test
	public void testCondition() {
		World world = new WorldImpl(0, 0,  null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.NAME, "performer");
		
		DeathReasonPropertyUtils.targetDiesByCondition(Condition.BURNING_CONDITION, performer, world);
		assertEquals("performer was burned to death", performer.getProperty(Constants.DEATH_REASON));
	}
	
	@Test
	public void testAction() {
		World world = new WorldImpl(0, 0,  null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.NAME, "target");
		
		DeathReasonPropertyUtils.targetDiesByPerformerAction(performer, target, Actions.MELEE_ATTACK_ACTION, world);
		assertEquals("target was pummeled to death", target.getProperty(Constants.DEATH_REASON));
	}
	
	@Test
	public void testGetAllDeathReasons() {
		assertEquals(13, DeathReasonPropertyUtils.getAllDeathReasons().size());
	}
}
