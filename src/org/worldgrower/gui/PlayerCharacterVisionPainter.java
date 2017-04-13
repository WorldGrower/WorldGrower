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
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.PerceptionPropertyUtils;

public class PlayerCharacterVisionPainter {

	private int lastCircleRadius = 0;
	private int lastPlayerCharacterX = -1;
	private int lastPlayerCharacterY = -1;
	private BufferedImage lastImage = null;
	
	private final Map<Integer, BufferedImage> playerVisionImages = new HashMap<>();
	
	public void paintPlayerCharacterVision(Graphics worldPanelGraphics, WorldObject playerCharacter, World world, WorldPanel worldPanel) {
		int circleRadius = (PerceptionPropertyUtils.calculateRadius(playerCharacter, world) + 1) * 48;
		int playerCharacterX = worldPanel.getScreenX(playerCharacter.getProperty(Constants.X)) * 48;
		int playerCharacterY = worldPanel.getScreenY(playerCharacter.getProperty(Constants.Y)) * 48;
		
		final BufferedImage image;
		if (circleRadius == lastCircleRadius && playerCharacterX == lastPlayerCharacterX && playerCharacterY == lastPlayerCharacterY) {
			image = lastImage;
		} else {
			image = createImageToDraw(worldPanel, circleRadius, playerCharacterX, playerCharacterY);
		}
		worldPanelGraphics.drawImage(image, 0, 0, null);
		
		lastCircleRadius = circleRadius;
		lastPlayerCharacterX = playerCharacterX;
		lastPlayerCharacterY = playerCharacterY;
		lastImage = image;
	}

	private BufferedImage createImageToDraw(WorldPanel worldPanel, int circleRadius, int playerCharacterX, int playerCharacterY) {
		float circleDiameter = circleRadius * 2.0f;
		Shape circle = new Ellipse2D.Float(playerCharacterX - circleRadius, playerCharacterY - circleRadius, circleDiameter-1, circleDiameter-1);
		BufferedImage image = new BufferedImage(worldPanel.getWorldViewWidth(), worldPanel.getWorldViewHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D ga = (Graphics2D) image.createGraphics();
		
		BufferedImage playerVisionImage = getPlayerVisionImage(circleRadius);
		ga.drawImage(playerVisionImage, playerCharacterX - circleRadius, playerCharacterY - circleRadius, null);
		
		Area outter = new Area(circle.getBounds());
        outter.subtract(new Area(circle));
        
        ga.setColor(Color.BLACK);
        ga.fill(outter);
		
		ga.dispose();
		return image;
	}
	
	private BufferedImage getPlayerVisionImage(int radius) {
		BufferedImage playerVisionImage = playerVisionImages.get(radius);
		if (playerVisionImage == null) {
			playerVisionImage = createPlayerVisionImage(radius);
			playerVisionImages.put(radius, playerVisionImage);
		}
		return playerVisionImage;
	}

	private BufferedImage createPlayerVisionImage(int circleRadius) {
		float circleDiameter = circleRadius * 2.0f;
		Shape circle = new Ellipse2D.Float(0, 0, circleDiameter, circleDiameter);
		BufferedImage image = new BufferedImage((int)circleDiameter, (int)circleDiameter, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D ga = (Graphics2D) image.createGraphics();
		
		Point2D center = new Point2D.Float(circleRadius, circleRadius);
	    Point2D focus = new Point2D.Float(circleRadius, circleRadius);
	    float[] dist = {0.0f, 0.70f, 0.95f};
	    Color transparentBlack = new Color(0, 0, 0, 0);
		Color[] colors = {transparentBlack, transparentBlack, Color.BLACK};
	    RadialGradientPaint p =
	        new RadialGradientPaint(center, circleRadius, focus,
	                                 dist, colors,
	                                 CycleMethod.NO_CYCLE);
	    
	    ga.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    ga.setPaint(p);
		
		ga.fill(circle);
		
		ga.dispose();
		
		return image;
	}
}
