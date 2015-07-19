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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.history.HistoryImpl;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class UTestHistoryImpl {

	private History history;
	private WorldObject performer = TestUtils.createWorldObject(6, "Test1");
	private WorldObject target = TestUtils.createWorldObject(7, "Test2");
	
	public UTestHistoryImpl() {
		history = new HistoryImpl();
		history.actionPerformed(new OperationInfo(performer, target, new int[0], Actions.CUT_WOOD_ACTION), new Turn());
	}
	
	@Test
	public void testfindHistoryItem() {
		HistoryItem historyItem = history.findHistoryItem(performer, target, new int[0], Actions.CUT_WOOD_ACTION);
		assertEquals(true, historyItem != null);
		assertEquals(6, historyItem.getOperationInfo().getPerformer().getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindHistoryItems() {
		List<HistoryItem> historyItems = history.findHistoryItems(performer, target, new int[0], Actions.CUT_WOOD_ACTION);
		assertEquals(1, historyItems.size());
		assertEquals(6, historyItems.get(0).getOperationInfo().getPerformer().getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindHistoryItemsNoResult() {
		List<HistoryItem> historyItems = history.findHistoryItems(performer, performer, new int[0], Actions.CUT_WOOD_ACTION);
		assertEquals(0, historyItems.size());
	}
}
