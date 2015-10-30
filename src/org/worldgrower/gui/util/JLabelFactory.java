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

public class JLabelFactory {

	public static JLabel createJLabel(String description) {
		return new JLabel(description);
	}
	
	public static JLabel createJLabel(int value) {
		return createJLabel(Integer.toString(value));
	}
	
	public static JLabel createJLabel(Image image) {
		return new JLabel(new ImageIcon(image));
	}
}
