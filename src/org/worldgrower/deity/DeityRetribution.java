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
package org.worldgrower.deity;

import java.util.HashMap;
import java.util.Map;

import org.worldgrower.World;

public class DeityRetribution {

	private static final Map<Deity, Integer> RETRIBUTION_CHECK_TURN_MAP = new HashMap<>();
	
	static {
		for(Deity deity : Deity.ALL_DEITIES) {
			RETRIBUTION_CHECK_TURN_MAP.put(deity, calculateTurnRecurrence(deity));
		}
	}

	static int calculateTurnRecurrence(Deity deity) {
		return 4000 + (getOffset(deity) % 100 - 50);
	}
	
	public static boolean shouldCheckForDeityRetribution(Deity deity, World world) {
		int currentTurn = world.getCurrentTurn().getValue();
		int turnRecurrence = RETRIBUTION_CHECK_TURN_MAP.get(deity);
		return (currentTurn % turnRecurrence == 0); 
	}

	private static int getOffset(Deity deity) {
		int offset = 0;
		for(char c : deity.getName().toCharArray()) {
			offset += c;
		}
		return offset;
	}
}
