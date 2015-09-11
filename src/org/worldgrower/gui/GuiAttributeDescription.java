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

import java.util.List;

import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;

public class GuiAttributeDescription {

	public static String createToolTipDescription(IntProperty attributeProperty, String description) {
		List<SkillProperty> skillProperties = SkillUtils.getSkillsForAttribute(attributeProperty);
		StringBuilder tooltipBuilder = new StringBuilder();
		tooltipBuilder.append(description).append(" governs ");
		if (skillProperties.size() > 0) {
			for(int i=0; i<skillProperties.size(); i++) {
				tooltipBuilder.append(skillProperties.get(i).getName());
				if (i < skillProperties.size() - 1) {
					tooltipBuilder.append(", ");
				}
			}
		} else {
			tooltipBuilder.append(" no skill");
		}
		return tooltipBuilder.toString();
	}
}
