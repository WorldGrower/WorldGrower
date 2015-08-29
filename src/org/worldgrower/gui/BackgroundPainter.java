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
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.worldgrower.World;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainType;

public class BackgroundPainter {

	private final Map<TerrainType, Color> terrainTypesToColor = new HashMap<>();
	private Image[] backgroundImages = new Image[TerrainType.values().length];
	private Image[] backgroundTransitionMap = new Image[TerrainType.values().length * TerrainType.values().length * TerrainType.values().length * TerrainType.values().length];
	
	public BackgroundPainter(Image grassBackgroundImage, World world) {
		terrainTypesToColor.put(TerrainType.WATER, new Color(0, 0, 163));
		terrainTypesToColor.put(TerrainType.GRASLAND, calculateColorForImage(grassBackgroundImage));
		terrainTypesToColor.put(TerrainType.PLAINS, new Color(235, 195, 75));
		terrainTypesToColor.put(TerrainType.HILL, new Color(171, 140, 17));
		terrainTypesToColor.put(TerrainType.MOUNTAIN, new Color(161, 161, 161));
		
		fillBackgroundImagesMap(grassBackgroundImage);
		fillBackgroundTransitionMap(world);
	}
	
	private Color calculateColorForImage(Image image) {
		BufferedImage bufferedImage = toBufferedImage(image);
		long redBucket = 0;
		long greenBucket = 0;
		long blueBucket = 0;
		long pixelCount = 0;

		for (int y = 0; y < bufferedImage.getHeight(); y++) {
		    for (int x = 0; x < bufferedImage.getWidth(); x++) {
		        Color c = new Color(bufferedImage.getRGB(x, y));

		        pixelCount++;
		        redBucket += c.getRed();
		        greenBucket += c.getGreen();
		        blueBucket += c.getBlue();
		    }
		}

		int red = (int)(redBucket / pixelCount);
		int green = (int)(greenBucket / pixelCount);
		int blue = (int)(blueBucket / pixelCount);
		
		return new Color(red, green, blue);
	}
	
	private void fillBackgroundImagesMap(Image grassBackgroundImage) {
		for(Entry<TerrainType, Color> entry : terrainTypesToColor.entrySet()) {
			TerrainType terrainType = entry.getKey();
			Color currentColor = entry.getValue();
			
			if (terrainType == TerrainType.GRASLAND) {
				addBackgroundImage(terrainType, cropImage(toBufferedImage(grassBackgroundImage), 48, 48));
			} else {
				Color grassColor = terrainTypesToColor.get(TerrainType.GRASLAND);
				
				int deltaRed = currentColor.getRed() - grassColor.getRed();
				int deltaGreen = currentColor.getGreen() - grassColor.getGreen();
				int deltaBlue = currentColor.getBlue() - grassColor.getBlue();
				
				Image filteredImage = filterImage(grassBackgroundImage, new ColorFilter(deltaRed, deltaGreen, deltaBlue));
				BufferedImage bufferedImage = toBufferedImage(filteredImage); 
				addBackgroundImage(terrainType, cropImage(bufferedImage, 48, 48));
			}
		}
	}

	private void fillBackgroundTransitionMap(World world) {
		for(int x = 0; x<world.getWidth() ;x++) {
			for(int y = 0; y<world.getHeight(); y++) {
				Terrain terrain = world.getTerrain();
				TerrainType terrainType = terrain.getTerrainInfo(x, y).getTerrainType();
				int key = getKeyForBackgroundTransitionMap(terrain, x, y, world);
				if(backgroundTransitionMap[key] == null) {
					final TerrainType left = getTerrainTypeFor(terrain, x-1, y, world, terrainType);
					final TerrainType right = getTerrainTypeFor(terrain, x+1, y, world, terrainType);
					final TerrainType up = getTerrainTypeFor(terrain, x, y-1, world, terrainType);
					final TerrainType down = getTerrainTypeFor(terrain, x, y+1, world, terrainType);
					
					Image image = getBackgroundImage(terrainType);
					Image newImage = filterImage(image, new TerrainTransitionFilter(terrainType, left, right, up, down, terrainTypesToColor));
					BufferedImage bufferedImage = toBufferedImage(newImage); 
										
					backgroundTransitionMap[key] = bufferedImage;
				}
			}
		}
	}
	
	private int getKeyForBackgroundTransitionMap(Terrain terrain, int x, int y, World world) {
		TerrainType terrainType = terrain.getTerrainInfo(x, y).getTerrainType();
		
		TerrainType left = getTerrainTypeFor(terrain, x-1, y, world, terrainType);
		TerrainType right = getTerrainTypeFor(terrain, x+1, y, world, terrainType);
		TerrainType up = getTerrainTypeFor(terrain, x, y-1, world, terrainType);
		TerrainType down = getTerrainTypeFor(terrain, x, y+1, world, terrainType);
		
		if (surroundingTerrainHasLowerPriority(terrainType, left, right, up, down)) {
			left = terrainType;
			right = terrainType;
			up = terrainType;
			down = terrainType;
		}
		
		int numberOfTerrainTypes = TerrainType.values().length;
		return left.ordinal() 
				+ right.ordinal() * numberOfTerrainTypes 
				+ up.ordinal() * numberOfTerrainTypes * numberOfTerrainTypes
				+ down.ordinal() * numberOfTerrainTypes * numberOfTerrainTypes * numberOfTerrainTypes;
				
	}
	
