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

import java.util.List;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.FacadeUtils;

public class UTestHistoryImpl {

	private History history;
	private WorldObject performer = TestUtils.createWorldObject(6, "Test1");
	private WorldObject target = TestUtils.createWorldObject(7, "Test2");
	
	public UTestHistoryImpl() {
		history = new HistoryImpl();
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
	}
	
	@Test
	public void testfindHistoryItem() {
		HistoryItem historyItem = history.findHistoryItem(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
		assertEquals(true, historyItem != null);
		assertEquals(6, historyItem.getPerformerId());
	}
	
	@Test
	public void testFindHistoryItems() {
		List<HistoryItem> historyItems = history.findHistoryItems(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
		assertEquals(1, historyItems.size());
		assertEquals(6, historyItems.get(0).getPerformerId());
	}
	
	@Test
	public void testFindHistoryItemsNoResult() {
		List<HistoryItem> historyItems = history.findHistoryItems(performer, performer, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
		assertEquals(0, historyItems.size());
	}
	
	@Test
	public void testfindHistoryItemsByManagedOperation() {
		List<HistoryItem> historyItems = history.findHistoryItems(Actions.MELEE_ATTACK_ACTION);
		assertEquals(1, historyItems.size());
		assertEquals(6, historyItems.get(0).getPerformerId());
		
		assertEquals(0, history.findHistoryItems(Actions.MINE_ORE_ACTION).size());
	}
	
	@Test
	public void testGetLastPerformedOperation() {
		History history = new HistoryImpl();
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		assertEquals(Actions.MELEE_ATTACK_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
		
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.TALK_ACTION), new Turn());
		assertEquals(Actions.TALK_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
	}

	@Test
	public void testGetLastPerformedOperationOverwrite() {
		History history = new HistoryImpl();
		
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.TALK_ACTION), new Turn());
		assertEquals(Actions.TALK_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
		
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.FIRE_BOLT_ATTACK_ACTION), new Turn());
		assertEquals(Actions.FIRE_BOLT_ATTACK_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
		
		history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.SILENCE_MAGIC_ACTION), new Turn());
		assertEquals(Actions.SILENCE_MAGIC_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
		
	}
	
	@Test
	public void testGetHistoryItem() {
		assertEquals(Actions.MELEE_ATTACK_ACTION, history.getHistoryItem(0).getManagedOperation());
	}
	
	@Test
	public void testGetHistoryItemWithSkippedActions() {
		History history = new HistoryImpl();
		HistoryItem historyItem1 = history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION), new Turn());
		HistoryItem historyItem2 = history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.MOVE_ACTION), new Turn());
		
		assertEquals(Actions.MOVE_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
		assertEquals(0, historyItem1.getHistoryId());
		assertEquals(null, historyItem2);
		assertEquals(Actions.MELEE_ATTACK_ACTION, history.getHistoryItem(0).getManagedOperation());
		
		HistoryItem historyItem3 = history.actionPerformed(new OperationInfo(performer, target, Args.EMPTY, Actions.FIRE_BOLT_ATTACK_ACTION), new Turn());
		assertEquals(1, historyItem3.getHistoryId());
		assertEquals(Actions.FIRE_BOLT_ATTACK_ACTION, history.getHistoryItem(1).getManagedOperation());
		assertEquals(Actions.FIRE_BOLT_ATTACK_ACTION, history.getLastPerformedOperation(performer).getManagedOperation());
	}
	
	@Test
	public void testFindHistoryItemsForAnyPerformer() {
		List<HistoryItem> historyItems = history.findHistoryItemsForAnyPerformer(performer, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
		assertEquals(1, historyItems.size());
		assertEquals(6, historyItems.get(0).getPerformerId());

	}
	
	@Test
	public void testFindHistoryItemsForAnyPerformerFacade() {
		WorldObject person = TestUtils.createWorldObject(8, "Test3");
		person.setProperty(Constants.FACADE, performer);
		WorldObject facade = FacadeUtils.createFacadeForSelf(person);
		List<HistoryItem> historyItems = history.findHistoryItemsForAnyPerformer(facade, target, Args.EMPTY, Actions.MELEE_ATTACK_ACTION);
		assertEquals(1, historyItems.size());
		assertEquals(6, historyItems.get(0).getPerformerId());

	}
}
