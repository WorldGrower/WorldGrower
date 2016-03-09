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

import org.worldgrower.profession.Profession;

public class ProfessionExplanation {
	private final Profession profession;
	private final String explanation;
	
	public ProfessionExplanation(Profession profession, String explanation) {
		super();
		this.profession = profession;
		this.explanation = explanation;
	}

	public Profession getProfession() {
		return profession;
	}

	public String getExplanation() {
		return explanation;
	}
}
