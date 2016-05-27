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

public class UTestFearCondition {

	private final FearCondition condition = Condition.FEAR_CONDITION;
	
	@Test
	public void testOnTurn() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		performer.setProperty(Constants.X, 1);
		performer.setProperty(Constants.Y, 1);
		WorldObject fearCaster = TestUtils.createIntelligentWorldObject(3, "fearCaster");
		fearCaster.setProperty(Constants.X, 0);
		fearCaster.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.FEAR_CASTER_ID, fearCaster.getProperty(Constants.ID));
		world.addWorldObject(fearCaster);
		
		condition.onTurn(performer, world, 100, new WorldStateChangedListeners());
		
		assertEquals(0, performer.getProperty(Constants.X).intValue());
		assertEquals(2, performer.getProperty(Constants.Y).intValue());
	}
}
