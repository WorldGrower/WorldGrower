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
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.generator.Item;

public class Prices implements Serializable {
	private Map<Item, Integer> prices = new HashMap<>();
	
	public void setPrice(Item item, int price) {
		if (price != item.getPrice()) {
			prices.put(item, price);
		} else {
			prices.remove(item);
		}
	}
	
	public int getPrice(Item item) {
		Integer modifiedPrice = prices.get(item);
		if (modifiedPrice != null) {
			return modifiedPrice.intValue();
		} else {
			return item.getPrice();
		}
	}

	public Prices copy() {
		Prices copy = new Prices();
		copy.prices = new HashMap<>(prices);
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Prices) {
			Prices other = (Prices) obj;
			return this.prices.equals(other.prices);
		} else {
			return false;
		}
	}
	
	public int[] toArgs() {
		int[] args = new int[Item.values().length];
		for(int i=0; i<args.length; i++) {
			Item item = Item.value(i);
			args[i] = getPrice(item);
		}
		return args;
	}
	
	public void setPrices(int[] args) {
		int itemCount = Item.values().length;
		for(int i=0; i<itemCount; i++) {
			Item item = Item.value(i);
			setPrice(item, args[i]);
		}
	}
}
