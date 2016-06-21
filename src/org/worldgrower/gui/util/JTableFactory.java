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

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JTableFactory {

	public static JTable createJTable() {
		JTable table = new JTable();
		setTableProperties(table);
		return table;
	}

	public static void setTableProperties(JTable table) {
		table.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		table.setForeground(ColorPalette.FOREGROUND_COLOR);
		table.setSelectionBackground(ColorPalette.DARK_BACKGROUND_COLOR.brighter());
		table.setSelectionForeground(ColorPalette.FOREGROUND_COLOR.brighter());
		table.setFont(Fonts.FONT);
		
		table.getTableHeader().setDefaultRenderer(new DefaultHeaderRenderer(table.getTableHeader().getDefaultRenderer()));
	}

	public static JTable createJTable(TableModel model) {
		JTable table = new JTable(model);
		setTableProperties(table);
		return table;
	}
	
	private static class DefaultHeaderRenderer implements TableCellRenderer {

		private TableCellRenderer defaultRenderer;
		
	    public DefaultHeaderRenderer(TableCellRenderer defaultRenderer) {
	        this.defaultRenderer = defaultRenderer;
	    }
		
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	    	Component comp = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    	 
	        if (comp instanceof JLabel) {
	        	JLabel label = (JLabel) comp;
	        	label.setFont(Fonts.BOLD_FONT);
	        	label.setOpaque(true);
	        	label.setForeground(ColorPalette.DARK_BACKGROUND_COLOR);
	        	label.setBackground(ColorPalette.FOREGROUND_COLOR);
	        	label.setBorder(BorderFactory.createEtchedBorder());
	        	label.setText(value.toString());
	        }
	        return comp;
	    }
	 
	}
}
