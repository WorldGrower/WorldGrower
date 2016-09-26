/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.conversation;

import org.worldgrower.gui.ImageIds;

public enum ConversationCategory {
	PERSONAL_INFORMATION("Personal information", ImageIds.GREY_RING),
	RELATIONSHIP_OTHERS("Relationship with others", ImageIds.GREY_CIRCLE),
	DEMAND("Demand", ImageIds.SILVER_COIN),
	DIPLOMACY_TARGET("Diplomacy", ImageIds.HEART),
	INTIMIDATE_TARGET("Intimidate", ImageIds.IRON_GREATAXE),
	GROUP("Organization", ImageIds.BLACK_CROSS),
	DEITY("Deity", ImageIds.WORSHIP),
	LEADER("Leader", ImageIds.GOLD_SHIELD), 
	SHARE_KNOWLEDGE("Share knowledge", ImageIds.PAPER),
	ARENA("Arena", ImageIds.IRON_AXE),
	REQUEST_ACTION("Request action", ImageIds.SKELETON);
	
	private final String description;
	private final ImageIds imageId;
	
	ConversationCategory(String description, ImageIds imageId) {
		this.description = description;
		this.imageId = imageId;
	}
	
	public String getDescription() {
		return description;
	}

	public ImageIds getImageId() {
		return imageId;
	}
}
