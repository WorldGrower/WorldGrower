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
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IntProperty;

public class BonusDescriptions {

	private final List<BonusDescription> bonusDescriptions = 
			Arrays.asList(
					new BonusDescription(Constants.SLEEP_COMFORT, "sleep"),
					new BonusDescription(Constants.SMITH_QUALITY, "smithing"),
					new BonusDescription(Constants.WORKBENCH_QUALITY, "carpentry"),
					new BonusDescription(Constants.PAPER_MILL_QUALITY, "lumbering"),
					new BonusDescription(Constants.WEAVERY_QUALITY, "weaving"),
					new BonusDescription(Constants.BREWERY_QUALITY, "brewing"),
					new BonusDescription(Constants.APOTHECARY_QUALITY, "apothecary")
					
			);
	
	public String getWorldObjectDescription(WorldObject worldObject) {
		String name = worldObject.getProperty(Constants.NAME);
		
		for (BonusDescription bonusDescription : bonusDescriptions) {
			if (bonusDescription.getProductionBonusDescription(worldObject) != null) {
				return name + bonusDescription.getProductionBonusDescription(worldObject);
			}
		}

		return name;
	}
	
	private static class BonusDescription {
		private final IntProperty intProperty;
		private final String description;

		public BonusDescription(IntProperty intProperty, String description) {
			super();
			this.intProperty = intProperty;
			this.description = description;
		}
		
		public String getProductionBonusDescription(WorldObject worldObject) {
			if (worldObject.hasProperty(intProperty)) {
				int bonus = worldObject.getProperty(intProperty);
				return " (" + description + " bonus: " + bonus + ")";
			} else {
				return null;
			}
		}
	}
}
