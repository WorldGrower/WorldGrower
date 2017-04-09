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
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.ImageInfoReader;

public class IconUtils {

	private static final Image IMAGE_ICON;
	private static final Image IMAGE_ICON48;
	private static final Image NEW_ICON;
	private static final Image LOAD_ICON;
	private static final Image SAVE_ICON;
	private static final Image EXIT_ICON;
	private static final Image MOUSE_ICON;
	private static final Image CONTROLS_ICON;
	private static final Image NEW_TUTORIAL_GAME_ICON;
	private static final Image NEW_STANDARD_GAME_ICON;
	private static final Image NEW_CUSTOM_GAME_ICON;
	
	static {
		IMAGE_ICON = ImageUtils.getImage("/community.png");
		IMAGE_ICON48 = ImageUtils.getImage("/community48.png");
		NEW_ICON = ImageUtils.getImage("/planet_14.png");
		LOAD_ICON = ImageUtils.getImage("/load.png");
		SAVE_ICON = ImageUtils.getImage("/save.png");
		EXIT_ICON = ImageUtils.getImage("/x.png");
		MOUSE_ICON = ImageUtils.getImage("/cursor.png");
		CONTROLS_ICON = ImageUtils.getImage("/tools.png");
		NEW_TUTORIAL_GAME_ICON = ImageUtils.getImage("/swordWood.png");
		NEW_STANDARD_GAME_ICON = ImageUtils.getImage("/sword.png");
		NEW_CUSTOM_GAME_ICON = ImageUtils.getImage("/upg_sword.png");
	}

	public static void setIcon(Window frame) {
		frame.setIconImage(IMAGE_ICON);
	}
	
	public static ImageIcon getImageIcon48() {
		return new ImageIcon(IMAGE_ICON48);
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
	
	public static Image getWorldObjectImage(WorldObject performer, ImageInfoReader imageInfoReader) {
		ImageIds performerImageIds = performer.getProperty(Constants.IMAGE_ID);
		return imageInfoReader.getImage(performerImageIds, null);
	}
	
	public static Icon getWorldObjectIcon(WorldObject performer, ImageInfoReader imageInfoReader) {
		return new ImageIcon(getWorldObjectImage(performer, imageInfoReader));
	}

	public static ImageIcon getControlsIcon() {
		return new ImageIcon(CONTROLS_ICON);
	}
	
	public static ImageIcon getNewTutorialGameIcon() {
		return new ImageIcon(NEW_TUTORIAL_GAME_ICON);
	}
	
	public static ImageIcon getNewStandardGameIcon() {
		return new ImageIcon(NEW_STANDARD_GAME_ICON);
	}
	
	public static ImageIcon getNewCustomGameIcon() {
		return new ImageIcon(NEW_CUSTOM_GAME_ICON);
	}
}
