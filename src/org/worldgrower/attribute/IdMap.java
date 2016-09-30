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

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.worldgrower.World;
import org.worldgrower.WorldObject;

public interface IdMap extends Serializable, IdContainer {

	public void incrementValue(int id, int value);
	public void incrementValue(WorldObject worldObject, int value);
	public int getValue(int id);
	public int getValue(WorldObject worldObject);
	public int getSumOfAllValues();
	
	public int findBestId(Predicate<WorldObject> predicate, World world);
	public int findBestId(Predicate<WorldObject> predicate, Comparator<WorldObject> comparator,  World world);
	public int findWorstId(World world);
	public List<Integer> getIds();
	public List<Integer> getIdsWithoutTarget(WorldObject target);
	public boolean contains(WorldObject worldObject);
	
	public String toString();
	
	public IdMap copy();

	public void remove(int id);
	public void remove(WorldObject worldObject);
}
