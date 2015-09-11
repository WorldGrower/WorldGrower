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

import org.worldgrower.Main;

public class IconUtils {

	private static final Image IMAGE_ICON;
	
	static {
		IMAGE_ICON = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/community.png"));
	}
	
	public static void setIcon(Window frame) {
		frame.setIconImage(IMAGE_ICON);
	}
}
