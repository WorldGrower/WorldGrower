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

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class TextComboBoxRenderer<T> extends JLabel implements ListCellRenderer<T> {

	public TextComboBoxRenderer(int horizontalAlignment) {
		setOpaque(false);
		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(CENTER);
	}

	public Component getListCellRendererComponent(JList list, T value, int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.toString());
		setForeground(list.getForeground());
		return this;
	}
}