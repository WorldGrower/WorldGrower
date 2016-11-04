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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

// http://stackoverflow.com/questions/24746354/java-jpanel-tiled-background-image
public class TiledImagePanel extends JPanel {  
    private final BufferedImage tileImage;  

    public TiledImagePanel() {  
    	try {
    		tileImage = ImageIO.read(new File("./resources/conc_patchwork_c.png"));
    	} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    }  

    @Override
    protected void paintComponent(Graphics g) {  
    	TiledImagePainter.paintComponent(this, g, tileImage);
    }   
}