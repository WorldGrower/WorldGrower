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
package org.worldgrower.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.plaf.PopupMenuUI;

public class TiledImageJMenu extends JMenu {

	private final BufferedImage tileImage;  

    private BufferedImage getTiledImage(ImageInfoReader imageInfoReader) {
    	return (BufferedImage) imageInfoReader.getImage(ImageIds.SCREEN_BACKGROUND, null);
    }  
	
	public TiledImageJMenu(String text, ImageInfoReader imageInfoReader) {
		super(text);
		tileImage = getTiledImage(imageInfoReader);
		getPopupMenu().setUI(new PopupMenuUI() {

			@Override
			public void paint(Graphics g, JComponent c) {
				TiledImagePainter.paintComponent(c, g, tileImage);
				super.paint(g, c);
			}
		});
	}
}