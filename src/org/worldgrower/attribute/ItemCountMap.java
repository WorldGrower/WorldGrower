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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;

public class ItemCountMap implements Serializable {

	private final Map<Item, Integer> itemCount = new HashMap<>();
	
	public void add(WorldObject worldObject) {
		Item item = worldObject.getProperty(Constants.ITEM_ID);
		if (item == null) {
			throw new IllegalStateException("item is null for WorldObject " + worldObject);
		}
		add(item, 1);
	}
	
	public void add(Item item, int quantity) {
		if (item == null) {
			throw new IllegalStateException("item is null");
		}
		
		if (!itemCount.containsKey(item)) {
			itemCount.put(item, quantity);
		} else {
			itemCount.put(item, itemCount.get(item) + quantity);
		}
	}
	
	public List<Item> getItems() {
		return new ArrayList<>(itemCount.keySet());
	}
	
	public void clear() {
		itemCount.clear();
	}

	public int get(Item item) {
		return itemCount.get(item);
	}

	public boolean contains(Item item) {
		return itemCount.containsKey(item);
	}

	public boolean isEmpty() {
		return itemCount.size() == 0;
	}
	
	@Override
	public String toString() {
		return itemCount.toString();
	}

	public boolean containsAny(List<Item> items) {
		for(Item item : items) {
			if (itemCount.containsKey(item)) {
				return true;
			}
		}
		return false;
	}
}
