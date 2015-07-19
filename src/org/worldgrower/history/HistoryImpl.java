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
package org.worldgrower.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.WorldObject;

public class HistoryImpl implements History, Serializable {

	private final List<HistoryItem> historyItems = new ArrayList<>();
	private int currentHistoryId = 0;
	
	private final Map<Integer, List<HistoryItem>> historyItemsByPerformer = new HashMap<>();
	
	@Override
	public HistoryItem actionPerformed(OperationInfo operationInfo, Turn turn) {
		HistoryItem historyItem = new HistoryItem(currentHistoryId, operationInfo.copy(), turn);
		historyItems.add(historyItem);
		
		Integer performerId = historyItem.getOperationInfo().getPerformer().getProperty(Constants.ID);
		List<HistoryItem> historyItemsByPerformerList = historyItemsByPerformer.get(performerId);
		if (historyItemsByPerformerList == null) {
			historyItemsByPerformerList = new ArrayList<>();
			historyItemsByPerformer.put(performerId, historyItemsByPerformerList);
		}
		historyItemsByPerformerList.add(historyItem);
		
		currentHistoryId++;
		return historyItem;
	}
	
	@Override
	public HistoryItem findHistoryItem(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
		List<HistoryItem> foundItems = findHistoryItems(performer, target, args, managedOperation);
		if (foundItems.size() > 0) {
			return foundItems.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
		OperationInfo searchOperationInfo = new OperationInfo(performer, target, args, managedOperation);
		
		Integer performerId = performer.getProperty(Constants.ID);
		List<HistoryItem> historyItemsByPerformerList = historyItemsByPerformer.get(performerId);
		if (historyItemsByPerformerList != null) {
			List<HistoryItem> foundItems = historyItemsByPerformerList.stream().filter(h -> h.getOperationInfo().isEqual(searchOperationInfo)).collect(Collectors.toList());
			return foundItems;			
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<HistoryItem> findHistoryItemsForAnyPerformer(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
		OperationInfo searchOperationInfo = new OperationInfo(performer, target, args, managedOperation);
		List<HistoryItem> foundItems = historyItems.stream().filter(h -> h.getOperationInfo().searchAnyPerformer(searchOperationInfo)).collect(Collectors.toList());
		return foundItems;
	}
	
	@Override
	public HistoryItem getLastPerformedOperation(WorldObject worldObject) {
		for(int i=historyItems.size()-1; i>=0; i--) {
			if (historyItems.get(i).getOperationInfo().isPerformer(worldObject)) {
				return historyItems.get(i);
			}
		}
		return null;
	}

	@Override
	public List<HistoryItem> findHistoryItems(ManagedOperation managedOperationToFind) {
		List<HistoryItem> foundItems = historyItems.stream().filter(h -> h.getOperationInfo().evaluate((performer, target, args, managedOperation) -> (managedOperation == managedOperationToFind))).collect(Collectors.toList());
		return foundItems;
	}

	@Override
	public HistoryItem getHistoryItem(int historyItemId) {
		for(HistoryItem historyItem : historyItems) {
			if (historyItem.getHistoryId() == historyItemId) {
				return historyItem;
			}
		}
		
		throw new IllegalStateException("historyItemId " + historyItemId + " not found");
	}
}
