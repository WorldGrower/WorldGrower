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

import org.worldgrower.OperationInfo;
import org.worldgrower.World;

/**
 * A HistoryItem represents an action that a performer performed on a target on a certain point in time.
 */
public class HistoryItem implements Serializable {
	private final int historyId;
	private final OperationInfo operationInfo;
	private final int turnValue;
	private final Object additionalValue;

	public HistoryItem(int historyId, OperationInfo operationInfo, Turn turn, Object additionalValue) {
		this.historyId = historyId;
		this.operationInfo = operationInfo;
		this.turnValue = turn.getValue();
		this.additionalValue = additionalValue;
	}
	
	public int getHistoryId() {
		return historyId;
	}

	public OperationInfo getOperationInfo() {
		return operationInfo;
	}

	public Turn getTurn() {
		return Turn.valueOf(turnValue);
	}

	public Object getAdditionalValue() {
		return additionalValue;
	}

	@Override
	public String toString() {
		return "HistoryItem [operationInfo=" + operationInfo + ", turn=" + turnValue + "]";
	}

	public String getSecondPersonDescription(World world) {
		return operationInfo.getSecondPersonDescription(world);
	}
}