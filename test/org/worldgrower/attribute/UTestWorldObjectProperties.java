/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.worldgrower.Constants;

public class UTestWorldObjectProperties {

	@Test
	public void testGetPut() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		assertEquals("Test", worldObjectProperties.get(Constants.NAME));
		
		worldObjectProperties.put(Constants.NAME, "Test2");
		assertEquals("Test2", worldObjectProperties.get(Constants.NAME));
	}
	
	@Test
	public void testRemove() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		assertEquals("Test", worldObjectProperties.get(Constants.NAME));
		
		worldObjectProperties.remove(Constants.NAME);
		assertEquals(null, worldObjectProperties.get(Constants.NAME));
	}
	
	@Test
	public void testContainsKey() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		assertEquals(true, worldObjectProperties.containsKey(Constants.NAME));
		assertEquals(false, worldObjectProperties.containsKey(Constants.GOLD));
	}
	
	@Test
	public void testKeySet() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		assertEquals(Arrays.asList(Constants.NAME), worldObjectProperties.keySet());
	}
	
	@Test
	public void testToString() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		assertEquals("NAME=Test, ", worldObjectProperties.toString());
	}
	
	@Test
	public void testEntrySet() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, "Test");
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		
		List<Entry<ManagedProperty<?>, Object>> entrySet = worldObjectProperties.entrySet();
		assertEquals(1, entrySet.size());
		assertEquals(Constants.NAME, entrySet.get(0).getKey());
		assertEquals("Test", entrySet.get(0).getValue());	
	}
	
	@Test
	public void testShallowCopy() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		IdList originalGroup = new IdList().add(1);
		properties.put(Constants.GROUP, originalGroup);
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		WorldObjectProperties copyWorldObjectProperties = worldObjectProperties.shallowCopy();
		
		copyWorldObjectProperties.get(Constants.GROUP).add(2);
		assertEquals(2, originalGroup.size());
	}
	
	@Test
	public void testDeepCopy() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		IdList originalGroup = new IdList().add(1);
		properties.put(Constants.GROUP, originalGroup);
		WorldObjectProperties worldObjectProperties = new WorldObjectProperties(properties);
		WorldObjectProperties copyWorldObjectProperties = worldObjectProperties.deepCopy();
		
		copyWorldObjectProperties.get(Constants.GROUP).add(2);
		assertEquals(1, originalGroup.size());
	}
}
