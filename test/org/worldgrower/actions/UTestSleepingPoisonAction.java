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

public class UTestSleepingPoisonAction {

	@Test
	public void testExecute() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject well = createWell(world);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SLEEPING_POTION.generate(1f), 10);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		Actions.SLEEPING_POISON_ACTION.execute(performer, well, Args.EMPTY, world);
		
		assertEquals(9, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SLEEP_INDUCING_DRUG_STRENGTH));
		assertEquals(true, well.hasProperty(Constants.SLEEP_INDUCING_DRUG_STRENGTH));
	}
	
	@Test
	public void testIsValidTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createWell(world);
		
		assertEquals(true, Actions.SLEEPING_POISON_ACTION.isValidTarget(performer, target, world));
		assertEquals(false, Actions.SLEEPING_POISON_ACTION.isValidTarget(performer, performer, world));
	}
	
	@Test
	public void testIsActionPossible() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createWell(world);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SLEEPING_POTION.generate(1f), 10);
		
		assertEquals(true, Actions.SLEEPING_POISON_ACTION.isActionPossible(performer, target, Args.EMPTY, world));
	}
	
	@Test
	public void testDistance() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createWell(world);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SLEEPING_POTION.generate(1f), 10);
		
		assertEquals(0, Actions.SLEEPING_POISON_ACTION.distance(performer, target, Args.EMPTY, world));
	}

	private WorldObject createWell(World world) {
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject target = world.findWorldObjectById(wellId);
		return target;
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