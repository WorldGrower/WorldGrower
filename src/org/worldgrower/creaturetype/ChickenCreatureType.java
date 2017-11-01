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
package org.worldgrower.creaturetype;

import java.io.ObjectStreamException;
import java.util.List;

import org.worldgrower.WorldObject;
import org.worldgrower.goal.CattlePropertyUtils;
import org.worldgrower.gui.music.SoundIds;

public class ChickenCreatureType implements CattleCreatureType {

	public ChickenCreatureType(List<CreatureType> allCreatureTypes) {
		allCreatureTypes.add(this);
	}

	@Override
	public boolean canTalk() {
		return false;
	}

	@Override
	public boolean canMove() {
		return true;
	}
	
	@Override
	public boolean canTrade() {
		return false;
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public boolean isCattle() {
		return true;
	}
	
	@Override
	public String getDescription() {
		return "chicken";
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.CHICKEN;
	}
	
	@Override
	public boolean canHaveOffSpring(WorldObject performer, WorldObject w) {
		return CattlePropertyUtils.isOldEnoughToReproduce(performer)
				&& CattlePropertyUtils.isOldEnoughToReproduce(w);
	}
}
