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

import javax.swing.JTextPane;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.TiledImageTextPane;
import org.worldgrower.gui.font.Fonts;

public class JTextPaneFactory {

	public static JTextPane createJTextPane(ImageInfoReader imageInfoReader) {
		JTextPane textPane = new TiledImageTextPane(imageInfoReader);
		setTextPaneProperties(textPane);
		return textPane;
	}
	
	public static JTextPane createHmtlJTextPane(ImageInfoReader imageInfoReader) {
		JTextPane textPane = createJTextPane(imageInfoReader);
		textPane.setContentType("text/html");
		textPane.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, true);
		return textPane;
	}
	
	private static void setTextPaneProperties(JTextPane textPane) {
		textPane.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		textPane.setForeground(ColorPalette.FOREGROUND_COLOR);
		textPane.setFont(Fonts.FONT);
	}
}