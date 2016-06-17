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
package org.worldgrower;

public class SaveGameStatistics {
	private final String playerCharacterName;
	private final int playerCharacterLevel;
	private final int turn;
	
	
	public SaveGameStatistics(String playerCharacterName, int playerCharacterLevel, int turn) {
		super();
		this.playerCharacterName = playerCharacterName;
		this.playerCharacterLevel = playerCharacterLevel;
		this.turn = turn;
	}


	public String getPlayerCharacterName() {
		return playerCharacterName;
	}


	public int getPlayerCharacterLevel() {
		return playerCharacterLevel;
	}


	public int getTurn() {
		return turn;
	}
}
