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
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.LookDirection;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.gui.util.ImageUtils;

public class BuySellIconsDrawer {

	private static final int BUY_SELL_IMAGE_WIDTH = 12;
	private static final int BUY_SELL_IMAGE_HEIGHT = 12;
	
	private final Map<Item, Image> itemImages = new HashMap<>();
	
	public BuySellIconsDrawer(ImageInfoReader imageInfoReader) {
		for(Item item : Item.values()) {
			Image overlayingImage = imageInfoReader.getImage(item.getImageId() , null);
			overlayingImage = ImageUtils.createResizedCopy(overlayingImage, BUY_SELL_IMAGE_WIDTH, BUY_SELL_IMAGE_HEIGHT, false);
			
			itemImages.put(item, overlayingImage);
		}
	}
	
	public void drawIcons(Graphics g, WorldObject worldObject, LookDirection lookDirection, Image image, int worldObjectX, int worldObjectY) {
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		List<Image> buyingImages = getBuyingImages(worldObject);
		int imageIndex = 0;
		for(Image buyingImage : buyingImages) {
			g.setColor(Color.BLACK);
			int overlayingImageX = calculateBuyingX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateBuyingY(worldObjectY, worldObjectHeight, imageIndex);
			drawImage(g, buyingImage, overlayingImageX, overlayingImageY);
			
			imageIndex++;
		}
		
		List<Image> sellingImages = getSellingImages(worldObject);
		imageIndex = 0;
		for(Image sellingImage : sellingImages) {
			g.setColor(Color.BLACK);
			int overlayingImageX = calculateSellingX(worldObjectX, worldObjectWidth, imageIndex);
			int overlayingImageY = calculateSellingY(worldObjectY, worldObjectHeight, imageIndex);
			drawImage(g, sellingImage, overlayingImageX, overlayingImageY);
			
			imageIndex++;
		}
	}

	void drawImage(Graphics g, Image image, int overlayingImageX, int overlayingImageY) {
		g.drawRect(overlayingImageX, overlayingImageY, BUY_SELL_IMAGE_WIDTH, BUY_SELL_IMAGE_HEIGHT);
		g.drawImage(image, overlayingImageX, overlayingImageY, null);
	}
	
    private List<Image> getBuyingImages(WorldObject worldObject) {
    	if (canAdvertiseBuying(worldObject)) {
    		List<Image> images = new ArrayList<>();
	    	for(ManagedProperty<?> managedProperty : BuySellUtils.getBuyingProperties(worldObject)) {
	    		Item item = Item.getItemFor(managedProperty);
	    		images.add(itemImages.get(item));
	    	}
			return images; 
    	} else {
    		return new ArrayList<>();
    	}
    }

	boolean canAdvertiseBuying(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.DEMANDS);
	}
    
    private List<String> getBuyingDescriptions(WorldObject worldObject) {
    	return BuySellUtils.getBuyingProperties(worldObject).stream().map(m -> m.getName()).collect(Collectors.toList());
    }
    
    private List<Image> getSellingImages(WorldObject worldObject) {
    	if (canAdvertiseSelling(worldObject)) {
	    	List<WorldObject> sellableWorldObjects = BuySellUtils.getSellableWorldObjects(worldObject);
			return sellableWorldObjects.stream().map(w -> itemImages.get(w.getProperty(Constants.ITEM_ID))).collect(Collectors.toList()); 
    	} else {
    		return new ArrayList<>();
    	}
    }

	boolean canAdvertiseSelling(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.INVENTORY);
	}
    
    private List<String> getSellingDescriptions(WorldObject worldObject) {
    	return BuySellUtils.getSellableWorldObjects(worldObject).stream().map(w -> w.getProperty(Constants.NAME)).collect(Collectors.toList());
    }

	int calculateBuyingX(int worldObjectX, int worldObjectWidth, int imageIndex) {
		return worldObjectX + BUY_SELL_IMAGE_WIDTH * imageIndex - 1;
	}
    
	int calculateBuyingY(int worldObjectY, int worldObjectHeight, int imageIndex) {
		return worldObjectY - BUY_SELL_IMAGE_HEIGHT * (imageIndex / 4) - 2;
	}
	
	int calculateSellingX(int worldObjectX, int worldObjectWidth, int imageIndex) {
		return worldObjectX + worldObjectWidth - BUY_SELL_IMAGE_WIDTH - BUY_SELL_IMAGE_WIDTH * imageIndex - 1;
	}
    
	int calculateSellingY(int worldObjectY, int worldObjectHeight, int imageIndex) {
		return worldObjectY - BUY_SELL_IMAGE_HEIGHT * (imageIndex / 4) - 2;
	}

	public String getItemDescriptionFor(WorldObject worldObject, int panelX, int panelY, int offsetX, int offsetY) {
		int worldObjectX = (worldObject.getProperty(Constants.X)+offsetX) * 48;
		int worldObjectY = (worldObject.getProperty(Constants.Y)+offsetY) * 48;
		int worldObjectWidth = worldObject.getProperty(Constants.WIDTH) * 48;
		int worldObjectHeight = worldObject.getProperty(Constants.HEIGHT) * 48;
		
		int imageIndex = 0;
		if (canAdvertiseBuying(worldObject)) {
			for(String buyingDescription : getBuyingDescriptions(worldObject)) {
				int overlayingImageX = calculateBuyingX(worldObjectX, worldObjectWidth, imageIndex);
				int overlayingImageY = calculateBuyingY(worldObjectY, worldObjectHeight, imageIndex);
	
				if (isInOverlayingImageX(panelX, overlayingImageX)) {
					if (isInOverlayingY(panelY, overlayingImageY)) {
						return "buys " + buyingDescription;
					}
				}
				
				imageIndex++;
			}
		}
		
		if (canAdvertiseSelling(worldObject)) {
			imageIndex = 0;
			for(String sellingDescription : getSellingDescriptions(worldObject)) {
				int overlayingImageX = calculateSellingX(worldObjectX, worldObjectWidth, imageIndex);
				int overlayingImageY = calculateSellingY(worldObjectY, worldObjectHeight, imageIndex);
				if (isInOverlayingImageX(panelX, overlayingImageX)) {
					if (isInOverlayingY(panelY, overlayingImageY)) {
						return "sells " + sellingDescription;
					}
				}
				imageIndex++;
			}
		}
		return null;
	}

	boolean isInOverlayingY(int panelY, int overlayingImageY) {
		return overlayingImageY <= panelY && panelY <= overlayingImageY + BUY_SELL_IMAGE_HEIGHT;
	}

	boolean isInOverlayingImageX(int panelX, int overlayingImageX) {
		return overlayingImageX <= panelX && panelX <= overlayingImageX + BUY_SELL_IMAGE_WIDTH;
	}	
}
