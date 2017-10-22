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
package org.worldgrower.gui.start;

public enum GuiAction {
	SHOW_CHARACTER_SHEET("Show character sheet", 'C'),
	SHOW_INVENTORY("Show inventory", 'I'),
	SHOW_MAGIC_OVERVIEW("Show magic overview", 'M'),
	REST_ACTION("Rest action", 'R'),
	SHOW_STATUS_MESSAGES("Show status messages", 'S'),
	ASSIGN_ACTION_TO_LEFT_MOUSE("Assign action to left mouse", 'A'),
	SHOW_CHARACTER_ACTIONS("Show character actions", 'W'),
	COMMUNITY_OVERVIEW("Show community overview", 'U'),
	SHOW_GOVERNANCE("Show governance overview", 'G'),
	SHOW_BUILDINGS("Show Buildings", 'B'),
	SHOW_PRODUCTION_BUILDINGS("Show Production Buildings", 'P'),
	SHOW_PLANTS("Show Plants", 'L'),
	;
	
	private final String description;
	private final char defaultValue;

	private GuiAction(String description, char defaultValue) {
		this.description = description;
		this.defaultValue = defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public char getDefaultValue() {
		return defaultValue;
	}
}
