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

import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.attribute.SkillProperty;

public class SkillImageIds {

	private Map<SkillProperty, ImageIds> imageIdsMap = new HashMap<>();
	
	public SkillImageIds() {
		imageIdsMap.put(Constants.ILLUSION_SKILL, ImageIds.DISGUISE_MAGIC_SPELL);
		imageIdsMap.put(Constants.RESTORATION_SKILL, ImageIds.MINOR_HEAL);
		imageIdsMap.put(Constants.TRANSMUTATION_SKILL, ImageIds.ENLARGE_MAGIC_SPELL);
		imageIdsMap.put(Constants.EVOCATION_SKILL, ImageIds.FIRE_BOLT);
		imageIdsMap.put(Constants.NECROMANCY_SKILL, ImageIds.LICH);
		imageIdsMap.put(Constants.ENCHANTMENT_SKILL, ImageIds.FEAR_INDICATOR);
	}
	
	public ImageIds getImageFor(SkillProperty skillProperty) {
		ImageIds imageId = imageIdsMap.get(skillProperty);
		if (imageId == null) {
			throw new IllegalStateException("No image found for skill " + skillProperty);
		}
		return imageId;
	}
}
