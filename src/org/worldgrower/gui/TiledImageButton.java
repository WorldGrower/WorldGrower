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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TiledImageButton extends JButton {
	private static final int BORDER_PADDING = 4;

	private final BufferedImage tileImage;  

    public BufferedImage getTiledImage(ImageInfoReader imageInfoReader) {
    	return (BufferedImage) imageInfoReader.getImage(ImageIds.BUTTON_BACKGROUND, null);
    }  
	
	public TiledImageButton(String text, ImageInfoReader imageInfoReader) {
		super(text);
		setPaintOptions();
		tileImage = getTiledImage(imageInfoReader);
	}

	public TiledImageButton(String text, ImageIcon icon, ImageInfoReader imageInfoReader) {
		super(text, icon);
		setPaintOptions();
		tileImage = getTiledImage(imageInfoReader);
	}

	private void setPaintOptions() {
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorder(null);
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();
		if (getModel().isPressed()) {
			g.drawImage(tileImage,0,0,this);
		} else {
			g.drawImage(tileImage,0,0,this);
		}
		
		if (isFocusOwner()) {
			drawDashedLine(g2, BORDER_PADDING, BORDER_PADDING, getWidth() - BORDER_PADDING, BORDER_PADDING);
			drawDashedLine(g2, getWidth() - BORDER_PADDING, BORDER_PADDING, getWidth() - BORDER_PADDING, getHeight() - BORDER_PADDING);
			drawDashedLine(g2, getWidth() - BORDER_PADDING, getHeight() - BORDER_PADDING, BORDER_PADDING, getHeight() - BORDER_PADDING);
			drawDashedLine(g2, BORDER_PADDING, getHeight() - BORDER_PADDING, BORDER_PADDING, BORDER_PADDING);
		}
		
		g2.dispose();

		super.paintComponent(g);
	}
	
	private void drawDashedLine(Graphics2D g2, int x1, int y1, int x2, int y2){
       
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        g2.setStroke(dashed);
        g2.setColor(ColorPalette.FOREGROUND_COLOR);
        g2.drawLine(x1, y1, x2, y2);
	}
}