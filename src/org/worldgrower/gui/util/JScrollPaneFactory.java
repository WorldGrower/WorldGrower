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
package org.worldgrower.gui.util;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;

public class JScrollPaneFactory {

	public static JScrollPane createScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		
		setScrollPaneProperties(scrollPane);
		
		return scrollPane;
	}

	private static void setScrollPaneProperties(JScrollPane scrollPane) {
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
	}
	
	public static JScrollPane createScrollPane(JTable table) {
		JScrollPane scrollPane = new JScrollPane(table);
		
		setScrollPaneProperties(scrollPane);
		
		return scrollPane;
	}
	
	public static JScrollPane createScrollPane(JTree tree) {
		JScrollPane scrollPane = new JScrollPane(tree);
		
		setScrollPaneProperties(scrollPane);
		
		return scrollPane;
	}
}
