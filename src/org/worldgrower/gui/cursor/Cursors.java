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
package org.worldgrower.gui.cursor;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import org.worldgrower.gui.util.ImageUtils;

public class Cursors {

	public static final Cursor CURSOR;
	
	static {
		if (Toolkit.getDefaultToolkit().getMaximumCursorColors() <= 2) {
			CURSOR = Cursor.getDefaultCursor();
		} else {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = ImageUtils.getImage("/RPG_Mouse_Cursor_3.png");
			CURSOR = toolkit.createCustomCursor(image , new Point(0, 0), "cursor");
		}
	}
}
