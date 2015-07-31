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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class IdList implements Serializable {

	private final List<Integer> ids = new ArrayList<>();
	
	public IdList add(int id) {
		ids.add(id);
		return this;
	}
	
	public IdList add(WorldObject worldObject) {
		ids.add(worldObject.getProperty(Constants.ID));
		return this;
	}
	
	public void remove(WorldObject worldObject) {
		ids.remove(worldObject.getProperty(Constants.ID));
	}
	
	public void removeAll(List<Integer> idsToRemove) {
		ids.removeAll(idsToRemove);
	}
	
	public void remove(int id) {
		ids.remove(Integer.valueOf(id));
	}

	public boolean contains(WorldObject worldObject) {
		return ids.contains(worldObject.getProperty(Constants.ID));
	}
	
	public boolean intersects(IdList otherIdList) {
		List<Integer> copyIds = new ArrayList<>(this.ids);
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
	
	public IdList copy() {
		IdList idList = new IdList();
		idList.ids.addAll(this.ids);
		return idList;
	}

	public int size() {
		return ids.size();
	}
}
