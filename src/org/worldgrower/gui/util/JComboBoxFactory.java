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
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.TiledImageComboPopup;
import org.worldgrower.gui.font.Fonts;

public class JComboBoxFactory {

	public static<T> JComboBox<T> createJComboBox(ImageInfoReader imageInfoReader) {
		JComboBox<T> comboBox = new JComboBox<T>();
		setComboBoxProperties(comboBox, imageInfoReader);
		return comboBox;
	}
	
	public static<T> JComboBox<T> createJComboBox(T[] values, ImageInfoReader imageInfoReader) {
		JComboBox<T> comboBox = new JComboBox<T>(values);
		setComboBoxProperties(comboBox, imageInfoReader);
		return comboBox;
	}
	
	public static<T> JComboBox<T> createJComboBox(T[] values, String[] tooltips, ImageInfoReader imageInfoReader) {
		JComboBox<T> comboBox = new JComboBox<T>(values);
		setComboBoxProperties(comboBox, imageInfoReader);
		comboBox.setRenderer(new ComboboxRenderer(Arrays.asList(tooltips)));
		return comboBox;
	}
	
	public static JComboBox<String> createJComboBox(ListData listData, ImageInfoReader imageInfoReader) {
		JComboBox<String> comboBox = new JComboBox<String>(listData.getList());
		setComboBoxProperties(comboBox, imageInfoReader);
		comboBox.setRenderer(new ComboboxRenderer(listData));
		return comboBox;
	}

	private static<T> void setComboBoxProperties(JComboBox<T> comboBox, ImageInfoReader imageInfoReader) {
		comboBox.setOpaque(false);
		comboBox.setBackground(ColorPalette.FOREGROUND_COLOR);
		comboBox.setForeground(ColorPalette.FOREGROUND_COLOR);
		comboBox.setFont(Fonts.FONT);
		
		comboBox.setUI(new MetalComboBoxUI() {

			@Override
			protected ComboPopup createPopup() {
				return new TiledImageComboPopup( comboBox, imageInfoReader );
			}
		});
	}
}