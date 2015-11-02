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

import javax.swing.JList;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JListFactory {

	public static JList<String> createJList(String[] values) {
		JList<String> list = new JList<>(values);
		setListProperties(list);
		return list;
	}
	
	public static<T> JList<T> createJList(T[] values) {
		JList<T> list = new JList<T>(values);
		setListProperties(list);
		return list;
	}
	
	private static<T> void setListProperties(JList<T> list) {
		list.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		list.setForeground(ColorPalette.FOREGROUND_COLOR);
		list.setFont(Fonts.FONT);
	}
}