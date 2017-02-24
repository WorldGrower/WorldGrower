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

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.worldgrower.conversation.ConversationFormatter;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.ImageSubstituter;
import org.worldgrower.gui.conversation.ConversationFormatterImpl;

public class GuiShowGoalDescriptionOverviewAction extends AbstractAction {
	
	private final ConversationFormatter conversationFormatter;
	
	public GuiShowGoalDescriptionOverviewAction(ImageInfoReader imageInfoReader) {
		super();
		this.conversationFormatter = new ConversationFormatterImpl(new ImageSubstituter(imageInfoReader));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame();
		
		JTable table = new JTable(new GoalModel());
		table.setRowHeight(50);
		table.setBounds(50, 50, 400, 700);
		frame.add(new JScrollPane(table));
	
		frame.setBounds(100,  100, 500, 800);
		frame.setVisible(true);
	}
	
	private class GoalModel extends AbstractTableModel {
		
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return Goals.ALL_GOALS.size();
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Name";
			} else if (columnIndex == 1) {
				return "Description";
			} else {
				return null;
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return Goals.ALL_GOALS.get(rowIndex).getClass().getSimpleName();
			} else if (columnIndex == 1) {
				return "<html>" + conversationFormatter.format(Goals.ALL_GOALS.get(rowIndex).getDescription()) + "</html>";
			} else {
				return null;
			}
		}
	}
}