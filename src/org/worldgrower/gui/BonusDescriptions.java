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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BooleanProperty;
import org.worldgrower.attribute.IdProperty;
import org.worldgrower.attribute.IntProperty;

public class BonusDescriptions {

	private final List<TooltipFormatter> tooltipFormatters = 
			Arrays.asList(
					new BonusDescription(Constants.SLEEP_COMFORT, "sleep", Constants.SELLABLE, ImageIds.SLEEPING_INDICATOR),
					new BonusDescription(Constants.SMITH_QUALITY, "smithing", Constants.SELLABLE, ImageIds.REPAIR_HAMMER),
					new BonusDescription(Constants.WORKBENCH_QUALITY, "carpentry", Constants.SELLABLE, ImageIds.SAW),
					new BonusDescription(Constants.PAPER_MILL_QUALITY, "lumbering", Constants.SELLABLE, ImageIds.IRON_AXE),
					new BonusDescription(Constants.WEAVERY_QUALITY, "weaving", Constants.SELLABLE, ImageIds.COTTON_HAT),
					new BonusDescription(Constants.BREWERY_QUALITY, "brewing", Constants.SELLABLE, ImageIds.WINE),
					new BonusDescription(Constants.APOTHECARY_QUALITY, "apothecary", Constants.SELLABLE, ImageIds.POISON),
					new OwnerDescription(Constants.CATTLE_OWNER_ID, Constants.MEAT_SOURCE, ImageIds.MEAT),
					new BonusDescription(Constants.LIBRARY_QUALITY, "research", Constants.SELLABLE, ImageIds.SPELL_BOOK)
					
			);
	
	public String getWorldObjectDescription(WorldObject worldObject, SmallImageTagFactory smallImageTagFactory, World world) {
		String name = worldObject.getProperty(Constants.NAME);
		
		for (TooltipFormatter tooltipFormatter : tooltipFormatters) {
			String toolTip = tooltipFormatter.getToolTip(worldObject, smallImageTagFactory, world);
			if (toolTip != null) {
				return "<html>" + name + "<br>" + toolTip + "</html>";
			}
		}

		return name;
	}
	
	private static interface TooltipFormatter {
		public String getToolTip(WorldObject worldObject, SmallImageTagFactory smallImageTagFactory, World world);
	}
	
	private static class BonusDescription implements TooltipFormatter {
		private final IntProperty intProperty;
		private final String description;
		private final BooleanProperty booleanProperty;
		private final ImageIds imageId;

		public BonusDescription(IntProperty intProperty, String description, BooleanProperty booleanProperty, ImageIds imageId) {
			super();
			this.intProperty = intProperty;
			this.description = description;
			this.booleanProperty = booleanProperty;
			this.imageId = imageId;
		}
		
		@Override
		public String getToolTip(WorldObject worldObject, SmallImageTagFactory smallImageTagFactory, World world) {
			if (worldObject.hasProperty(intProperty)) {
				int bonus = worldObject.getProperty(intProperty);
				boolean isBooleanFlagSet = worldObject.hasProperty(booleanProperty) && worldObject.getProperty(booleanProperty);
				String booleanFlagDescription = (isBooleanFlagSet ? booleanProperty.getName() + "</td><td> yes" : "");
				return " <table><tr><td>" + description + " bonus </td><td>" + bonus + " " + smallImageTagFactory.smallImageTag(imageId) + "</td></tr><tr><td>"+ booleanFlagDescription + "</td></table>";
			} else {
				return null;
			}
		}
	}
	
	private static class OwnerDescription implements TooltipFormatter {
		private final IdProperty idProperty;
		private final IntProperty intProperty;
		private final ImageIds imageId;

		public OwnerDescription(IdProperty idProperty, IntProperty intProperty, ImageIds imageId) {
			super();
			this.idProperty = idProperty;
			this.intProperty = intProperty;
			this.imageId = imageId;
		}
		
		@Override
		public String getToolTip(WorldObject worldObject, SmallImageTagFactory smallImageTagFactory, World world) {
			if (worldObject.hasProperty(idProperty) && worldObject.getProperty(idProperty) != null) {
				int id = worldObject.getProperty(idProperty);
				WorldObject owner = world.findWorldObjectById(id);
				return "<table><tr><td>owner</td><td>" + owner.getProperty(Constants.NAME) + "</td></tr><tr><td>" + generateIntPropertyDescription(worldObject, smallImageTagFactory) + "</td></tr></table>";
			} else if (worldObject.hasProperty(intProperty)) {
				return "<table><tr><td>" + generateIntPropertyDescription(worldObject, smallImageTagFactory) + "</td></tr></table>";
			} else {
				return null;
			}
		}
		
		private String generateIntPropertyDescription(WorldObject worldObject, SmallImageTagFactory smallImageTagFactory) {
			return intProperty.getName() + "</td><td>" + worldObject.getProperty(intProperty) + " " + smallImageTagFactory.smallImageTag(imageId);
		}
	}
}
