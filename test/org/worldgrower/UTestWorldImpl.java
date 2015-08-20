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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.history.Turn;

public class UTestWorldImpl {

	private World world;
	
	public UTestWorldImpl() {
		world = createWorld();
		WorldObject worldObject = TestUtils.createWorldObject(6, "test");
		world.addWorldObject(worldObject);
	}
	
	@Test
	public void testFindWorldObjects() {
		List<WorldObject> result = world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6);
		
		assertEquals(1, result.size());
		assertEquals(6, result.get(0).getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindWorldObject() {
		WorldObject result = world.findWorldObject(Constants.NAME, "test");
		assertEquals(6, result.getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testRemoveWorldObject() {
		World world = createWorld();
		WorldObject worldObject = TestUtils.createWorldObject(6, "test");
		world.addWorldObject(worldObject);
		
		assertEquals(1, world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6).size());
		
		world.removeWorldObject(worldObject);
		assertEquals(0, world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6).size());
	}
	
	@Test
	public void testRemoveDependentWorldObject() {
		World world = createWorld();
		WorldObject house = TestUtils.createWorldObject(6, "test");
		WorldObject person = TestUtils.createIntelligentWorldObject(7, Constants.SMITH_ID, 6);
		
		world.addWorldObject(house);
		world.addWorldObject(person);
		
		assertEquals(6, person.getProperty(Constants.SMITH_ID).intValue());
		
		world.removeWorldObject(house);
		assertEquals(null, person.getProperty(Constants.SMITH_ID));
	}
	
	@Test
	public void testRemoveWorldObjectInIdContainer() {
		World world = createWorld();
		WorldObject person1 = TestUtils.createWorldObject(6, "test");
		IdMap idMap = new IdRelationshipMap();
		idMap.incrementValue(person1, 6);
		WorldObject person2 = TestUtils.createIntelligentWorldObject(7, Constants.RELATIONSHIPS, idMap);
		
		world.addWorldObject(person1);
		world.addWorldObject(person2);

		world.removeWorldObject(person1);
		assertEquals(false, idMap.contains(person1));
		assertEquals(-1, idMap.findBestId(w -> true, world));
		
	}
	
	@Test
	public void testSaveLoad() throws IOException {
		File fileToSave = File.createTempFile("worldgrower", ".sav");
		World world = createWorld();
		WorldObject house = TestUtils.createWorldObject(6, "test");
		WorldObject person = TestUtils.createIntelligentWorldObject(7, Constants.SMITH_ID, 6);
		
		world.addWorldObject(house);
		world.addWorldObject(person);
		
		world.getHistory().actionPerformed(new OperationInfo(person, house, new int[0], Actions.CUT_WOOD_ACTION), new Turn());
		
		world.save(fileToSave);
		world = WorldImpl.load(fileToSave);
		
		assertEquals(2, world.getWorldObjects().size());
		assertEquals(Actions.CUT_WOOD_ACTION, world.getHistory().getHistoryItem(0).getOperationInfo().getManagedOperation());
	}
	
	@Test
	public void testIdHigherThan128() {
		// this test is for java Integer caching, see http://stackoverflow.com/questions/3131136/integers-caching-in-java
		World world = createWorld();
		for(int i=0; i<300; i++) {
			int id = world.generateUniqueId();
			world.addWorldObject(TestUtils.createWorldObject(id, "test"));
		}
		assertEquals(290, world.findWorldObject(Constants.ID, 290).getProperty(Constants.ID).intValue());
	}

	private WorldImpl createWorld() {
		return new WorldImpl(0, 0, null, null);
	}
}
