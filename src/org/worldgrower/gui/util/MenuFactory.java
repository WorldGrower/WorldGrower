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

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.font.Fonts;

public class MenuFactory {

	public static JPopupMenu createJPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		popupMenu.setForeground(ColorPalette.FOREGROUND_COLOR);
		popupMenu.setFont(Fonts.FONT);
		return popupMenu;
	}
	
	public static JMenuItem createJMenuItem(Action action) {
		JMenuItem menuItem = new JMenuItem(action);
		setMenuProperties(menuItem);
		return menuItem;
	}
	
	public static JMenuItem createJMenuItem(String description) {
		JMenuItem menuItem = new JMenuItem(description);
		setMenuProperties(menuItem);
		return menuItem;
	}

	private static void setMenuProperties(JMenuItem menuItem) {
		menuItem.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		menuItem.setForeground(ColorPalette.FOREGROUND_COLOR);
		menuItem.setFont(Fonts.FONT);
	}
	
	public static JMenu createJMenu(String description) {
		JMenu menu = new JMenu(description);
		menu.setOpaque(true);
		menu.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		menu.setForeground(ColorPalette.FOREGROUND_COLOR);
		menu.setFont(Fonts.FONT);
		return menu;
	}
	
	public static JCheckBoxMenuItem createJCheckBoxMenuItem(Action action) {
		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(action);
		setMenuProperties(checkBoxMenuItem);
		return checkBoxMenuItem;
	}
}
