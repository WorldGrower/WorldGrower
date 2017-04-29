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

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public final class CustomPopupFactory {

	private static final int TOOLTIP_X_OFFSET = 50;

	public static void setPopupFactory() {
		PopupFactory.setSharedInstance(new PopupFactory() {

			@Override
			public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
				if (contents instanceof JToolTip) {
					JToolTip toolTip = (JToolTip)contents;
					int width = (int) toolTip.getPreferredSize().getWidth();
					
					GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
					int screenWidth = gd.getDisplayMode().getWidth();
					
					// if there is enough room, move tooltip to the right to have enough room
					// for large tooltips.
					// this way they don't hinder mouse movement and make it possible to easily
					// view multiple tooltips of items.
					if (x + width + TOOLTIP_X_OFFSET < screenWidth) {
						x += TOOLTIP_X_OFFSET;
					}
				}
				return super.getPopup(owner, contents, x, y);
			}
		});
	}
}
