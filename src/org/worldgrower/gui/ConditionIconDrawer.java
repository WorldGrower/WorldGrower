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
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.condition.Condition;
import org.worldgrower.gui.util.ImageUtils;

public class ConditionIconDrawer {

	private static final int CONDITION_IMAGE_WIDTH = 12;
	private static final int CONDITION_IMAGE_HEIGHT = 12;
	
	private final Map<ImageIds, Image> conditionImages = new HashMap<>();
	
	public ConditionIconDrawer(ImageInfoReader imageInfoReader) {
		for(Condition condition : Condition.ALL_CONDITIONS) {
			Image overlayingImage = imageInfoReader.getImage(condition.getImageIds(), null);
			overlayingImage = ImageUtils.createResizedCopy(overlayingImage, CONDITION_IMAGE_WIDTH, CONDITION_IMAGE_HEIGHT, false);
			
			conditionImages.put(condition.getImageIds(), overlayingImage);
		}
	}
	
	public void drawConditions(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int worldObjectX, int worldObjectY) {
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		List<ImageIds> conditionImageIds = getConditionImageIds(worldObject);
		int imageIndex = 0;
		for(ImageIds conditionImageId : conditionImageIds) {
			Image conditionImage = conditionImages.get(conditionImageId);
			g.setColor(Color.BLACK);
			int overlayingImageX = calculateConditionX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateConditionY(worldObjectY, worldObjectHeight, imageIndex);
			drawImage(g, conditionImage, overlayingImageX, overlayingImageY);
			
			imageIndex++;
		}
	}

	void drawImage(Graphics g, Image image, int overlayingImageX, int overlayingImageY) {
		g.drawRect(overlayingImageX, overlayingImageY, CONDITION_IMAGE_WIDTH, CONDITION_IMAGE_HEIGHT);
		g.drawImage(image, overlayingImageX, overlayingImageY, null);
	}
	
    private List<ImageIds> getConditionImageIds(WorldObject worldObject) {
    	if (worldObject.hasProperty(Constants.CONDITIONS)) {
	    	return worldObject.getProperty(Constants.CONDITIONS).getImageIds(); 
    	} else {
    		return new ArrayList<>();
    	}
    }

	int calculateConditionX(int worldObjectX, int worldObjectWidth, int imageIndex) {
		return worldObjectX + worldObjectWidth - CONDITION_IMAGE_WIDTH - CONDITION_IMAGE_WIDTH * imageIndex - 1;
	}
    
	int calculateConditionY(int worldObjectY, int worldObjectHeight, int imageIndex) {
		return worldObjectY + worldObjectHeight - CONDITION_IMAGE_HEIGHT * (imageIndex / 4) - 1;
	}

	public String getConditionDescriptionFor(WorldObject worldObject, int panelX, int panelY, int offsetX, int offsetY) {
		List<String> conditionDescriptions = getConditionDescriptions(worldObject);
		int worldObjectX = (worldObject.getProperty(Constants.X)+offsetX) * 48;
		int worldObjectY = (worldObject.getProperty(Constants.Y)+offsetY) * 48;
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		int imageIndex = 0;
		for(String conditionDescription : conditionDescriptions) {
			int overlayingImageX = calculateConditionX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateConditionY(worldObjectY, worldObjectHeight, imageIndex);

			if (isInOverlayingX(panelX, overlayingImageX)) {
				if (isInOverlayingY(panelY, overlayingImageY)) {
					return conditionDescription;
				}
			}
			
			imageIndex++;
		}
		return null;
	}

	boolean isInOverlayingY(int panelY, int overlayingImageY) {
		return overlayingImageY <= panelY && panelY <= overlayingImageY + CONDITION_IMAGE_HEIGHT;
	}

	boolean isInOverlayingX(int panelX, int overlayingImageX) {
		return overlayingImageX <= panelX && panelX <= overlayingImageX + CONDITION_IMAGE_WIDTH;
	}
	
    private List<String> getConditionDescriptions(WorldObject worldObject) {
    	if (worldObject.hasProperty(Constants.CONDITIONS)) {
	    	return worldObject.getProperty(Constants.CONDITIONS).getDescriptions(); 
    	} else {
    		return new ArrayList<>();
    	}
    }
}
