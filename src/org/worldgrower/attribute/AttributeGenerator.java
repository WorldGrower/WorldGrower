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
package org.worldgrower.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.worldgrower.Constants;

public class AttributeGenerator {

	private static final List<IntProperty> ATTRIBUTES = Arrays.asList(
			Constants.STRENGTH, 
			Constants.DEXTERITY, 
			Constants.CONSTITUTION, 
			Constants.INTELLIGENCE, 
			Constants.WISDOM, 
			Constants.CHARISMA
			);
	
	private final Random random;
	
	public AttributeGenerator(Random random) {
		this.random = random;
	}

	public void addCommonerAttributes(Map<ManagedProperty<?>, Object> properties) {
		int bonusAttributePoints = 6;
		List<Integer> attributes = new ArrayList<>();
		bonusAttributePoints = generateAttributeValues(bonusAttributePoints, attributes);
		addRemainingAtrributePointsToHighestAttribute(bonusAttributePoints, attributes);
		putAttributesIntoMap(properties, attributes);
	}

	private int generateAttributeValues(int bonusAttributePoints, List<Integer> attributes) {
		for(IntProperty attributeProperty : ATTRIBUTES) {
			int attributeOffset = random.nextInt(9); // 0-8
			if (attributeOffset == 5) {
				attributeOffset = 1;
			}
			if (attributeOffset == 7 || attributeOffset == 8) {
				attributeOffset = 2;
			}
			if (attributeOffset == 6) {
				attributeOffset = 3;
			}
			int attribute = attributeOffset + 8; // 8, 9, 10, 11 or 12
			
			if (attribute >= 11 && bonusAttributePoints >= 2) {
				attribute += 2;
				bonusAttributePoints -= 2;
			}
			attributes.add(attribute);
		}
		return bonusAttributePoints;
	}

	private void addRemainingAtrributePointsToHighestAttribute(int bonusAttributePoints, List<Integer> attributes) {
		int highestAttribute = Integer.MIN_VALUE;
		int highestAttributeIndex = -1;
		for(int i=0; i<ATTRIBUTES.size(); i++) {
			IntProperty attributeProperty = ATTRIBUTES.get(i);
			int attribute = attributes.get(i);
			if (attribute > highestAttribute) {
				highestAttribute = attribute;
				highestAttributeIndex = i;
			}
		}
		
		attributes.set(highestAttributeIndex, attributes.get(highestAttributeIndex) + bonusAttributePoints);
		bonusAttributePoints = 0;
	}

	private void putAttributesIntoMap(Map<ManagedProperty<?>, Object> properties, List<Integer> attributes) {
		for(int i=0; i<ATTRIBUTES.size(); i++) {
			IntProperty attributeProperty = ATTRIBUTES.get(i);
			int attribute = attributes.get(i);
			properties.put(attributeProperty, attribute);
		}
	}
}
