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

import java.util.Collection;
import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.WorldObject;

/**
 * History holds all ManagedOperations that were executed until now.
 * Its main purpose is to hold this information and provide fast ways to query it.
 */
public interface History {

	public HistoryItem actionPerformed(OperationInfo operationInfo, Turn turn);
	
	public HistoryItem findHistoryItem(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation);
	public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation);
	public List<HistoryItem> findHistoryItems(WorldObject performer, WorldObject target, ManagedOperation managedOperation);
	public List<HistoryItem> findHistoryItemsForAnyPerformer(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation);
	public List<HistoryItem> findHistoryItemsForPerformer(WorldObject worldObject);
	public HistoryItem getLastPerformedOperation(WorldObject worldObject);
	public Collection<HistoryItem> getAllLastPerformedOperations();
	public List<HistoryItem> findHistoryItems(ManagedOperation managedOperation);
	public HistoryItem getHistoryItem(int historyItemId);
	public List<HistoryItem> findHistoryItems(WorldObject performer, ManagedOperation managedOperation);

	
}
