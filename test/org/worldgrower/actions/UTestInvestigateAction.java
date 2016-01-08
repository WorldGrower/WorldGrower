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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.generator.TerrainGenerator;

public class UTestInvestigateAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = TerrainGenerator.generateFireTrap(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		Actions.INVESTIGATE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasKnowledge(target));
	}

	@Test
	public void testExecuteAlreadyKnown() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = TerrainGenerator.generateFireTrap(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		Actions.INVESTIGATE_ACTION.execute(performer, target, new int[0], world);
		Actions.INVESTIGATE_ACTION.execute(performer, target, new int[0], world);
		
		assertEquals(true, performer.getProperty(Constants.KNOWLEDGE_MAP).hasKnowledge(target));
		assertEquals(1, performer.getProperty(Constants.KNOWLEDGE_MAP).getKnowledge(target).size());
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int id = TerrainGenerator.generateFireTrap(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, id);
		
		assertEquals(true, Actions.INVESTIGATE_ACTION.isValidTarget(performer, performer, world));
		assertEquals(false, Actions.INVESTIGATE_ACTION.isValidTarget(performer, target, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(0, Actions.INVESTIGATE_ACTION.distance(performer, performer, new int[0], world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}