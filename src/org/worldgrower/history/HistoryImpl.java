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
import org.worldgrower.actions.Actions;

public class HistoryImpl implements History, Serializable {

	private final List<HistoryItem> historyItems = new ArrayList<>();
	private int currentHistoryId = 0;
	
	private final Map<Integer, HistoryItemsForTarget> historyItemsByPerformer = new HashMap<>();
	private final Map<ManagedOperation, List<HistoryItem>> historyItemsByOperations = new HashMap<>();
	
	private final Map<Integer, HistoryItem> lastPerformedActionMap = new HashMap<>();
	
	@Override
	public HistoryItem actionPerformed(OperationInfo operationInfo, Turn turn) {
		HistoryItem historyItem = new HistoryItem(currentHistoryId, operationInfo.copy(), turn);
		
		Integer performerId = historyItem.getOperationInfo().getPerformer().getProperty(Constants.ID);
		addHistoryItem(historyItem);
		
		lastPerformedActionMap.put(performerId, historyItem);
		
		currentHistoryId++;
		return historyItem;
	}

	private void addHistoryItem(HistoryItem historyItem) {
		ManagedOperation action = historyItem.getOperationInfo().getManagedOperation();
		if (action != Actions.MOVE_ACTION) {
			historyItems.add(historyItem);
			
			Integer performerId = historyItem.getOperationInfo().getPerformer().getProperty(Constants.ID);
			HistoryItemsForTarget historyItemsForTarget = historyItemsByPerformer.get(performerId);
			if (historyItemsForTarget == null) {
				historyItemsForTarget = new HistoryItemsForTarget();
				historyItemsByPerformer.put(performerId, historyItemsForTarget);
			}
			historyItemsForTarget.addHistoryItem(historyItem);
			
			List<HistoryItem> historyItemsByOperationsList = historyItemsByOperations.get(action);
			if (historyItemsByOperationsList == null) {
				historyItemsByOperationsList = new ArrayList<>();
				historyItemsByOperations.put(action, historyItemsByOperationsList);
			}
			historyItemsByOperationsList.add(historyItem);
		}
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
		
		Integer performerId = performer.getProperty(Constants.ID);
		HistoryItemsForTarget historyItemsForTarget = historyItemsByPerformer.get(performerId);
		if (historyItemsForTarget != null) {
			List<HistoryItem> foundItems = historyItemsForTarget.findHistoryItems(performer, target, args, managedOperation);
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
		return lastPerformedActionMap.get(worldObject.getProperty(Constants.ID));
	}

	@Override
	public List<HistoryItem> findHistoryItems(ManagedOperation managedOperationToFind) {
		List<HistoryItem> foundItems = historyItemsByOperations.get(managedOperationToFind);
		if (foundItems != null) {
			return foundItems;
		} else {
			return new ArrayList<>();
		}
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
	
	private static class HistoryItemsForTarget implements Serializable {
		private final Map<Integer, List<HistoryItem>> historyItemsByTarget = new HashMap<>();
		
		public void addHistoryItem(HistoryItem historyItem) {
				
			Integer targetId = historyItem.getOperationInfo().getTarget().getProperty(Constants.ID);
			List<HistoryItem> historyItemsByTargetList = historyItemsByTarget.get(targetId);
			if (historyItemsByTargetList == null) {
				historyItemsByTargetList = new ArrayList<>();
				historyItemsByTarget.put(targetId, historyItemsByTargetList);
			}
			historyItemsByTargetList.add(historyItem);
		}
		
		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation) {
			OperationInfo searchOperationInfo = new OperationInfo(performer, target, args, managedOperation);
			
			List<HistoryItem> historyItemsByTargetList = getHistoryItemsByTargetList(target);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.stream().filter(h -> h.getOperationInfo().isEqual(searchOperationInfo)).collect(Collectors.toList());
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, ManagedOperation managedOperation) {
			List<HistoryItem> historyItemsByTargetList = getHistoryItemsByTargetList(target);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.stream().filter(h -> h.getOperationInfo().matches(performer, target, managedOperation)).collect(Collectors.toList());
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		private List<HistoryItem> getHistoryItemsByTargetList(WorldObject target) {
			Integer targetId = target.getProperty(Constants.ID);
			List<HistoryItem> historyItemsByTargetList = historyItemsByTarget.get(targetId);
			return historyItemsByTargetList;
		}
	}

	@Override
	public List<HistoryItem> findHistoryItems(WorldObject performer, ManagedOperation managedOperation) {
		List<HistoryItem> historyItems = findHistoryItems(managedOperation);
		historyItems = historyItems.stream().filter(w -> w.getOperationInfo().getPerformer().equals(performer)).collect(Collectors.toList());
		return historyItems;
	}

	@Override
	public List<HistoryItem> findHistoryItems(WorldObject performer,WorldObject target, ManagedOperation managedOperation) {
		Integer performerId = performer.getProperty(Constants.ID);
		HistoryItemsForTarget historyItemsForTarget = historyItemsByPerformer.get(performerId);
		if (historyItemsForTarget != null) {
			List<HistoryItem> foundItems = historyItemsForTarget.findHistoryItems(performer, target, managedOperation);
			return foundItems;			
		} else {
			return new ArrayList<>();
		}		
	}
}
