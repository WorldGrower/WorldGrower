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
import org.worldgrower.attribute.IdProperty;
import org.worldgrower.attribute.IntProperty;

public class BonusDescriptions {

	private final List<TooltipFormatter> tooltipFormatters = 
			Arrays.asList(
					new BonusDescription(Constants.SLEEP_COMFORT, "sleep"),
					new BonusDescription(Constants.SMITH_QUALITY, "smithing"),
					new BonusDescription(Constants.WORKBENCH_QUALITY, "carpentry"),
					new BonusDescription(Constants.PAPER_MILL_QUALITY, "lumbering"),
					new BonusDescription(Constants.WEAVERY_QUALITY, "weaving"),
					new BonusDescription(Constants.BREWERY_QUALITY, "brewing"),
					new BonusDescription(Constants.APOTHECARY_QUALITY, "apothecary"),
					new OwnerDescription(Constants.CATTLE_OWNER_ID, Constants.MEAT_SOURCE)
					
			);
	
	public String getWorldObjectDescription(WorldObject worldObject, World world) {
		String name = worldObject.getProperty(Constants.NAME);
		
		for (TooltipFormatter tooltipFormatter : tooltipFormatters) {
			if (tooltipFormatter.getToolTip(worldObject, world) != null) {
				return name + tooltipFormatter.getToolTip(worldObject, world);
			}
		}

		return name;
	}
	
	private static interface TooltipFormatter {
		public String getToolTip(WorldObject worldObject, World world);
	}
	
	private static class BonusDescription implements TooltipFormatter {
		private final IntProperty intProperty;
		private final String description;

		public BonusDescription(IntProperty intProperty, String description) {
			super();
			this.intProperty = intProperty;
			this.description = description;
		}
		
		@Override
		public String getToolTip(WorldObject worldObject, World world) {
			if (worldObject.hasProperty(intProperty)) {
				int bonus = worldObject.getProperty(intProperty);
				return " (" + description + " bonus: " + bonus + ")";
			} else {
				return null;
			}
		}
	}
	
	private static class OwnerDescription implements TooltipFormatter {
		private final IdProperty idProperty;
		private final IntProperty intProperty;

		public OwnerDescription(IdProperty idProperty, IntProperty intProperty) {
			super();
			this.idProperty = idProperty;
			this.intProperty = intProperty;
		}
		
		@Override
		public String getToolTip(WorldObject worldObject, World world) {
			if (worldObject.hasProperty(idProperty)) {
				int id = worldObject.getProperty(idProperty);
				WorldObject owner = world.findWorldObjectById(id);
				return " (owner: " + owner.getProperty(Constants.NAME) + ", " + generateIntPropertyDescription(worldObject) + ")";
			} else if (worldObject.hasProperty(intProperty)) {
				return "(" + generateIntPropertyDescription(worldObject) + ")";
			} else {
				return null;
			}
		}
		
		private String generateIntPropertyDescription(WorldObject worldObject) {
			return intProperty.getName() + ": " + worldObject.getProperty(intProperty);
		}
	}
}
