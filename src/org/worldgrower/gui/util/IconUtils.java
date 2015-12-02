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
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;
import org.worldgrower.gui.start.Game;

public class IconUtils {

	private static final Image IMAGE_ICON;
	private static final Image NEW_ICON;
	private static final Image LOAD_ICON;
	private static final Image SAVE_ICON;
	private static final Image EXIT_ICON;
	private static final Image MOUSE_ICON;
	
	static {
		IMAGE_ICON = getImage("/community.png");
		NEW_ICON = getImage("/new.png");
		LOAD_ICON = getImage("/load.png");
		SAVE_ICON = getImage("/save.png");
		EXIT_ICON = getImage("/exit.png");
		MOUSE_ICON = getImage("/cursor.png");
	}

	private static Image getImage(String imageURL) {
		return Toolkit.getDefaultToolkit().getImage(Game.class.getResource(imageURL));
	}
	
	public static void setIcon(Window frame) {
		frame.setIconImage(IMAGE_ICON);
	}

	public static ImageIcon getNewIcon() {
		return new ImageIcon(NEW_ICON);
	}

	public static ImageIcon getLoadIcon() {
		return new ImageIcon(LOAD_ICON);
	}

	public static ImageIcon getSaveIcon() {
		return new ImageIcon(SAVE_ICON);
	}

	public static ImageIcon getExitIcon() {
		return new ImageIcon(EXIT_ICON);
	}
	
	public static ImageIcon getMouseIcon() {
		return new ImageIcon(MOUSE_ICON);
	}
	
	public static Icon getWorldObjectIcon(WorldObject performer, ImageInfoReader imageInfoReader) {
		ImageIds performerImageIds = performer.getProperty(Constants.IMAGE_ID);
		Icon performerIcon = new ImageIcon(imageInfoReader.getImage(performerImageIds, null));
		return performerIcon;
	}
}
