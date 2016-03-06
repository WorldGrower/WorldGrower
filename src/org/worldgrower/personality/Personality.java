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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.worldgrower.WorldObject;

/**
 * A Personality describes the personality traits of an NPC.
 */
public class Personality implements Serializable {

	private final List<PersonalityTraitValue> personalityTraitValues = new ArrayList<>();
	
	public Personality() {
		super();
		for(PersonalityTrait personalityTrait : PersonalityTrait.ALL_TRAITS) {
			personalityTraitValues.add(new PersonalityTraitValue(personalityTrait));
		}
	}
	
	public void initialize(WorldObject performer) {
		for(PersonalityTraitValue personalityTraitValue : personalityTraitValues) {
			personalityTraitValue.initialize(performer);
		}
	}

	public void changeValue(PersonalityTrait personalityTrait, int value, String reason) {
		getPersonalityTraitValue(personalityTrait).changeValue(value, reason);
	}
	
	private PersonalityTraitValue getPersonalityTraitValue(PersonalityTrait personalityTrait) {
		for(PersonalityTraitValue personalityTraitValue : personalityTraitValues) {
			if (personalityTraitValue.getPersonalityTrait() == personalityTrait) {
				return personalityTraitValue;
			}
		}
		throw new IllegalStateException("PersonalityTrait " + personalityTrait + " not found in " + personalityTraitValues);
	}

	public int getValue(PersonalityTrait personalityTrait) {
		return getPersonalityTraitValue(personalityTrait).getValue();
	}

	public Personality copy() {
		Personality copyPersonality = new Personality();
		for(PersonalityTraitValue personalityTraitValue : copyPersonality.personalityTraitValues) {
			personalityTraitValue.copy(getPersonalityTraitValue(personalityTraitValue.getPersonalityTrait()));
		}
		return copyPersonality;
	}
	
	public PersonalityTrait getMostExtremePersonalityTrait() {
		PersonalityTraitValue mostExtremePersonalityTraitValue = personalityTraitValues.get(0);
		
		for(PersonalityTraitValue personalityTraitValue : personalityTraitValues) {
			if (Math.abs(personalityTraitValue.getValue()) > mostExtremePersonalityTraitValue.getValue()) {
				mostExtremePersonalityTraitValue = personalityTraitValue;
			}
		}
		return mostExtremePersonalityTraitValue.getPersonalityTrait();
	}	
}
