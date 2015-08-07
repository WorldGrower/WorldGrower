/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.conversation;

public enum ConversationCategory {
	PERSONAL_INFORMATION("Personal information"),
	RELATIONSHIP_OTHERS("Relationship with others"),
	DEMAND("Demand"),
	DIPLOMACY_TARGET("Diplomacy"),
	INTIMIDATE_TARGET("Intimidate"),
	GROUP("Organization"),
	DEITY("Deity"),
	LEADER("Leader");
	
	private final String description;
	
	ConversationCategory(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
