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

import javax.swing.JTextPane;

public class TiledImageTextPane extends JTextPane {  
    private final BufferedImage tileImage;  

    public BufferedImage getTiledImage(ImageInfoReader imageInfoReader) {
    	return (BufferedImage) imageInfoReader.getImage(ImageIds.SCREEN_BACKGROUND, null);
    }  
	
	public TiledImageTextPane(ImageInfoReader imageInfoReader) {
		super();
		tileImage = getTiledImage(imageInfoReader);
		setOpaque(false);
	} 

    @Override
    protected void paintComponent(Graphics g) {  
    	TiledImagePainter.paintComponent(this, g, tileImage);
    	super.paintComponent(g);
    }   
}