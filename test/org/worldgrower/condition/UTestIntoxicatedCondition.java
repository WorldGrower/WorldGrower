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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;

public class UTestIntoxicatedCondition {

	private final IntoxicatedCondition condition = Condition.INTOXICATED_CONDITION;
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		performer.setProperty(Constants.ALCOHOL_LEVEL, 500);
		Conditions.add(performer, Condition.INTOXICATED_CONDITION, 8, world);
		
		condition.onTurn(performer, world, 100, new WorldStateChangedListeners());
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INTOXICATED_CONDITION));
		
		performer.setProperty(Constants.ALCOHOL_LEVEL, 0);
		condition.onTurn(performer, world, 100, new WorldStateChangedListeners());
		assertEquals(false, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INTOXICATED_CONDITION));
	}
}
