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
import java.util.Collection;
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
	
	private final Map<Integer, HistoryItemsForTarget> historyItemsByPerformer = new HashMap<>();
	private final Map<ManagedOperation, List<HistoryItem>> historyItemsByOperations = new HashMap<>();
	
	private final Map<Integer, OperationInfo> lastPerformedOperationMap = new HashMap<>();
	private Object currentAdditionalValue = null;
	
	private final HistoryWorldObjects historyWorldObjects = new HistoryWorldObjects();
	
	@Override
	public HistoryItem actionPerformed(OperationInfo operationInfo, Turn turn) {
		HistoryItem historyItem = null;
		ManagedOperation action = operationInfo.getManagedOperation();
		boolean shouldLogAction = shouldLogAction(action);
		
		if (shouldLogAction) {
			historyItem = new HistoryItem(getNextHistoryId(), operationInfo, turn, currentAdditionalValue, historyWorldObjects);
			currentAdditionalValue = null;
			addHistoryItem(historyItem);
			updateHistoryWorldObjects(operationInfo);
		}
		
		addAsLastPerformedOperation(operationInfo);
		
		return historyItem;
	}

	private void updateHistoryWorldObjects(OperationInfo operationInfo) {
		historyWorldObjects.add(operationInfo.getPerformer());
		historyWorldObjects.add(operationInfo.getTarget());
	}

	private void addAsLastPerformedOperation(OperationInfo operationInfo) {
		Integer performerId = operationInfo.getPerformer().getProperty(Constants.ID);
		lastPerformedOperationMap.put(performerId, operationInfo);
	}

	private void addHistoryItem(HistoryItem historyItem) {
		ManagedOperation action = historyItem.getManagedOperation();

		historyItems.add(historyItem);
		
		Integer performerId = historyItem.getPerformerId();
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

	private boolean shouldLogAction(ManagedOperation action) {
		return action != Actions.MOVE_ACTION 
				&& action != Actions.REST_ACTION 
				&& action != Actions.SLEEP_ACTION
				&& action != Actions.CUT_WOOD_ACTION
				&& action != Actions.MINE_STONE_ACTION
				&& action != Actions.MINE_ORE_ACTION
				&& action != Actions.DO_NOTHING_ACTION;
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
		Integer performerId = performer.getProperty(Constants.ID);
		HistoryItemsForTarget historyItemsForTarget = historyItemsByPerformer.get(performerId);
		if (historyItemsForTarget != null) {
			List<HistoryItem> foundItems = historyItemsForTarget.findHistoryItemsForAnyPerformer(performer, target, args, managedOperation);
			return foundItems;			
		} else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<HistoryItem> findHistoryItemsForPerformer(WorldObject performer) {
		int performerId = performer.getProperty(Constants.ID).intValue();
		return historyItems.stream().filter(h -> h.getPerformerId() == performerId).collect(Collectors.toList());
	}
	
	@Override
	public OperationInfo getLastPerformedOperation(WorldObject worldObject) {
		return lastPerformedOperationMap.get(worldObject.getProperty(Constants.ID));
	}
	
	@Override
	public Collection<OperationInfo> getAllLastPerformedOperations() {
		return lastPerformedOperationMap.values();
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
		return historyItems.get(historyItemId);
	}
	
	private static class HistoryItemsForTarget implements Serializable {
		private final Map<Integer, HistoryItemsForAction> historyItemsByTarget = new HashMap<>();
		
		public void addHistoryItem(HistoryItem historyItem) {
				
			Integer targetId = historyItem.getTargetId();
			HistoryItemsForAction historyItemsByTargetList = historyItemsByTarget.get(targetId);
			if (historyItemsByTargetList == null) {
				historyItemsByTargetList = new HistoryItemsForAction();
				historyItemsByTarget.put(targetId, historyItemsByTargetList);
			}
			historyItemsByTargetList.addHistoryItem(historyItem);
		}
		
		public List<HistoryItem> findHistoryItemsForAnyPerformer(WorldObject performer, WorldObject target, int[] args, ManagedOperation action) {
			HistoryItemsForAction historyItemsByTargetList = getHistoryItemsByTargetList(target);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.findHistoryItemsForAnyPerformer(performer, target, args, action);
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, int[] args, ManagedOperation action) {
			HistoryItemsForAction historyItemsByTargetList = getHistoryItemsByTargetList(target);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.findHistoryItems(performer, target, args, action);
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, ManagedOperation action) {
			HistoryItemsForAction historyItemsByTargetList = getHistoryItemsByTargetList(target);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.findHistoryItems(performer, target, action);
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		private HistoryItemsForAction getHistoryItemsByTargetList(WorldObject target) {
			Integer targetId = target.getProperty(Constants.ID);
			HistoryItemsForAction historyItemsByTargetList = historyItemsByTarget.get(targetId);
			return historyItemsByTargetList;
		}
	}
	
	private static class HistoryItemsForAction implements Serializable {
		private final Map<ManagedOperation, List<HistoryItem>> historyItemsByAction = new HashMap<>();
		
		public void addHistoryItem(HistoryItem historyItem) {
				
			ManagedOperation action = historyItem.getManagedOperation();
			List<HistoryItem> historyItemsByActionList = historyItemsByAction.get(action);
			if (historyItemsByActionList == null) {
				historyItemsByActionList = new ArrayList<>();
				historyItemsByAction.put(action, historyItemsByActionList);
			}
			historyItemsByActionList.add(historyItem);
		}
		
		public List<HistoryItem> findHistoryItemsForAnyPerformer(WorldObject performer, WorldObject target, int[] args, ManagedOperation action) {
			int performerId = performer.getProperty(Constants.ID).intValue();
			int targetId = target.getProperty(Constants.ID).intValue();
			
			List<HistoryItem> historyItemsByTargetList = getHistoryItemsByActionList(action);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.stream().filter(h -> h.isEqualUsingFacade(performerId, targetId, args, action)).collect(Collectors.toList());
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, int[] args, ManagedOperation action) {
			HistoryItem searchHistoryItem = new HistoryItem(-1, new OperationInfo(performer, target, args, action), new Turn(), null, null);
			
			List<HistoryItem> historyItemsByTargetList = getHistoryItemsByActionList(action);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.stream().filter(h -> h.isEqual(searchHistoryItem)).collect(Collectors.toList());
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, ManagedOperation action) {
			int performerId = performer.getProperty(Constants.ID).intValue();
			int targetId = target.getProperty(Constants.ID).intValue();
			List<HistoryItem> historyItemsByTargetList = getHistoryItemsByActionList(action);
			if (historyItemsByTargetList != null) {
				List<HistoryItem> foundItems = historyItemsByTargetList.stream().filter(h -> h.matches(performerId, targetId, action)).collect(Collectors.toList());
				return foundItems;			
			} else {
				return new ArrayList<>();
			}
		}

		private List<HistoryItem> getHistoryItemsByActionList(ManagedOperation action) {
			List<HistoryItem> historyItemsByTargetList = historyItemsByAction.get(action);
			return historyItemsByTargetList;
		}
	}

	@Override
	public List<HistoryItem> findHistoryItems(WorldObject performer, ManagedOperation managedOperation) {
		List<HistoryItem> historyItems = findHistoryItems(managedOperation);
		historyItems = historyItems.stream().filter(h -> h.getPerformerId() == performer.getProperty(Constants.ID)).collect(Collectors.toList());
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

	@Override
	public void setNextAdditionalValue(Object value) {
		this.currentAdditionalValue = value;
	}
	
	@Override
	public int getNextHistoryId() {
		return this.historyItems.size();
	}

	@Override
	public int size() {
		return historyItems.size();
	}
}
