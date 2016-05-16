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

public class SpecialAttribute {
	private final String description;
	private final int currentValue;
	private final int maxValue;
	private final String longDescription;
	
	public SpecialAttribute(String description, int currentValue, int maxValue, String longDescription) {
		super();
		this.description = description;
		this.currentValue = currentValue;
		this.maxValue = maxValue;
		this.longDescription = longDescription;
	}

	public String getDescription() {
		return description;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public String getLongDescription() {
		return longDescription;
	}
	
	
}
