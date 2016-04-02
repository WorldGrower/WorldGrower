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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class UTestPoisonAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObject(Constants.ID, wellId);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.POISON.generate(1f), 10);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		Actions.POISON_ACTION.execute(performer, well, Args.EMPTY, world);
		
		assertEquals(9, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.POISON_DAMAGE));
		assertEquals(true, well.hasProperty(Constants.POISON_DAMAGE));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, wellId);
		
		assertEquals(true, Actions.POISON_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.POISON_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject target = world.findWorldObject(Constants.ID, wellId);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.POISON.generate(1f), 10);
		
		assertEquals(0, Actions.POISON_ACTION.distance(performer, target, Args.EMPTY, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}