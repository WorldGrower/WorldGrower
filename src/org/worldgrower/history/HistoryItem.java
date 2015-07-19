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

public class HistoryItem implements Serializable {
	private final int historyId;
	private final OperationInfo operationInfo;
	private final Turn turn;

	public HistoryItem(int historyId, OperationInfo operationInfo, Turn turn) {
		this.historyId = historyId;
		this.operationInfo = operationInfo;
		this.turn = turn;
	}
	
	public int getHistoryId() {
		return historyId;
	}

	public OperationInfo getOperationInfo() {
		return operationInfo;
	}

	public Turn getTurn() {
		return turn;
	}

	@Override
	public String toString() {
		return "HistoryItem [operationInfo=" + operationInfo + ", turn=" + turn + "]";
	}

	public String getSecondPersonDescription(World world) {
		return operationInfo.getSecondPersonDescription(world);
	}
}