	private boolean surroundingTerrainHasLowerPriority(TerrainType terrainType, TerrainType left, TerrainType right, TerrainType up, TerrainType down) {
		Set<TerrainType> terrainTypes = new HashSet<>();
		terrainTypes.add(left);
		terrainTypes.add(right);
		terrainTypes.add(up);
		terrainTypes.add(down);
		terrainTypes.remove(terrainType);
		
		if (terrainTypes.size() > 1) {
			throw new IllegalStateException("Multiple terrainTypes: "+ terrainTypes);
		}
		
		return (terrainTypes.size() > 0 && terrainTypes.iterator().next().ordinal() < terrainType.ordinal());

	}

	private TerrainType getTerrainTypeFor(Terrain terrain, int x, int y, World world, TerrainType terrainType) {
		final TerrainType terrainTypeForLocation;
		if (LocationUtils.areInvalidCoordinates(x, y, world)) {
			terrainTypeForLocation = terrainType;
		} else {
			terrainTypeForLocation = terrain.getTerrainInfo(x, y).getTerrainType();
		}
		return terrainTypeForLocation;
	}
	
	private void addBackgroundImage(TerrainType terrainType, Image backgroundImage) {
		backgroundImages[terrainType.ordinal()] = backgroundImage;
	}
	
	private Image getBackgroundImage(TerrainType terrainType) {
		return backgroundImages[terrainType.ordinal()];
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
					Image image = backgroundTransitionMap[getKeyForBackgroundTransitionMap(terrain, x, y, world)];
					worldPanel.drawBackgroundImage(g, image, x, y);
				} else {
					worldPanel.drawUnexploredTerrain(g, x, y);
				}
			}
		}
	}
	
	private Image filterImage(Image sourceImage, ImageFilter imageFilter) {
		ImageProducer ip = new FilteredImageSource(sourceImage.getSource(), imageFilter);
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
	
	static class TerrainTransitionFilter extends RGBImageFilter {
		private final Color colorCurrent;
		private final Color colorLeft;
		private final Color colorRight;
		private final Color colorUp;
		private final Color colorDown;
		
		public TerrainTransitionFilter(TerrainType current, TerrainType left, TerrainType right, TerrainType up, TerrainType down, Map<TerrainType, Color> terrainTypesToColor) {
			super();
			this.colorCurrent = terrainTypesToColor.get(current);
			this.colorLeft = terrainTypesToColor.get(left);
			this.colorRight = terrainTypesToColor.get(right);
			this.colorUp = terrainTypesToColor.get(up);
			this.colorDown = terrainTypesToColor.get(down);
			
			canFilterIndexColorModel = true;
		}
		
		@Override
		public int filterRGB(int x, int y, int rgb) {
			int r = ((rgb >> 16) & 0xff);
			int g = ((rgb >> 8) & 0xff);
			int b = ((rgb >> 0) & 0xff);

			float alphaLeft = calculateAlphaLeft(x, colorCurrent, colorLeft);
			float alphaRight = calculateAlphaRight(x, colorCurrent, colorRight);
			float alphaUp = calculateAlphaLeft(y, colorCurrent, colorUp);
			float alphaDown = calculateAlphaRight(y, colorCurrent, colorDown);
			
			float sumOfALphas = alphaLeft + alphaRight + alphaUp + alphaDown;
			if (sumOfALphas > 1.0f) {
				alphaLeft = alphaLeft / sumOfALphas;
				alphaRight = alphaRight / sumOfALphas;
				alphaUp = alphaUp / sumOfALphas;
				alphaDown = alphaDown / sumOfALphas;
			}
			
			float alpha = 1f - alphaLeft - alphaRight - alphaUp - alphaDown;
			
			r = (int) (alpha * r 
					+ alphaLeft * colorLeft.getRed() 
					+ alphaRight * colorRight.getRed() 
					+ alphaUp * colorUp.getRed() 
					+ alphaDown * colorDown.getRed());

			g = (int) (alpha * g 
					+ alphaLeft * colorLeft.getGreen() 
					+ alphaRight * colorRight.getGreen() 
					+ alphaUp * colorUp.getGreen() 
					+ alphaDown * colorDown.getGreen());

			b = (int) (alpha * b 
					+ alphaLeft * colorLeft.getBlue() 
					+ alphaRight * colorRight.getBlue() 
					+ alphaUp * colorUp.getBlue() 
					+ alphaDown * colorDown.getBlue());
			
			return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
		}

		private float calculateAlphaRight(int x, Color colorCurrent, Color colorRight) {
			float alphaRight;
			if (colorCurrent.equals(colorRight)) {
				alphaRight = 0f;
			} else {
				alphaRight = Math.max((x*4 - 96.0f) / 100.0f, 0f);
			}
			return alphaRight;
		}

		private float calculateAlphaLeft(int x, Color colorCurrent, Color colorLeft) {
			float alphaLeft;
			if (colorCurrent.equals(colorLeft)) {
				alphaLeft = 0f;
			} else {
				alphaLeft = (96.0f - x*2) / 100.0f;
			}
			return alphaLeft;
		}
	}
}
