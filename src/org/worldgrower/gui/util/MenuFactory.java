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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.worldgrower.gui.ColorPalette;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.SwingUtils;
import org.worldgrower.gui.TiledImageJMenu;
import org.worldgrower.gui.TiledImageJMenuItem;
import org.worldgrower.gui.TiledImagePopupMenu;
import org.worldgrower.gui.cursor.Cursors;
import org.worldgrower.gui.font.Fonts;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.music.SoundIds;

public class MenuFactory {

	public static JPopupMenu createJPopupMenu(ImageInfoReader imageInfoReader) {
		JPopupMenu popupMenu = new TiledImagePopupMenu(imageInfoReader);
		popupMenu.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		popupMenu.setForeground(ColorPalette.FOREGROUND_COLOR);
		popupMenu.setFont(Fonts.FONT);
		popupMenu.setCursor(Cursors.CURSOR);
		return popupMenu;
	}
	
	public static JMenuItem createJMenuItem(Action action, SoundIdReader soundIdReader) {
		JMenuItem menuItem = createJMenuItem(action);
		addRollOverSoundEffect(menuItem, soundIdReader);
		return menuItem;
	}
	
	private static JMenuItem createJMenuItem(Action action) {
		JMenuItem menuItem = new TiledImageJMenuItem(action, null);
		setMenuProperties(menuItem);
		return menuItem;
	}
	
	public static JMenuItem createJMenuItem(String description, SoundIdReader soundIdReader) {
		JMenuItem menuItem = createJMenuItem(description);
		addRollOverSoundEffect(menuItem, soundIdReader);
		return menuItem;
	}
	
	private static void addRollOverSoundEffect(JMenuItem menuItem, SoundIdReader soundIdReader) {
		if (soundIdReader == null) {
			throw new IllegalStateException("soundIdReader is null");
		}
		
		menuItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				soundIdReader.playSoundEffect(SoundIds.ROLLOVER);
			}
		});
	}

	private static JMenuItem createJMenuItem(String description) {
		JMenuItem menuItem = new TiledImageJMenuItem(description, null);
		setMenuProperties(menuItem);
		return menuItem;
	}

	private static void setMenuProperties(JMenuItem menuItem) {
		menuItem.setOpaque(false);
		menuItem.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		menuItem.setForeground(ColorPalette.FOREGROUND_COLOR);
		menuItem.setFont(Fonts.FONT);
		menuItem.setCursor(Cursors.CURSOR);
	}
	
	public static JMenu createJMenu(String description, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		JMenu menu = new TiledImageJMenu(description, imageInfoReader);
		menu.setBackground(ColorPalette.DARK_BACKGROUND_COLOR);
		menu.setForeground(ColorPalette.FOREGROUND_COLOR);
		menu.setFont(Fonts.FONT);
		menu.setCursor(Cursors.CURSOR);
		addRollOverSoundEffect(menu, soundIdReader);
		return menu;
	}
	
	public static JMenu createJMenu(String description, ImageIds imageId, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader) {
		JMenu menuItem = createJMenu(description, imageInfoReader, soundIdReader);
		SwingUtils.setMenuIcon(menuItem, imageId, imageInfoReader);
		return menuItem;
	}
	
	public static JCheckBoxMenuItem createJCheckBoxMenuItem(Action action, SoundIdReader soundIdReader) {
		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(action);
		setMenuProperties(checkBoxMenuItem);
		addRollOverSoundEffect(checkBoxMenuItem, soundIdReader);
		return checkBoxMenuItem;
	}
}
