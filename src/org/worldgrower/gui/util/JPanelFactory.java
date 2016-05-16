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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class JPanelFactory {

	public static JPanel createJPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(createBorder(title));
		panel.setOpaque(false);
		panel.setForeground(ColorPalette.FOREGROUND_COLOR);
		
		return panel;
	}
	
	public static JPanel createBorderlessPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setForeground(ColorPalette.FOREGROUND_COLOR);
		
		return panel;
	}

	private static TitledBorder createBorder(String title) {
		TitledBorder border = BorderFactory.createTitledBorder(title);
		border.setTitleColor(ColorPalette.FOREGROUND_COLOR);
		border.setTitleFont(Fonts.FONT);
		return border;
	}
}