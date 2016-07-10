/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.history;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class UTestHistoryItem {
	
	@Test
	public void testSearchAnyPerformer() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		HistoryItem historyItem = createHistoryItem(performer, target);
		assertEquals(true, historyItem.isEqualUsingFacade(1, 2, Args.EMPTY, Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testSearchAnyPerformerWithFacade() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.FACADE, TestUtils.createWorldObject(3, "facade"));
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		HistoryItem historyItem = createHistoryItem(performer, target);
		assertEquals(false, historyItem.isEqualUsingFacade(1, 2, Args.EMPTY, Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testGetThirdPersonDescription() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, "performer");
		world.addWorldObject(performer);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		world.addWorldObject(target);
		HistoryItem historyItem = createHistoryItem(performer, target);
		
		assertEquals("performer was attacking target", historyItem.getThirdPersonDescription(world));
	}

	private HistoryItem createHistoryItem(WorldObject performer, WorldObject target) {
		HistoryWorldObjects historyWorldObjects = new HistoryWorldObjects();
		historyWorldObjects.add(performer);
		historyWorldObjects.add(target);
		return new HistoryItem(-1, new OperationInfo(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn(), null, historyWorldObjects);
	}
}
