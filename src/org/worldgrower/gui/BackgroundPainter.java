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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.worldgrower.World;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainType;

public class BackgroundPainter {

	private final Map<TerrainType, Color> terrainTypesToColor = new HashMap<>();
	private final Map<TerrainType, Image> backgroundImages = new HashMap<>();

	public BackgroundPainter(Image grassBackgroundImage) {
		terrainTypesToColor.put(TerrainType.WATER, new Color(0, 0, 163));
		terrainTypesToColor.put(TerrainType.GRASLAND, new Color(110, 196, 88));
		terrainTypesToColor.put(TerrainType.PLAINS, new Color(235, 195, 75));
		terrainTypesToColor.put(TerrainType.HILL, new Color(171, 140, 17));
		terrainTypesToColor.put(TerrainType.MOUNTAIN, new Color(161, 161, 161));
		
		fillbackgroundImagesMap(grassBackgroundImage);
	}
	
	private void fillbackgroundImagesMap(Image grassBackgroundImage) {
		for(Entry<TerrainType, Color> entry : terrainTypesToColor.entrySet()) {
			TerrainType terrainType = entry.getKey();
			Color currentColor = entry.getValue();
			
			if (terrainType == TerrainType.GRASLAND) {
				backgroundImages.put(terrainType, cropImage(toBufferedImage(grassBackgroundImage), 48, 48));
			} else {
				Color grassColor = terrainTypesToColor.get(TerrainType.GRASLAND);
				
				int deltaRed = currentColor.getRed() - grassColor.getRed();
				int deltaGreen = currentColor.getGreen() - grassColor.getGreen();
				int deltaBlue = currentColor.getBlue() - grassColor.getBlue();
				
				Image filteredImage = filterImage(grassBackgroundImage, new ColorFilter(deltaRed, deltaGreen, deltaBlue));
				BufferedImage bufferedImage = toBufferedImage(filteredImage); 
				backgroundImages.put(terrainType, cropImage(bufferedImage, 48, 48));
			}
		}
	}

	private BufferedImage cropImage(BufferedImage src, int width, int height) {
		BufferedImage dest = src.getSubimage(0, 0, width, height);
		return dest;
	}

	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	public void paint(Graphics g, World world, WorldPanel worldPanel) {
		Terrain terrain = world.getTerrain();

		for(int x = 0; x<world.getWidth() ;x++) {
			for(int y = 0; y<world.getHeight(); y++) {
				if (terrain.isExplored(x, y)) {
					TerrainType terrainType = terrain.getTerrainInfo(x, y).getTerrainType();
					Image image = backgroundImages.get(terrainType);
					worldPanel.drawBackgroundImage(g, image, x, y);
				} else {
					worldPanel.drawUnexploredTerrain(g, x, y);
				}
			}
		}
	}
	
	private Image filterImage(Image sourceImage, ColorFilter colorFilter) {
		ImageProducer ip = new FilteredImageSource(sourceImage.getSource(), colorFilter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	static class ColorFilter extends RGBImageFilter {
		private final int deltaRed;
		private final int deltaGreen;
		private final int deltaBlue;

		public ColorFilter(int deltaRed, int deltaGreen, int deltaBlue) {
			super();
			this.deltaRed = deltaRed;
			this.deltaGreen = deltaGreen;
			this.deltaBlue = deltaBlue;

			canFilterIndexColorModel = true;
		}

		@Override
		public int filterRGB(int x, int y, int rgb) {
			int r = ((rgb >> 16) & 0xff) + deltaRed;
			int g = ((rgb >> 8) & 0xff) + deltaGreen;
			int b = ((rgb >> 0) & 0xff) + deltaBlue;

			r = normalize(r);
			g = normalize(g);
			b = normalize(b);
			
			return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
		}

		private int normalize(int colorValue) {
			if (colorValue < 0) {
				colorValue = 0;
			}
			if (colorValue > 255) {
				colorValue = 255;
			}
			return colorValue;
		}
	}
}
