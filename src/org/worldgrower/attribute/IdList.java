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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class IdList implements Serializable {

	private final IntList ids = new IntArrayList();
	
	public IdList add(int id) {
		ids.add(id);
		return this;
	}
	
	public IdList add(WorldObject worldObject) {
		ids.add(worldObject.getProperty(Constants.ID).intValue());
		return this;
	}
	
	public IdList addAll(IdList idList) {
		ids.addAll(idList.ids);
		return this;
	}
	
	public void remove(WorldObject worldObject) {
		ids.rem(worldObject.getProperty(Constants.ID).intValue());
	}
	
	public void removeAll(List<Integer> idsToRemove) {
		ids.removeAll(idsToRemove);
	}
	
	public void removeAll() {
		ids.clear();
	}
	
	public void remove(int id) {
		ids.rem(id);
	}

	public boolean contains(WorldObject worldObject) {
		return (ids.indexOf(worldObject.getProperty(Constants.ID).intValue()) != -1);
	}

	public boolean contains(int id) {
		return ids.indexOf(id) != -1;
	}
	
	public boolean intersects(IdList otherIdList) {
		IntList copyIds = new IntArrayList(this.ids);
		copyIds.removeAll(otherIdList.ids);
		
		return (copyIds.size() != this.ids.size());
	}
	
	public List<Integer> getIdsNotPresentInOther(IdList otherIdList) {
		List<Integer> copyIds = new ArrayList<>(this.ids);
		copyIds.removeAll(otherIdList.ids);
		
		return copyIds;
	}
	
	@Override
	public String toString() {
		return "[" + ids + "]";
	}
	
	public List<Integer> getIds() {
		return Collections.unmodifiableList(ids);
	}
	
	public List<WorldObject> mapToWorldObjects(World world) {
		List<WorldObject> worldObjects = new ArrayList<>(ids.size());
		for(int id : ids) {
			worldObjects.add(world.findWorldObjectById(id));
		}
		
		return worldObjects;
	}
	
	public List<WorldObject> mapToWorldObjects(World world, Function<WorldObject, Boolean> testFunction) {
		List<WorldObject> worldObjects = new ArrayList<>(ids.size());
		for(int id : ids) {
			WorldObject worldObject = world.findWorldObjectById(id);
			if (testFunction.apply(worldObject).booleanValue()) {
				worldObjects.add(worldObject);
			}
		}
		
		return worldObjects;
	}
	
	public IdList copy() {
		IdList idList = new IdList();
		idList.ids.addAll(this.ids);
		return idList;
	}

	public int size() {
		return ids.size();
	}
}
