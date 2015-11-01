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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldOnTurnImpl implements WorldOnTurn {

	private final List<WorldOnTurn> worldOnTurnList = new ArrayList<>();
	
	public WorldOnTurnImpl(WorldOnTurn... worldOnTurns) {
		worldOnTurnList.addAll(Arrays.asList(worldOnTurns));
	}
	
	@Override
	public void onTurn(World world) {
		for(WorldOnTurn worldOnTurn : worldOnTurnList) {
			worldOnTurn.onTurn(world);
		}
	}
}
