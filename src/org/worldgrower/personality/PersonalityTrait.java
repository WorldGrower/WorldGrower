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
package org.worldgrower.personality;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.WorldObject;

/**
 * A PersonalityTrait indicates how an NPC goes about achieving its goal.
 */
public interface PersonalityTrait extends Serializable {

	public static final List<PersonalityTrait> ALL_TRAITS = new ArrayList<>();
	
	public String getAdjective(int value, int relationshipValue);
	public int calculateInitialValue(WorldObject performer);
	public String getDescription();
	
	public default Object readResolveImpl() throws ObjectStreamException {
		Class<?> clazz = getClass();
		List<PersonalityTrait> allPersonalityTraits = PersonalityTrait.ALL_TRAITS;
		
		for(PersonalityTrait personalityTrait : allPersonalityTraits) {
			if (personalityTrait.getClass() == clazz) {
				return personalityTrait;
			}
		}
		
		throw new IllegalStateException("Profession with class " + clazz + " not found");
	}
	
	public static final PersonalityTrait GREEDY = new GreedyTrait(ALL_TRAITS);
	public static final PersonalityTrait POWER_HUNGRY = new PowerHungryTrait(ALL_TRAITS);
	public static final PersonalityTrait HONORABLE = new HonorableTrait(ALL_TRAITS);
	
	//TODO: implement
	public static final PersonalityTrait FORGIVING = new ForgivingTrait(ALL_TRAITS);
}
