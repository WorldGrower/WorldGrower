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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class TiledImagePainter {

	public static void paintComponent(JComponent component, Graphics g, BufferedImage tileImage) {

		Graphics2D g2 = (Graphics2D) g.create();
		int width = component.getWidth();  
        int height = component.getHeight();  
        for (int x = 0; x < width; x += tileImage.getWidth()) {  
            for (int y = 0; y < height; y += tileImage.getHeight()) {  
                g.drawImage(tileImage, x, y, component);  
            }  
        }
		g2.dispose();
	}
}