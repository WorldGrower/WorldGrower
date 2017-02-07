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
package org.worldgrower.gui.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import org.worldgrower.gui.ImageInfoReader;

public class Fonts {

	private static final int FONT_SIZE = 18;
	
	public static final Font FONT = createFont();
	
	private static Font createFont() {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, ImageInfoReader.class.getResourceAsStream("/ufonts.com_tw-cen-mt.ttf"));
			Font derivedFont = font.deriveFont((float)FONT_SIZE);
			
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			genv.registerFont(derivedFont);
			
			return derivedFont;
			
		} catch (FontFormatException | IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static int getFontSize() {
		return FONT_SIZE;
	}
}
