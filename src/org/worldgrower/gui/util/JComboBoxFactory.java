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

import java.util.Arrays;

import javax.swing.JComboBox;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JComboBoxFactory {

	public static<T> JComboBox<T> createJComboBox() {
		JComboBox<T> comboBox = new JComboBox<T>();
		setComboBoxProperties(comboBox);
		return comboBox;
	}
	
	public static<T> JComboBox<T> createJComboBox(T[] values) {
		JComboBox<T> comboBox = new JComboBox<T>(values);
		setComboBoxProperties(comboBox);
		return comboBox;
	}
	
	public static<T> JComboBox<T> createJComboBox(T[] values, String[] tooltips) {
		JComboBox<T> comboBox = new JComboBox<T>(values);
		setComboBoxProperties(comboBox);
		comboBox.setRenderer(new ComboboxRenderer(Arrays.asList(tooltips)));
		return comboBox;
	}
	
	public static JComboBox<String> createJComboBox(ListData listData) {
		JComboBox<String> comboBox = new JComboBox<String>(listData.getList());
		setComboBoxProperties(comboBox);
		comboBox.setRenderer(new ComboboxRenderer(listData));
		return comboBox;
	}

	private static<T> void setComboBoxProperties(JComboBox<T> comboBox) {
		comboBox.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		comboBox.setForeground(ColorPalette.FOREGROUND_COLOR);
		comboBox.setFont(Fonts.FONT);
	}
}