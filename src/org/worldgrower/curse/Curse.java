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
package org.worldgrower.curse;

import java.io.Serializable;
import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;

public interface Curse extends Serializable {

	public List<Goal> getCurseGoals(List<Goal> normalGoals);
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world);
	public boolean canMove();
	public String getExplanation();
	
	public static final SirenCurse SIREN_CURSE = new SirenCurse();
	public static final ToadCurse TOAD_CURSE = new ToadCurse();
	public static final VampireCurse VAMPIRE_CURSE = new VampireCurse();
}
