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

/**
 * Each time a property is created, it gets assigned a new ordinal.
 * The ordinal can thought of as a unique id related to each property.
 */
class OrdinalGenerator {

	private static int currentOrdinal;
	
	public static int getNextOrdinal() {
		return currentOrdinal++;
	}
	
	public static int getNumberOfProperties() {
		return currentOrdinal;
	}
}
