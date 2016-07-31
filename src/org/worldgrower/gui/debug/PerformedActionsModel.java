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
package org.worldgrower.gui.debug;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.history.HistoryItem;

public class PerformedActionsModel extends AbstractTableModel {

	private final List<HistoryItem> historyItems;
	
	public PerformedActionsModel(WorldObject worldObject, World world) {
		super();
		historyItems = world.getHistory().findHistoryItemsForPerformer(worldObject);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return historyItems.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		HistoryItem historyItem = historyItems.get(row);
		return historyItem.getTurn().getValue() + ":"
				+ historyItem.getManagedOperation().getSimpleDescription()
				+ asList(historyItem.getArgs());
	}

	private String asList(int[] args) {
		StringBuilder argsBuilder = new StringBuilder("[");
		for(int i=0; i<args.length; i++) {
			argsBuilder.append(args[i]);
			if (i < args.length - 1) {
				argsBuilder.append(", ");
			}
		}
		argsBuilder.append("]");
		return argsBuilder.toString();
	}
}