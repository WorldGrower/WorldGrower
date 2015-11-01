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
package org.worldgrower.goal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectFacade;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.Skill;

public class UTestFacadeUtils {

	@Test
	public void testCreateFacade() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(0);
		WorldObject worldObject = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		assertEquals(WorldObjectImpl.class, FacadeUtils.createFacade(worldObject, performer, target, world).getClass());
		
		WorldObject facade = TestUtils.createWorldObject(3, "facade");
		worldObject.setProperty(Constants.FACADE, facade);
		assertEquals(WorldObjectImpl.class, FacadeUtils.createFacade(worldObject, performer, target, world).getClass());
		
		performer.setProperty(Constants.BLUFF_SKILL, new Skill(20));
		assertEquals(WorldObjectFacade.class, FacadeUtils.createFacade(worldObject, performer, target, world).getClass());
	}
	
	@Test
	public void testPerformerIsSuccessFullyDisguised() {
		WorldObject performer = TestUtils.createWorldObject(0, "Test");
		assertEquals(false, FacadeUtils.performerIsSuccessFullyDisguised(performer));
	}
	
	@Test
	public void testDisguise() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 0);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 1);
		performer.setProperty(Constants.NAME, "performer");
		worldObject.setProperty(Constants.NAME, "worldObject");
		world.addWorldObject(performer);
		world.addWorldObject(worldObject);
		
		FacadeUtils.disguise(performer, 1, world);
		assertEquals("worldObject", performer.getProperty(Constants.FACADE).getProperty(Constants.NAME));
		
		FacadeUtils.disguise(performer, -1, world);
		assertEquals(null, performer.getProperty(Constants.FACADE));
	}
	
	@Test
	public void testCreateFacadeForSelf() {
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 0);
		performer.setProperty(Constants.NAME, "performer");
		WorldObject facade = TestUtils.createWorldObject(3, "facade");
		
		assertEquals("performer", FacadeUtils.createFacadeForSelf(performer).getProperty(Constants.NAME));
		
		performer.setProperty(Constants.FACADE, facade);
		assertEquals("facade", FacadeUtils.createFacadeForSelf(performer).getProperty(Constants.NAME));
	}
}
