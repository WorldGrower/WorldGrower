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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.WorldGenerator;
import org.worldgrower.generator.WorldGenerator.AddWorldObjectFunction;
import org.worldgrower.terrain.TerrainType;

public class UTestWorldGenerator {

	@Test
	public void testAddWorldObjects() {
		WorldGenerator worldGenerator = new WorldGenerator(0);
		World world = new WorldImpl(10, 10, null, null);
		AddWorldObjectFunctionImpl addWorldObjectFunction = new AddWorldObjectFunctionImpl();
		worldGenerator.addWorldObjects(world, 10, 10, 1, TerrainType.GRASLAND, addWorldObjectFunction);
		
		assertEquals(1, addWorldObjectFunction.getAddedWorldObjects().size());
		
		WorldObject addedWorldObject = addWorldObjectFunction.getAddedWorldObjects().get(0);
		assertEquals(9, addedWorldObject.getProperty(Constants.X).intValue());
		assertEquals(4, addedWorldObject.getProperty(Constants.Y).intValue());
	}
	
	private static class AddWorldObjectFunctionImpl implements AddWorldObjectFunction {

		private List<WorldObject> addedWorldObjects = new ArrayList<>();
		
		@Override
		public int addToWorld(int x, int y, World world) {
			Map<ManagedProperty<?>, Object> properties = new HashMap<>();			
			properties.put(Constants.X, x);
			properties.put(Constants.Y, y);
			addedWorldObjects.add(new WorldObjectImpl(properties));
			return 0;
		}

		public List<WorldObject> getAddedWorldObjects() {
			return addedWorldObjects;
		}
	}
}
