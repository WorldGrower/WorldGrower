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
import java.util.Arrays;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

/**
 * A HistoryItem represents an action that a performer performed on a target on a certain point in time.
 */
public class HistoryItem implements Serializable {
	private final int historyId;
	private final int performerId;
	private final int performerIdFacade;
	private final int targetId;
	private final int[] args;
	private final ManagedOperation action;
	private final int turnValue;
	private final Object additionalValue;
	private final HistoryWorldObjects historyWorldObjects;
	
	public HistoryItem(int historyId, OperationInfo operationInfo, Turn turn, Object additionalValue, HistoryWorldObjects historyWorldObjects) {
		this.historyId = historyId;
		this.performerId = operationInfo.getPerformer().getProperty(Constants.ID).intValue();
		WorldObject facade = operationInfo.getPerformer().getProperty(Constants.FACADE);
		if (facade != null) {
			performerIdFacade = facade.getProperty(Constants.ID).intValue();
		} else {
			performerIdFacade = -1;
		}
		Integer targetIdValue = operationInfo.getTarget().getProperty(Constants.ID);
		this.targetId = (targetIdValue != null ? targetIdValue.intValue() : -1);
		this.args = operationInfo.getArgs();
		this.action = operationInfo.getManagedOperation();
		this.turnValue = turn.getValue();
		this.additionalValue = additionalValue;
		this.historyWorldObjects = historyWorldObjects;
	}
	
	public int getHistoryId() {
		return historyId;
	}

	public WorldObject getPerformer() {
		return historyWorldObjects.get(performerId);
	}

	public WorldObject getTarget() {
		return historyWorldObjects.get(targetId);
	}

	int getPerformerId() {
		return performerId;
	}

	int getTargetId() {
		return targetId;
	}

	public int[] getArgs() {
		return args;
	}
	
	public ManagedOperation getManagedOperation() {
		return action;
	}
	
	public Turn getTurn() {
		return Turn.valueOf(turnValue);
	}

	public Object getAdditionalValue() {
		return additionalValue;
	}

	@Override
	public String toString() {
		return "HistoryItem [performerId=" + performerId + ",targetId=" + targetId + ",action=" + action + ", turn=" + turnValue + "]";
	}
	
	public boolean isEqual(HistoryItem h) {
		return (
				(this.performerId == h.performerId) 
				&& (this.targetId == h.targetId)
				&& Arrays.equals(this.args, h.args) 
				&& (this.action == h.action)
			);
	}
	
	public boolean matches(int performerId, int targetId, ManagedOperation managedOperation) {
		return (
				(this.performerId == performerId) 
				&& (this.targetId == targetId) 
				&& (this.action == managedOperation)
			);
	}
	
	public boolean isEqualUsingFacade(int performerId, int targetId, int[] args, ManagedOperation managedOperation) {
		final boolean isPerformerEqual;
		if (performerIdFacade != -1) {
			isPerformerEqual = (performerIdFacade == performerId);
		} else {
			isPerformerEqual = (this.performerId == performerId);
		}
		
		return (isPerformerEqual && (this.targetId == targetId) && Arrays.equals(this.args, args) && (this.action == managedOperation));
	}
	
	public String getThirdPersonDescription(World world) {
		return getPerformer().getProperty(Constants.NAME) + " was " + action.getDescription(getPerformer(), getTarget(), args, world);
	}
}