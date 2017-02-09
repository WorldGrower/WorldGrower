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

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JLabelFactory {

	public static JLabel createJLabel(String description) {
		JLabel label = new JLabel(description);
		setLabelProperties(label);
		return label;
	}
	
	public static JLabel createJLabel(int value) {
		return createJLabel(Integer.toString(value));
	}
	
	public static JLabel createJLabel(Image image) {
		JLabel label = new JLabel(new ImageIcon(image));
		setLabelProperties(label);
		return label;
	}
	
	public static JLabel createJLabel(int value, Image image) {
		return createJLabel(Integer.toString(value), image);
	}
	
	public static JLabel createJLabel(String description, Image image) {
		JLabel label = new JLabel(description, new ImageIcon(image), JLabel.HORIZONTAL);
		setLabelProperties(label);
		return label;
	}

	private static void setLabelProperties(JLabel label) {
		label.setForeground(ColorPalette.FOREGROUND_COLOR);
		label.setFont(Fonts.FONT);
	}
}