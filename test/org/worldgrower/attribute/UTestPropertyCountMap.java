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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;

public class UTestPropertyCountMap {

	@Test
	public void testAdd() {
		PropertyCountMap<ManagedProperty<?>> propertyCountMap = new PropertyCountMap<ManagedProperty<?>>();
		propertyCountMap.add(Constants.GOLD, 5);
		
		assertEquals(5, propertyCountMap.count(Constants.GOLD));
		assertEquals(0, propertyCountMap.count(Constants.HIT_POINTS));
	}
	
	@Test
	public void testRemove() {
		PropertyCountMap<ManagedProperty<?>> propertyCountMap = new PropertyCountMap<ManagedProperty<?>>();
		propertyCountMap.add(Constants.GOLD, 5);
		propertyCountMap.remove(Constants.GOLD);
		
		assertEquals(0, propertyCountMap.count(Constants.GOLD));
	}
	
	@Test
	public void testAddAll() {
		PropertyCountMap<ManagedProperty<?>> propertyCountMap1 = new PropertyCountMap<ManagedProperty<?>>();
		propertyCountMap1.add(Constants.GOLD, 5);
		
		PropertyCountMap<ManagedProperty<?>> propertyCountMap2 = new PropertyCountMap<ManagedProperty<?>>();
		propertyCountMap2.add(Constants.GOLD, 3);
		propertyCountMap2.add(Constants.HIT_POINTS, 2);
		
		propertyCountMap1.addAll(propertyCountMap2);
		
		assertEquals(8, propertyCountMap1.count(Constants.GOLD));
		assertEquals(2, propertyCountMap1.count(Constants.HIT_POINTS));
	}
}

