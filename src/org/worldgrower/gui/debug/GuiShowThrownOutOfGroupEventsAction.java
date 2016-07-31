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

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.DefaultGoalObstructedHandler;

public class GuiShowThrownOutOfGroupEventsAction extends AbstractAction {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new WorldModel());
		table.getColumnModel().getColumn(0).setCellRenderer(new TooltipCellRenderer());
		table.setBounds(50, 50, 1000, 800);
		frame.add(new JScrollPane(table));
		
		frame.setBounds(100,  100, 900, 900);
		frame.setVisible(true);
	}
	
	private List<String> getEvents() {
		return DefaultGoalObstructedHandler.getThrownOutOfGroupEvents();
	}
	
	private class WorldModel extends AbstractTableModel {

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return getEvents().size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Event";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return getEvents().get(rowIndex);
		}
	}
}