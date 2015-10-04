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

import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;

public interface Background extends IdContainer {

	public<T> T chooseValue(WorldObject performer, ManagedProperty<T> property, World world);
	public List<Goal> getPersonalGoals(WorldObject performer, World world);
	
	public void addGoalObstructed(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world);
	public List<String> getAngryReasons(boolean firstPerson, int personTalkingId, WorldObject performer, World world);
	public WorldObject getRevengeTarget(World world);
	public void checkForNewGoals(WorldObject performer, World world);
	public void remove(int id);
	public Background copy();
}
