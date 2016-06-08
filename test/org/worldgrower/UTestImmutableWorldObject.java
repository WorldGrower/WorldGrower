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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.attribute.ManagedProperty;

public class UTestImmutableWorldObject {

	@Test
	public void testMutableProperties() {
		WorldObject sourceWorldObject = createWorldObject();
	
		WorldObject immutableWorldObject = new ImmutableWorldObject(sourceWorldObject, Arrays.asList(Constants.WATER), new DoNothingOnTurn());
		
		assertEquals(500, immutableWorldObject.getProperty(Constants.FOOD).intValue());
		assertEquals(500, immutableWorldObject.getProperty(Constants.WATER).intValue());
		
		immutableWorldObject.increment(Constants.FOOD, 100);
		immutableWorldObject.increment(Constants.WATER, 100);
		assertEquals(500, immutableWorldObject.getProperty(Constants.FOOD).intValue());
		assertEquals(600, immutableWorldObject.getProperty(Constants.WATER).intValue());
		
		immutableWorldObject.setProperty(Constants.FOOD, 100);
		immutableWorldObject.setProperty(Constants.WATER, 100);
		assertEquals(500, immutableWorldObject.getProperty(Constants.FOOD).intValue());
		assertEquals(100, immutableWorldObject.getProperty(Constants.WATER).intValue());
		
		immutableWorldObject.removeProperty(Constants.FOOD);
		immutableWorldObject.removeProperty(Constants.WATER);
		assertEquals(500, immutableWorldObject.getProperty(Constants.FOOD).intValue());
		assertEquals(null, immutableWorldObject.getProperty(Constants.WATER));
	}

	private WorldObject createWorldObject() {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.FOOD, 500);
		properties.put(Constants.WATER, 500);
		WorldObject sourceWorldObject = new WorldObjectImpl(properties);
		return sourceWorldObject;
	}	
}
