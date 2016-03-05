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
import java.util.List;

import org.worldgrower.WorldObject;

public class ForgivingTrait implements PersonalityTrait {

	public ForgivingTrait(List<PersonalityTrait> allTraits) {
		allTraits.add(this);
	}

	@Override
	public String getAdjective(int value, int relationshipValue) {
		if (value > 0) {
			if (relationshipValue < 0) {
				return "kind";
			} else if (relationshipValue < 250) {
				return "lenient";
			} else if (relationshipValue < 500) {
				return "accepting";
			} else {
				return "forgiving";
			}
		} else {
			if (relationshipValue < 0) {
				return "demanding";
			} else if (relationshipValue < 250) {
				return "harsh";
			} else if (relationshipValue < 500) {
				return "unkind";
			} else {
				return "severe";
			}
		}
	}

	@Override
	public int calculateInitialValue(WorldObject performer) {
		return 0;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}
