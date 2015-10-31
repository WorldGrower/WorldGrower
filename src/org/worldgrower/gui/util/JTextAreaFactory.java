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

import javax.swing.JTextArea;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JTextAreaFactory {

	public static JTextArea createJTextArea(int rows, int columns) {
		JTextArea textArea = new JTextArea(rows, columns);
		setTextAreaProperties(textArea);
		return textArea;
	}
	
	private static void setTextAreaProperties(JTextArea textArea) {
		textArea.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		textArea.setForeground(ColorPalette.FOREGROUND_COLOR);
		textArea.setFont(Fonts.FONT);
	}
}