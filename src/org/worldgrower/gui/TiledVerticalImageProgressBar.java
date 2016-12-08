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
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class TiledVerticalImageProgressBar extends JProgressBar {
	private final BufferedImage tileImage;

    private BufferedImage getTiledImage(ImageIds imageId, ImageInfoReader imageInfoReader) {
    	return (BufferedImage) imageInfoReader.getImage(imageId, null);
    }
    
	public TiledVerticalImageProgressBar(ImageIds imageId, ImageInfoReader imageInfoReader) {
		super();
		this.tileImage = getTiledImage(imageId, imageInfoReader);
		changeUI();
	}
	
	public TiledVerticalImageProgressBar(int min, int max, ImageIds imageId, ImageInfoReader imageInfoReader) {
		super(JProgressBar.VERTICAL, min, max);
		this.tileImage = getTiledImage(imageId, imageInfoReader);
		changeUI();
	}

	void changeUI() {
		setUI(new BasicProgressBarUI() {

			@Override
			public void paint(Graphics g, JComponent c) {
				Insets b = TiledVerticalImageProgressBar.this.getInsets(); // area for border
		        int barRectWidth = TiledVerticalImageProgressBar.this.getWidth() - (b.right + b.left);
		        int barRectHeight = TiledVerticalImageProgressBar.this.getHeight() - (b.top + b.bottom);
		        
		        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
		        
		        g.clipRect(b.left, b.top + barRectHeight - amountFull, barRectWidth, amountFull);
				TiledImagePainter.paintComponent(TiledVerticalImageProgressBar.this, g, tileImage);
				g.setClip(null);
			}
		});
	}
}
